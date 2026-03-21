package io.github._127_0_0_l.infra_filter.interfaces;

import io.github._127_0_0_l.core.models.City;
import io.github._127_0_0_l.core.models.Filters;
import io.github._127_0_0_l.core.models.Region;
import io.github._127_0_0_l.core.models.VehicleAdvert;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FilterMapper {
    List<VehicleAdvert> toCoreVehicleAdverts (List<io.github._127_0_0_l.infra_filter.models.VehicleAdvert> model);
    List<io.github._127_0_0_l.infra_filter.models.VehicleAdvert> toFilterVehicleAdverts (List<VehicleAdvert> model);

    io.github._127_0_0_l.infra_filter.models.Filters toFilterFilters (Filters model);

    default String regionToString (Region model){
        return model.name();
    }

    default String cityToString (City model) {
        return model.name();
    }
}
