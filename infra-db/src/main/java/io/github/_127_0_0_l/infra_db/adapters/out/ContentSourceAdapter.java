package io.github._127_0_0_l.infra_db.adapters.out;

import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.ContentSourceRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ContentSourceAdapter implements ContentSourcePort {
    private final ContentSourceRepository contentSourceRepository;
    private final DBMapper mapper;

    public ContentSourceAdapter(ContentSourceRepository contentSourceRepository,
        DBMapper mapper
    ){
        this.contentSourceRepository = contentSourceRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Long> create(ContentSource source) {
        try{
            var model = mapper.toDBContentSource(source);
            var saved = contentSourceRepository.save(model);
            return Optional.of(saved.getId());
        } catch (IllegalArgumentException e){
            log.error("Failed to create content source: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(ContentSource source) {
        try{
            if (!contentSourceRepository.existsById(source.id())){
                throw new IllegalArgumentException("source with id=" + source.id() + " doesn't exists");
            }
            var model = mapper.toDBContentSource(source);
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
    public Optional<ContentSource> get(Long sourceId) {
        var source = contentSourceRepository.findById(sourceId);
        if (source.isPresent()){
            var mapped = mapper.toCoreContentSource(source.get());
            return Optional.of(mapped);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ContentSource> getAll(){
        var sources = contentSourceRepository.findAll();
        return mapper.toCoreContentSources(sources);
    }
}
