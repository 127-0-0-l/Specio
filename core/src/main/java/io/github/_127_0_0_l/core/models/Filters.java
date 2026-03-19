package io.github._127_0_0_l.core.models;

import java.util.Set;

public record Filters(
        Long id,
        Set<Region> regions,
        Set<City> cities,
        Integer priceFrom,
        Integer priceTo,
        Integer yearFrom,
        Integer yearTo
) { }
