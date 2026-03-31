package io.github._127_0_0_l.infra_scheduler.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import io.github._127_0_0_l.infra_scheduler.adapters.in.NewRecordsCountLogService;
import io.github._127_0_0_l.infra_scheduler.adapters.in.SchedulerUseCaseService;
import io.github._127_0_0_l.infra_scheduler.interfaces.DateTimeService;
import io.github._127_0_0_l.infra_scheduler.interfaces.FetchAndNotifyScheduler;
import io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLog;
import io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLogDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FetchAndNotifySchedulerService implements FetchAndNotifyScheduler {
    private final TaskScheduler taskScheduler;
    private final SchedulerUseCaseService schedulerUseCaseService;
    private final NewRecordsCountLogService nrclService;
    private final DateTimeService dateTimeService;
    private int interval = 120;
    private final int MAX_RECORDS_COUNT = 25;
    private Map<Long, LocalDateTime> sourceToLastRunTime = new HashMap<>();

    public FetchAndNotifySchedulerService (
            TaskScheduler taskScheduler,
            SchedulerUseCaseService schedulerUseCaseService,
            NewRecordsCountLogService nrclService,
            DateTimeService dateTimeService){
        this.taskScheduler = taskScheduler;
        this.schedulerUseCaseService = schedulerUseCaseService;
        this.nrclService = nrclService;
        this.dateTimeService = dateTimeService;
    }

    @Override
    public void schedule(Long contentSourceId) {
        schedule(contentSourceId, Instant.now());
    }

    private void schedule(Long sourceId, Instant nextRunTime){
        taskScheduler.schedule(() -> {
            var result = schedulerUseCaseService.runFetchAndNotify(sourceId);

            if (result.isPresent()){
                updateInterval(result.get());
                if (result.get().newRecordsCount() < MAX_RECORDS_COUNT &&
                        sourceToLastRunTime.containsKey(sourceId)){
                    saveNewRecordsCountLog(result.get(), sourceId);
                }
                sourceToLastRunTime.put(sourceId, result.get().dateTime());
            }

            schedule(sourceId, calculateNextTime(sourceId, sourceToLastRunTime.get(sourceId)));
            log.info("schedule task for " + interval + " secconds");
        }, nextRunTime);
    }

    private void updateInterval(NewRecordsCountLogDTO nrcl){
        interval += interval * (MAX_RECORDS_COUNT / 3 - nrcl.newRecordsCount()) / MAX_RECORDS_COUNT;
    }

    private void saveNewRecordsCountLog(NewRecordsCountLogDTO nrcl, Long sourceId){
        LocalDateTime lastRun = sourceToLastRunTime.get(sourceId);
        var log = new NewRecordsCountLog(
            sourceId,
            dateTimeService.getFirstWeekDay(nrcl.dateTime()),
            dateTimeService.getSecsFromFirstWeekDay(lastRun),
            dateTimeService.getSecsFromFirstWeekDay(nrcl.dateTime()),
            nrcl.newRecordsCount());
        nrclService.saveNewRecordsCountLog(log);
    }
    
    private Instant calculateNextTime(Long sourceId, LocalDateTime lastTunTime){
        var prediction = dateTimeService.calculateNextRunTime(sourceId, lastTunTime);
        int seconds;

        if (prediction.isPresent()){
            seconds = (interval + prediction.get() / 2);
        } else {
            seconds = interval;
        }

        return Instant.now().plusSeconds(seconds);
    }
}
