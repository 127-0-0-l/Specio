package io.github._127_0_0_l.infra_filter.services;

import io.github._127_0_0_l.infra_filter.interfaces.VehicleAdvertFilter;
import io.github._127_0_0_l.infra_filter.models.Filters;
import io.github._127_0_0_l.infra_filter.models.VehicleAdvert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleAdvertFilterService implements VehicleAdvertFilter {
    @Override
    public List<VehicleAdvert> filter(List<VehicleAdvert> adverts, Filters filters) {
        var stream = adverts.stream()
                .filter(a -> a.pricePrimary() >= filters.priceFrom()
                        && a.pricePrimary() <= filters.priceTo())
                .filter(a -> a.year() >= filters.yearFrom()
                        && a.year() <= filters.yearTo());

        if (filters.cities().size() > 0){
            stream = stream.filter(a -> filters.cities().contains(a.city()));
        }

        return stream.toList();
    }
}
