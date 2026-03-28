package io.github._127_0_0_l.infra_scheduler.services;

import java.time.Instant;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import io.github._127_0_0_l.infra_scheduler.adapters.in.SchedulerUseCaseService;
import io.github._127_0_0_l.infra_scheduler.interfaces.FetchAndNotifyScheduler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FetchAndNotifySchedulerService implements FetchAndNotifyScheduler {
    private final TaskScheduler taskScheduler;
    private final SchedulerUseCaseService schedulerUseCaseService;
    private int interval = 120;
    private final int MAX_RECORDS_COUNT = 25;

    public FetchAndNotifySchedulerService (TaskScheduler taskScheduler,
            SchedulerUseCaseService schedulerUseCaseService){
        this.taskScheduler = taskScheduler;
        this.schedulerUseCaseService = schedulerUseCaseService;
    }

    @Override
    public void schedule(Long contentSourceId) {
        schedule(contentSourceId, Instant.now());
    }

    private void schedule(Long sourceId, Instant nextRunTime){
        taskScheduler.schedule(() -> {
            var result = schedulerUseCaseService.runFetchAndNotify(sourceId);
            if (result.isPresent()){
                interval += interval * (MAX_RECORDS_COUNT / 3 - result.get().newRecordsCount()) / MAX_RECORDS_COUNT;

                if (result.get().newRecordsCount() < MAX_RECORDS_COUNT){
                    schedulerUseCaseService.saveNewRecordsCountLog(result.get());
                }
            }
            schedule(sourceId, calculateNextTime());
            log.info("schedule task for " + interval + " secconds");
        }, nextRunTime);
    }
    
    private Instant calculateNextTime(){
        return Instant.now().plusSeconds(interval);
    }
}
