package io.github._127_0_0_l.infra_db.adapters.out;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github._127_0_0_l.core.models.LastRecord;
import io.github._127_0_0_l.core.ports.out.db.LastRecordPort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.LastRecordRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LastRecordAdapter implements LastRecordPort {

    private final LastRecordRepository lastRecordRepository;
    private final DBMapper mapper;

    public LastRecordAdapter(LastRecordRepository lastRecordRepository,
        DBMapper mapper
    ){
        this.lastRecordRepository = lastRecordRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean updateLastRecord(LastRecord lastRecord) {
        try{
            var record = lastRecordRepository.findByContentSourceId(lastRecord.contentSource().id());
            if (record.isPresent()){
                record.get().setRecordIdentifier(lastRecord.recordIdentifier());
                lastRecordRepository.save(record.get());
            } else {
                var entity = mapper.toDBLastRecord(lastRecord);
                lastRecordRepository.save(entity);
            }
            return true;
        } catch (IllegalArgumentException e){
            log.error("Failed to update tg chat: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Optional<LastRecord> getLastRecord(Long sourceId) {
        var record = lastRecordRepository.findByContentSourceId(sourceId);
        if (record.isPresent()){
            var mapped = mapper.toCoreLastRecord(record.get());
            return Optional.of(mapped);
        } else {
            return Optional.empty();
        }
    }
    
}
