package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.ContentSource;

import java.util.List;
import java.util.Optional;

public interface ContentSourcePort {
    Optional<Long> create(ContentSource source);

    boolean update(ContentSource source);

    boolean delete(Long sourceId);

    Optional<ContentSource> get(Long sourceId);

    List<ContentSource> getAll();
}
