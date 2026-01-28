package io.github._127_0_0_l.infra_db.adapters;

import io.github._127_0_0_l.core.models.ParserConfig;
import io.github._127_0_0_l.core.ports.out.db.ParserConfigPort;
import org.springframework.stereotype.Component;

@Component
public class ParserConfigAdapter implements ParserConfigPort {
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
    public ParserConfig get(String sourceId) {
        return null;
    }
}
