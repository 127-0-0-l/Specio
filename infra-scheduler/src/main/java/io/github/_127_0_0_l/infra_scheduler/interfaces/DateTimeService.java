package io.github._127_0_0_l.infra_scheduler.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeService {
    LocalDate getFirstWeekDay(LocalDateTime dateTime);

    int getSecsFromFirstWeekDay(LocalDateTime dateTime);
}
