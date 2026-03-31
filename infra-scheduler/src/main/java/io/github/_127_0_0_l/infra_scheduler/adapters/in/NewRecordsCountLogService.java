package io.github._127_0_0_l.infra_scheduler.adapters.in;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github._127_0_0_l.core.ports.out.db.NewRecordsCountLogPort;
import io.github._127_0_0_l.infra_scheduler.interfaces.SchedulerMapper;
import io.github._127_0_0_l.infra_scheduler.models.NewRecordsCountLog;

@Service
public class NewRecordsCountLogService {
    private final NewRecordsCountLogPort nrclPort;
    private final SchedulerMapper mapper;

    public NewRecordsCountLogService(
        NewRecordsCountLogPort nrclPort,
        SchedulerMapper mapper
    ){
        this.nrclPort = nrclPort;
        this.mapper = mapper;
    }

    public void saveNewRecordsCountLog(NewRecordsCountLog model){
        nrclPort.create(mapper.toCoreNewRecordsCountLog(model));
    }

    public List<NewRecordsCountLog> getBySourceId(Long sourceId){
        return mapper.toSchedulerNewRecordsCountLogs(nrclPort.getBySourceId(sourceId));
    }
}
