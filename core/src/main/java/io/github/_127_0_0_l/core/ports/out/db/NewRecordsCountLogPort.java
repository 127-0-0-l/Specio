package io.github._127_0_0_l.core.ports.out.db;

import java.util.List;
import java.util.Optional;

import io.github._127_0_0_l.core.models.NewRecordsCountLog;

public interface NewRecordsCountLogPort {
    Optional<Long> create (NewRecordsCountLog newRecordsCountLog);

    boolean delete(Long id);

    List<NewRecordsCountLog> getBySourceId(Long sourceId);
}
