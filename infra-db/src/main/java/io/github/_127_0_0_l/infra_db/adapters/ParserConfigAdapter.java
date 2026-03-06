package io.github._127_0_0_l.infra_db.adapters;

import io.github._127_0_0_l.core.models.ParserConfig;
import io.github._127_0_0_l.core.ports.out.db.ParserConfigPort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.ParserConfigRepository;

import org.springframework.stereotype.Component;

@Component
public class ParserConfigAdapter implements ParserConfigPort {
    private final ParserConfigRepository parserConfigRepository;
    private final DBMapper mapper;

    public ParserConfigAdapter (ParserConfigRepository parserConfigRepository,
        DBMapper mapper
    ){
        this.parserConfigRepository = parserConfigRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean create(ParserConfig parserConfig) {
        return false;
    }

    @Override
    public boolean update(ParserConfig parserConfig) {
        return false;
    }

    @Override
    public boolean delete(String sourceId) {
        return false;
    }

    @Override
    public ParserConfig get(Long sourceId) {
        var config = parserConfigRepository.findById(sourceId);
        if (config.isPresent()){
            return mapper.toCoreParserConfig(config.get());
        } else {
            return null;
        }
    }
}
