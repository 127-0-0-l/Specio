package io.github._127_0_0_l.core.ports.out.db;

import java.util.Optional;

import io.github._127_0_0_l.core.models.LastRecord;

public interface LastRecordPort {
    boolean updateLastRecord(LastRecord lastRecord);

    Optional<LastRecord> getLastRecord(Long sourceId);
}
