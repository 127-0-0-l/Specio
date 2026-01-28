package io.github._127_0_0_l.core.ports.out;

import io.github._127_0_0_l.core.models.ContentSource;

public interface SchedulerPort {
    void ScheduleFor(ContentSource site);
}
