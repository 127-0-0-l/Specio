package io.github._127_0_0_l.infra_db.adapters;

import io.github._127_0_0_l.core.ports.out.db.FiltersPort;

import java.util.List;

public class FiltersAdapter implements FiltersPort {
    @Override
    public List<String> getContentSources() {
        return List.of();
    }

    @Override
    public List<String> getRegions() {
        return List.of();
    }

    @Override
    public List<String> getCities() {
        return List.of();
    }

    @Override
    public List<String> getCurrencies() {
        return List.of();
    }
}
