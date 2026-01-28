package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.ParserConfig;

public interface ParserConfigPort {
    boolean create(ParserConfig parserConfig);

    boolean update(ParserConfig parserConfig);

    boolean delete(String sourceId);

    ParserConfig get(String sourceId);
}
