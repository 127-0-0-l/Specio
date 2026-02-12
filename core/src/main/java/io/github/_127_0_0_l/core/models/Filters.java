package io.github._127_0_0_l.core.models;

import java.util.List;

public record Filters(
        List<String> regions,
        List<String> cities,
        int priceFrom,
        int priceTo,
        int yearFrom,
        int yearTo
) { }
