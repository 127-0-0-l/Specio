package io.github._127_0_0_l.infra_db.adapters;

import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.ContentSourceRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContentSourceAdapter implements ContentSourcePort {
    private final ContentSourceRepository contentSourceRepository;
    private final DBMapper coreMapper;

    public ContentSourceAdapter(ContentSourceRepository contentSourceRepository,
        DBMapper coreMapper
    ){
        this.contentSourceRepository = contentSourceRepository;
        this.coreMapper = coreMapper;
    }

    @Override
    public Long create(ContentSource source) {
        try{
            var model = coreMapper.toDBContentSource(source);
            var saved = contentSourceRepository.save(model);
            return saved.getId();
        } catch (IllegalArgumentException e){
            log.error("Failed to create content source: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean update(ContentSource source) {
        try{
            if (!contentSourceRepository.existsById(source.id())){
                throw new IllegalArgumentException("source with id=" + source.id() + " doesn't exists");
            }
            var model = coreMapper.toDBContentSource(source);
            contentSourceRepository.save(model);
            return true;
        } catch (IllegalArgumentException e){
            log.error("Failed to update content sources: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean delete(Long sourceId) {
        if (contentSourceRepository.existsById(sourceId)){
            contentSourceRepository.deleteById(sourceId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ContentSource get(Long sourceId) {
        var source = contentSourceRepository.findById(sourceId);
        if (source.isPresent()){
            return coreMapper.toCoreContentSource(source.get());
        } else {
            return null;
        }
    }
}
