package io.github._127_0_0_l.core.ports.out;

import io.github._127_0_0_l.core.models.Filters;
import io.github._127_0_0_l.core.models.VehicleAdvert;

import java.util.List;

public interface FilterPort {
    List<VehicleAdvert> filterVehicleAdverts(List<VehicleAdvert> adverts, Filters filters);
}
