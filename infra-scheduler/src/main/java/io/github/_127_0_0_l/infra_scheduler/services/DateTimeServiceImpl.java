package io.github._127_0_0_l.infra_scheduler.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import org.springframework.stereotype.Service;

import io.github._127_0_0_l.infra_scheduler.interfaces.DateTimeService;

@Service
public class DateTimeServiceImpl implements DateTimeService {

    @Override
    public LocalDate getFirstWeekDay(LocalDateTime dateTime) {
        var monday = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return LocalDate.of(monday.getYear(), monday.getMonth(), monday.getDayOfMonth());
    }

    @Override
    public int getSecsFromFirstWeekDay(LocalDateTime dateTime) {
        return Math.abs((int)ChronoUnit.SECONDS.between(dateTime, getFirstWeekDay(dateTime).atStartOfDay()));
    }
    
}
