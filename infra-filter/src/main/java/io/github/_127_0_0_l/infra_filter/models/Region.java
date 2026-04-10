package io.github._127_0_0_l.infra_filter.models;

import java.util.List;

public record Region(
    Long id,
    String name,
    List<City> cities
) {}
