package io.github._127_0_0_l.models;

import java.util.List;

public record Filters(
        List<Site> sites,
        List<String> regions,
        List<String> cities,
        int priceFrom,
        int priceTo,
        int yearFrom,
        int yearTo
) { }
