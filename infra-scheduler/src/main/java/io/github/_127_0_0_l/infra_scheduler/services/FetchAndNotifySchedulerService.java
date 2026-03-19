package io.github._127_0_0_l.infra_scheduler.services;

import java.time.Instant;
import java.util.Random;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import io.github._127_0_0_l.core.ports.in.SchedulerUseCase;
import io.github._127_0_0_l.core.services.ContentProviderService;
import io.github._127_0_0_l.infra_scheduler.interfaces.FetchAndNotifyScheduler;

@Component
public class FetchAndNotifySchedulerService implements FetchAndNotifyScheduler {
    private final TaskScheduler taskScheduler;
    //private final SchedulerUseCase schedulerUseCase;
    private final ContentProviderService providerService;

    public FetchAndNotifySchedulerService (TaskScheduler taskScheduler,
            //SchedulerUseCase schedulerUseCase,
            ContentProviderService providerService){
        this.taskScheduler = taskScheduler;
        //this.schedulerUseCase = schedulerUseCase;
        this.providerService = providerService;
    }

    @Override
    public void schedule(Long contentSourceId) {
        schedule(contentSourceId, Instant.now());
    }

    private void schedule(Long sourceId, Instant nextRunTime){
        taskScheduler.schedule(() -> {
            //schedulerUseCase.runFetchAndNotify(sourceId);
            providerService.showContent();
            schedule(sourceId, calculateNextTime());
        }, nextRunTime);
    }
    
    private Instant calculateNextTime(){
        return Instant.now().plusSeconds(300 + new Random().nextInt(30));
    }
}
