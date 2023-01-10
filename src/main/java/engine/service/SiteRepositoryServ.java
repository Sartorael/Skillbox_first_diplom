package engine.service;

import engine.models.Site;

import java.util.List;

public interface SiteRepositoryServ {
    Site getSite (String url);
    Site getSite (int siteId);
    void save(Site site);
    long siteCount();
    List<Site> getAllSites();
}
