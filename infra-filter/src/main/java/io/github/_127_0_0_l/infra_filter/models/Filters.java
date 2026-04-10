package io.github._127_0_0_l.infra_filter.models;

import java.util.Set;

public record Filters(
        Set<Region> regions,
        Set<City> cities,
        Integer priceFrom,
        Integer priceTo,
        Integer yearFrom,
        Integer yearTo
) {
}
