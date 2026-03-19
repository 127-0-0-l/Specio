package io.github._127_0_0_l.core.ports.out.db;

import java.util.List;

import io.github._127_0_0_l.core.models.City;

public interface CitiesPort {
    List<City> getAll();
}
