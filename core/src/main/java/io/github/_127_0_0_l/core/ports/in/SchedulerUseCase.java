package io.github._127_0_0_l.core.ports.in;

import io.github._127_0_0_l.core.models.NewAdvertLog;
import io.github._127_0_0_l.core.models.ContentSource;

import java.util.List;

public interface SchedulerUseCase {
    List<NewAdvertLog> getNewAdverts(ContentSource site);
}
