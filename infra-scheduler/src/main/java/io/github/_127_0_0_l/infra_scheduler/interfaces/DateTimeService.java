package io.github._127_0_0_l.infra_scheduler.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface DateTimeService {
    LocalDate getFirstWeekDay(LocalDateTime dateTime);

    int getSecsFromFirstWeekDay(LocalDateTime dateTime);

    Optional<Integer> calculateNextRunTime(Long sourceId, LocalDateTime lastRunTime);
}
