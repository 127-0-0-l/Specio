package io.github._127_0_0_l.infra_scheduler.models;

import java.time.LocalDate;

public record NewRecordsCountLog(
        Long contentSourceId,
        LocalDate firstWeekDay,
        int weekSecondsFrom,
        int weekSecondsTo,
        int newRecordsCount
) {
    
}
