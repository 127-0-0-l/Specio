package io.github._127_0_0_l.infra_scheduler.adapters.in;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github._127_0_0_l.infra_scheduler.interfaces.SchedulerMapper;
import io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLog;
import io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLogDTO;
import io.github._127_0_0_l.core.ports.in.SchedulerUseCase;

@Component
public class SchedulerUseCaseService {
    private final SchedulerUseCase schedulerUseCase;
    private final SchedulerMapper mapper;

    public SchedulerUseCaseService(SchedulerUseCase schedulerUseCase,
        SchedulerMapper mapper
    ){
        this.schedulerUseCase = schedulerUseCase;
        this.mapper = mapper;
    }

    public Optional<NewRecordsCountLogDTO> runFetchAndNotify(Long sourceId){
        var result = schedulerUseCase.runFetchAndNotify(sourceId);
        if (result.isPresent()){
            return Optional.of(mapper.toSchedulerNewRecordsCountLogDTO(result.get()));
        } else {
            return Optional.empty();
        }
    }

    public void saveNewRecordsCountLog(NewRecordsCountLog model){
        schedulerUseCase.saveNewRecordsCountLog(mapper.toCoreNewRecordsCountLog(model));
    }
}
