package io.github._127_0_0_l.core.models;

import java.util.Set;

public record Filters(
        Long id,
        Set<String> regions,
        Set<String> cities,
        int priceFrom,
        int priceTo,
        int yearFrom,
        int yearTo
) { }
