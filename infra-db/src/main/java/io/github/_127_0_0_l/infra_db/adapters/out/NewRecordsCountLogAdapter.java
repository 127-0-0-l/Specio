package io.github._127_0_0_l.infra_db.adapters.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github._127_0_0_l.core.models.NewRecordsCountLog;
import io.github._127_0_0_l.core.ports.out.db.NewRecordsCountLogPort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.NewRecordsCountLogRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NewRecordsCountLogAdapter implements NewRecordsCountLogPort {

    private final NewRecordsCountLogRepository nrcRepository;
    private final DBMapper mapper;

    public NewRecordsCountLogAdapter(NewRecordsCountLogRepository nrcRepository,
        DBMapper mapper
    ){
        this.nrcRepository = nrcRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Long> create(NewRecordsCountLog newRecordsCountLog) {
        try{
            var entity = mapper.toDBNewRecordsCountLog(newRecordsCountLog);
            var saved = nrcRepository.save(entity);
            return Optional.of(saved.getId());
        } catch (IllegalArgumentException e){
            log.error("Failed to create new records count log: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Long id) {
        if (nrcRepository.existsById(id)){
            nrcRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<NewRecordsCountLog> getBySourceId(Long sourceId) {
        var entities = nrcRepository.findAllByContentSourceId(sourceId);
        return mapper.toCoreNewRecordsCountLogs(entities);
    }
    
}
