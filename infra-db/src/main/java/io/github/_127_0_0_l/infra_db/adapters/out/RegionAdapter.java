package io.github._127_0_0_l.infra_db.adapters.out;

import java.util.List;

import org.springframework.stereotype.Component;

import io.github._127_0_0_l.core.models.Region;
import io.github._127_0_0_l.core.ports.out.db.RegionsPort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.RegionRepository;

@Component
public class RegionAdapter implements RegionsPort {

    private final RegionRepository regionRepository;
    private final DBMapper mapper;

    public RegionAdapter (RegionRepository regionRepository,
        DBMapper mapper
    ) {
        this.regionRepository = regionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Region> getAll() {
        var dbRegions = regionRepository.findAll();
        return mapper.toCoreRegions(dbRegions);
    }
}
