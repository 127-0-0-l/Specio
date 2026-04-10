package io.github._127_0_0_l.infra_filter.adapters.in;

import java.util.List;

import org.springframework.stereotype.Component;

import io.github._127_0_0_l.core.ports.out.db.RegionsPort;
import io.github._127_0_0_l.infra_filter.interfaces.FilterMapper;
import io.github._127_0_0_l.infra_filter.models.Region;

@Component
public class DataProvider {

    private final FilterMapper mapper;
    private final RegionsPort regionsPort;

    public DataProvider(
            FilterMapper mapper,
            RegionsPort regionsPort){
        this.mapper = mapper;
        this.regionsPort = regionsPort;
    }

    public List<Region> getAllRegions(){
        var regions = regionsPort.getAll();
        return mapper.toFilterRegions(regions);
    }
}
