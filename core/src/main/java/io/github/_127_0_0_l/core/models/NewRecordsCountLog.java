package io.github._127_0_0_l.core.models;

import java.time.LocalDateTime;

public record NewRecordsCountLog(
        LocalDateTime dateTime,
        Long contentSourceId,
        int newRecordsCount
) {
}
