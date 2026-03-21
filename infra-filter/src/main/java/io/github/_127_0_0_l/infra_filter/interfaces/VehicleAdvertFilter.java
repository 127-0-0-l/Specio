package io.github._127_0_0_l.infra_filter.interfaces;

import io.github._127_0_0_l.infra_filter.models.Filters;
import io.github._127_0_0_l.infra_filter.models.VehicleAdvert;

import java.util.List;

public interface VehicleAdvertFilter {
    List<VehicleAdvert> filter (List<VehicleAdvert> adverts, Filters filters);
}
