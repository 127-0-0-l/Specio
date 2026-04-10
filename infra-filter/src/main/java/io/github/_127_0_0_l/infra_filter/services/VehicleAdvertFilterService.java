package io.github._127_0_0_l.infra_filter.services;

import io.github._127_0_0_l.infra_filter.adapters.in.DataProvider;
import io.github._127_0_0_l.infra_filter.interfaces.VehicleAdvertFilter;
import io.github._127_0_0_l.infra_filter.models.Filters;
import io.github._127_0_0_l.infra_filter.models.VehicleAdvert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleAdvertFilterService implements VehicleAdvertFilter {

    private final DataProvider dataProvider;

    public VehicleAdvertFilterService (
            DataProvider dataProvider
    ){
        this.dataProvider = dataProvider;
    }

    @Override
    public List<VehicleAdvert> filter(List<VehicleAdvert> adverts, Filters filters) {
        var stream = adverts.stream()
                .filter(a -> a.pricePrimary() >= filters.priceFrom()
                        && a.pricePrimary() <= filters.priceTo())
                .filter(a -> a.year() >= filters.yearFrom()
                        && a.year() <= filters.yearTo());

        if (filters.regions().size() > 0){
            var allRegions = dataProvider.getAllRegions();
            var rNames = filters.regions().stream()
                .map(r -> r.name())
                .toList();

            allRegions = allRegions.stream()
                .filter(r -> rNames.contains(r.name()))
                .toList();

            var cities = allRegions.stream()
                .flatMap(r -> r.cities().stream())
                .map(c -> c.name())
                .toList();

            stream = stream.filter(a -> cities.contains(a.city()));
        }

        if (filters.cities().size() > 0){
            var names = filters.cities().stream()
                .map(c -> c.name())
                .toList();

            stream = stream.filter(a -> names.contains(a.city()));
        }

        return stream.toList();
    }

    @Override
    public List<VehicleAdvert> filter(List<VehicleAdvert> adverts, String lastRecordIdentifier) {
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
