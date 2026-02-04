package io.github._127_0_0_l.core.ports.out.db;

import java.util.List;

public interface FiltersPort {
    List<String> getContentSources();

    List<String> getRegions();

    List<String> getCities();

    List<String> getCurrencies();
}
