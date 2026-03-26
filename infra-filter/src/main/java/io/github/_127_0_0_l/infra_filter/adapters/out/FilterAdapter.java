package io.github._127_0_0_l.infra_filter.adapters.out;

import io.github._127_0_0_l.core.models.Filters;
import io.github._127_0_0_l.core.models.LastRecord;
import io.github._127_0_0_l.core.models.VehicleAdvert;
import io.github._127_0_0_l.core.ports.out.FilterPort;
import io.github._127_0_0_l.infra_filter.interfaces.FilterMapper;
import io.github._127_0_0_l.infra_filter.interfaces.VehicleAdvertFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterAdapter implements FilterPort {
    private final FilterMapper mapper;
    private final VehicleAdvertFilter vehicleAdvertFilter;

    public FilterAdapter (FilterMapper mapper,
        VehicleAdvertFilter vehicleAdvertFilter) {
        this.mapper = mapper;
        this.vehicleAdvertFilter = vehicleAdvertFilter;
    }

    @Override
    public List<VehicleAdvert> filterVehicleAdverts(List<VehicleAdvert> adverts, Filters filters) {
        var filterAdverts = mapper.toFilterVehicleAdverts(adverts);
        var filterFilters = mapper.toFilterFilters(filters);

        var result = vehicleAdvertFilter.filter(filterAdverts, filterFilters);
        return mapper.toCoreVehicleAdverts(result);
    }

    @Override
    public List<VehicleAdvert> filterVehicleAdverts(List<VehicleAdvert> adverts, String lastRecordIdentifier) {
        var lastRecord = adverts.stream()
            .filter(a -> a.url().equals(lastRecordIdentifier))
            .findFirst();

        if (lastRecord.isPresent()){
            return adverts.subList(0, adverts.indexOf(lastRecord.get()));
        } else {
            return adverts;
        }
    }
}
