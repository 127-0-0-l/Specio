package io.github._127_0_0_l.ports.in;

import io.github._127_0_0_l.models.NewAdvertLog;
import io.github._127_0_0_l.models.Site;

import java.util.List;

public interface SchedulerUseCase {
    List<NewAdvertLog> getNewAdverts(Site site);
}
