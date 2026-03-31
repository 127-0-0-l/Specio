package io.github._127_0_0_l.infra_scheduler.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github._127_0_0_l.infra_scheduler.adapters.in.NewRecordsCountLogService;
import io.github._127_0_0_l.infra_scheduler.interfaces.DateTimeService;
import io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLog;

@Service
public class DateTimeServiceImpl implements DateTimeService {

    private final NewRecordsCountLogService nrclService;
    private final int MAX_RECORDS_COUNT = 25;

    public DateTimeServiceImpl(
        NewRecordsCountLogService nrclService
    ){
        this.nrclService = nrclService;
    }

    @Override
    public LocalDate getFirstWeekDay(LocalDateTime dateTime) {
        var monday = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return LocalDate.of(monday.getYear(), monday.getMonth(), monday.getDayOfMonth());
    }

    @Override
    public int getSecsFromFirstWeekDay(LocalDateTime dateTime) {
        return Math.abs((int)ChronoUnit.SECONDS.between(dateTime, getFirstWeekDay(dateTime).atStartOfDay()));
    }

    @Override
    public Optional<Integer> calculateNextRunTime(Long sourceId, LocalDateTime lastRunTime) {
        var nrcLogs = nrclService.getBySourceId(sourceId);

        var groupedLogs = nrcLogs.stream()
            .collect(Collectors.groupingBy(l -> l.firstWeekDay()));

        var predictions = getPredictions(groupedLogs, lastRunTime);

        return getAvg(predictions);
    }

    private List<Integer> getPredictions (
            Map<LocalDate, List<NewRecordsCountLog>> groupedLogs,
            LocalDateTime lastRunTime) {
        int currentWeekSeconds = getSecsFromFirstWeekDay(lastRunTime);
        List<Integer> predictions = new ArrayList<>();

        for (var weekLogs : groupedLogs.values()) {
            var matchingLog = findMatchingLog(weekLogs, currentWeekSeconds);

            if (matchingLog.isPresent()){
                int recordsRemain = MAX_RECORDS_COUNT;
                int prediction = 0;

                while (recordsRemain > 0){
                    int allSeconds = matchingLog.get().weekSecondsTo() - matchingLog.get().weekSecondsFrom();
                    int seconds = matchingLog.get().weekSecondsTo() - currentWeekSeconds;
                    int records = matchingLog.get().newRecordsCount() * seconds / allSeconds;

                    if (records < recordsRemain) {
                        prediction += seconds;
                        recordsRemain -= records;
                        matchingLog = findMatchingLog(weekLogs, matchingLog.get().weekSecondsTo());
                        if (matchingLog.isEmpty()){
                            recordsRemain = 0;
                        }
                    } else {
                        prediction += seconds * recordsRemain / records;
                        recordsRemain = 0;
                    }
                }

                predictions.add(prediction);
            }
        }

        return predictions;
    }

    private Optional<NewRecordsCountLog> findMatchingLog(List<NewRecordsCountLog> list, int currentWeekSeconds){
        return list.stream()
            .filter(l ->
                l.weekSecondsFrom() <= currentWeekSeconds
                && l.weekSecondsTo() > currentWeekSeconds)
            .findFirst();
    }

    private Optional<Integer> getAvg(List<Integer> list) {
        if (list.size() > 0){
            int avg = list.stream()
                .collect(Collectors.averagingInt(p -> p))
                .intValue();
            return Optional.of(avg);
        } else {
            return Optional.empty();
        }
    }
}
