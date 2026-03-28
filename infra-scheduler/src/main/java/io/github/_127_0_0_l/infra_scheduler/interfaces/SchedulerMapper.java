package io.github._127_0_0_l.infra_scheduler.interfaces;

import org.mapstruct.Mapper;

import io.github._127_0_0_l.core.models.NewRecordsCountLog;
import io.github._127_0_0_l.core.models.NewRecordsCountLogDTO;

@Mapper(componentModel = "spring")
public interface SchedulerMapper {
    io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLog toSchedulerNewRecordsCountLog(NewRecordsCountLog model);
    NewRecordsCountLog toCoreNewRecordsCountLog(io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLog model);

    io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLogDTO toSchedulerNewRecordsCountLogDTO(NewRecordsCountLogDTO model);
}
