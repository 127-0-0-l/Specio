package io.github._127_0_0_l.core.services;

import org.springframework.stereotype.Service;

import io.github._127_0_0_l.core.ports.out.SchedulerPort;

@Service
public class SchedulerService {
    private final SchedulerPort schedulerPort;

    public SchedulerService (SchedulerPort schedulerPort){
        this.schedulerPort = schedulerPort;
    }

    public void runScheduler(){
        schedulerPort.ScheduleFetchAndNotify(Long.valueOf(1));
    }
}
