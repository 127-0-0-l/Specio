package io.github._127_0_0_l.ports.out.db;

import io.github._127_0_0_l.models.Site;

import java.util.List;

public interface SitePort {
    boolean createSite(Site site);

    boolean updateSite(Site site);

    boolean deleteSite(String siteName);

    Site getSite(String siteName);

    List<Site> getAllSites();
}
