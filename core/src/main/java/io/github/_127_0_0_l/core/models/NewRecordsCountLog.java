package io.github._127_0_0_l.core.models;

import java.time.LocalDate;

public record NewRecordsCountLog(
        Long contentSourceId,
        LocalDate firstWeekDay,
        int weekSecondsFrom,
        int weekSecondsTo,
        int newRecordsCount
) {
}
