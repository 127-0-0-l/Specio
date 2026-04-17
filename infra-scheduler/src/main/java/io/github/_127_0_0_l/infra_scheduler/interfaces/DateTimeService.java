package io.github._127_0_0_l.infra_scheduler.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Optional;

public interface DateTimeService {
    LocalDate getFirstWeekDay(LocalDateTime dateTime);

    int getSecsFromFirstWeekDay(LocalDateTime dateTime);

    int getDiffInSecs(Temporal from, Temporal to);

    Optional<Integer> calculateNextRunTime(Long sourceId, LocalDateTime lastRunTime);
}
