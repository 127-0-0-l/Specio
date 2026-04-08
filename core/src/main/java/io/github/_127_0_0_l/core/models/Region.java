package io.github._127_0_0_l.core.models;

import java.util.List;

public record Region(
    Long id,
    String name,
    List<City> cities
) {
    
}
