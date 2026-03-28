package io.github._127_0_0_l.core.ports.out;

public interface SchedulerPort {
    void scheduleFetchAndNotify(Long contentSourceId);
}
