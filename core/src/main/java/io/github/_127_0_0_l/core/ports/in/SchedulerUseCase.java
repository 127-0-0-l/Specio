package io.github._127_0_0_l.core.ports.in;

import io.github._127_0_0_l.core.models.NewRecordsCountLog;

import java.util.Optional;

public interface SchedulerUseCase {
    Optional<NewRecordsCountLog> runFetchAndNotify(Long contentSourceId);

    void saveNewRecordsCountLog(NewRecordsCountLog model);
}
