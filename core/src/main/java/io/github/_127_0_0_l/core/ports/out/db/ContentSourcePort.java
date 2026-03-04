package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.ContentSource;

public interface ContentSourcePort {
    public Long create(ContentSource source);

    public boolean update(ContentSource source);

    public boolean delete(Long sourceId);

    ContentSource get(Long sourceId);
}
