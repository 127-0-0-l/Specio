package io.github._127_0_0_l.infra_db.adapters.out;

import java.util.List;

import org.springframework.stereotype.Component;

import io.github._127_0_0_l.core.models.City;
import io.github._127_0_0_l.core.ports.out.db.CitiesPort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.CityRepository;

@Component
public class CityAdapter implements CitiesPort {

    private final CityRepository cityRepository;
    private final DBMapper mapper;

    public CityAdapter (CityRepository cityRepository,
        DBMapper mapper
    ) {
        this.cityRepository = cityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<City> getAll() {
        var cities = cityRepository.findAll();
        return mapper.toCoreCities(cities);
    }
}
