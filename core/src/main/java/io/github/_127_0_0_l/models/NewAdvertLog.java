package io.github._127_0_0_l.models;

import java.time.LocalDateTime;

public record NewAdvertLog(
        LocalDateTime dateTime,
        Site site,
        int newAdvertsCount
) {
}
