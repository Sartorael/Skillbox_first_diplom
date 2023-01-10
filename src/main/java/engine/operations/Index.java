    package engine.operations;
//1

    import engine.models.Field;
    import engine.models.Site;
    import engine.models.Status;
    import engine.service.*;
    import engine.settings.SearchSettings;
    import org.apache.commons.logging.Log;
    import org.apache.commons.logging.LogFactory;
    import org.springframework.stereotype.Component;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.concurrent.Executors;
    import java.util.concurrent.ThreadPoolExecutor;
    import java.util.concurrent.TimeUnit;

    @Component
    public class Index {


    private final SearchSettings searchSettings;

    private final FieldRepositoryServ fieldRepositoryServ;
    private final SiteRepositoryServ siteRepositoryServ;
    private final IndexRepositoryServ indexRepositoryServ;
    private final PageRepositoryServ pageRepositoryServ;
    private final LemmaRepositoryServ lemmaRepositoryServ;

    public Index(SearchSettings searchSettings,
                 FieldRepositoryServ fieldRepositoryServ,
                 SiteRepositoryServ siteRepositoryServ,
                 IndexRepositoryServ indexRepositoryServ,
                 PageRepositoryServ pageRepositoryServ,
                 LemmaRepositoryServ lemmaRepositoryServ) {
        this.searchSettings = searchSettings;
        this.fieldRepositoryServ = fieldRepositoryServ;
        this.siteRepositoryServ = siteRepositoryServ;
        this.indexRepositoryServ = indexRepositoryServ;
        this.pageRepositoryServ = pageRepositoryServ;
        this.lemmaRepositoryServ = lemmaRepositoryServ;
    }
    private final static Log logginng = LogFactory.getLog(Index.class);
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    public boolean fullSiteIndexing() throws InterruptedException {
        init();
        boolean isIndexing;
        List<Site> siteList = getSitesFromConfig();
        for (Site site : siteList) {
            isIndexing = startIndexing(site);
            if (!isIndexing){
                stopIndexing();
                return false;
            }
        }
        return true;
    }

    public String checkForIndex(String url) throws InterruptedException {
        List<Site> siteList = siteRepositoryServ.getAllSites();
        String baseUrl = "";
        for(Site site : siteList) {
            if(site.getStatus() != Status.INDEXED) {
                return "false";
            }
            if(url.contains(site.getUrl())){
                baseUrl = site.getUrl();
            }
        }
        if(baseUrl.isEmpty()){
            return "not found";
        } else {
            Site site = siteRepositoryServ.getSite(baseUrl);
            site.setUrl(url);
            IndexationOfTheSites indexing = new IndexationOfTheSites(
                    site,
                    searchSettings,
                    fieldRepositoryServ,
                    siteRepositoryServ,
                    indexRepositoryServ,
                    pageRepositoryServ,
                    lemmaRepositoryServ,
                    false);
            executor.execute(indexing);
            site.setUrl(baseUrl);
            siteRepositoryServ.save(site);
            return "true";
        }
    }


    private void init() {
        Field fieldTitle = new Field("title", "title", 1.0f);
        Field fieldBody = new Field("body", "body", 0.8f);
        if (fieldRepositoryServ.getFieldByName("title") == null) {
            fieldRepositoryServ.save(fieldTitle);
            fieldRepositoryServ.save(fieldBody);
        }
    }

    private boolean startIndexing(Site site) throws InterruptedException {
        Site site1 = siteRepositoryServ.getSite(site.getUrl());
        if (site1 == null) {
            siteRepositoryServ.save(site);
            IndexationOfTheSites indexing = new IndexationOfTheSites(
                    siteRepositoryServ.getSite(site.getUrl()),
                    searchSettings,
                    fieldRepositoryServ,
                    siteRepositoryServ,
                    indexRepositoryServ,
                    pageRepositoryServ,
                    lemmaRepositoryServ,
                    true);
            executor.execute(indexing);
            return true;
        } else {
            if (!site1.getStatus().equals(Status.INDEXING)){
                IndexationOfTheSites indexing = new IndexationOfTheSites(
                        siteRepositoryServ.getSite(site.getUrl()),
                        searchSettings,
                        fieldRepositoryServ,
                        siteRepositoryServ,
                        indexRepositoryServ,
                        pageRepositoryServ,
                        lemmaRepositoryServ,
                        true);
                executor.execute(indexing);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean stopIndexing(){
        boolean isThreadAlive = false;
        if(executor.getActiveCount() == 0){
            return false;
        }

        executor.shutdownNow();
        try {
            isThreadAlive = executor.awaitTermination(5,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logginng.error("Ошибка закрытия потоков: " + e);
        }
        if (isThreadAlive){
            List<Site> siteList = siteRepositoryServ.getAllSites();
            for(Site site : siteList) {
                site.setStatus(Status.FAILED);
                siteRepositoryServ.save(site);
            }
        }
        return isThreadAlive;
    }

    private List<Site> getSitesFromConfig() {
        List<Site> sitesList = new ArrayList<>();
        List<HashMap<String, String>> sites = searchSettings.getSite();
        for (HashMap<String, String> map : sites) {
            String url = "";
            String name = "";
            for (Map.Entry<String, String> siteInfo : map.entrySet()) {
                if (siteInfo.getKey().equals("name")) {
                    name = siteInfo.getValue();
                }
                if (siteInfo.getKey().equals("url")) {
                    url = siteInfo.getValue();
                }
            }
            Site currentSite = new Site();
            currentSite.setUrl(url);
            currentSite.setName(name);
            currentSite.setStatus(Status.FAILED);
            sitesList.add(currentSite);
        }
        return sitesList;
        }
    }
