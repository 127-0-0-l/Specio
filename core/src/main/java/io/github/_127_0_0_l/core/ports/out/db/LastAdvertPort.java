package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.LastAdvert;

public interface LastAdvertPort {
    boolean updateLastAdvert(LastAdvert lastAdvert);

    LastAdvert getLastAdvert(String siteName);
}
