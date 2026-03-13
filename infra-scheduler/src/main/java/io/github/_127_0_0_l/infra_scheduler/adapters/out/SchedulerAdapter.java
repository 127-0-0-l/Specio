package io.github._127_0_0_l.infra_scheduler.adapters.out;

import org.springframework.stereotype.Component;

import io.github._127_0_0_l.core.ports.out.SchedulerPort;
import io.github._127_0_0_l.infra_scheduler.interfaces.FetchAndNotifyScheduler;

@Component
public class SchedulerAdapter implements SchedulerPort {
    private final FetchAndNotifyScheduler scheduler;

    public SchedulerAdapter (FetchAndNotifyScheduler scheduler){
        this.scheduler = scheduler;
    }

    @Override
    public void ScheduleFetchAndNotify(Long contentSourceId) {
        scheduler.schedule(contentSourceId);
    }
    
}
