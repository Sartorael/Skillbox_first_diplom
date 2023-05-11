    package search_engine.parser;
    import org.apache.commons.logging.Log;
    import org.apache.commons.logging.LogFactory;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Element;
    import org.jsoup.select.Elements;
    import search_engine.model.PageData;
    import search_engine.model.SiteData;
    import search_engine.model.SiteStatus;
    import search_engine.service.impl.IndexServiceImpl;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.RecursiveTask;
    import java.util.stream.Collectors;

    public class SiteCrawler extends RecursiveTask<PageData> {

        private PageData pageData;
        private SiteData siteData;
        private IndexServiceImpl indexService;
        private List<PageData> pageDataStore;
        private final static Log log = LogFactory.getLog(SiteCrawler.class);

        public SiteCrawler(PageData pageData, List<PageData> pageDataStore, IndexServiceImpl indexService) {

            this.pageData = pageData;
            this.indexService = indexService;
            this.siteData = pageData.getSite();
            this.pageDataStore = pageDataStore;
        }

        @Override
        protected PageData compute() {

            if (siteData.getStatus() == SiteStatus.FAILED) {
                return pageData;
            }
            Document doc;
            try {
                doc = indexService.getDocument(siteData.getUrl().concat(pageData.getPath()));
            } catch (IOException e) {
                pageData.setCode(404);
                log.debug(e.getStackTrace());
                return pageData;
            }
            pageData.setCode(doc.connection().response().statusCode());
            pageData.setContent(doc.html());
            List<SiteCrawler> siteCrawlerList = getUrlChildResearcherList(doc);
            for (SiteCrawler siteCrawler : siteCrawlerList) {
                siteCrawler.join();
            }
            synchronized (pageDataStore) {
                List<PageData> pagesToInsert = pageDataStore
                        .stream()
                        .filter(p -> p.getCode() > 0)
                        .collect(Collectors.toList());
                if (pagesToInsert.size() > 500) {
                    indexService.insertAllData(pagesToInsert, siteData);
                    pageDataStore.removeAll(pagesToInsert);
                }
            }
            return pageData;
        }

        private List<SiteCrawler> getUrlChildResearcherList(Document doc) {

            List<SiteCrawler> siteCrawlerList = new ArrayList<>();
            Elements elements = doc.select("a[href~=^[^#?]+$]");
            for (Element element : elements) {
                String urlChild = element.attr("abs:href");
                String relativeUrlChild = indexService.getRelativeUrl(urlChild, siteData.getUrl());
                if (relativeUrlChild.isBlank() || relativeUrlChild.length() > PageData.MAX_LENGTH_PATH) {
                    continue;
                }
                PageData pageDataChild;
                synchronized (pageDataStore) {
                    if (indexService.getPageRepository().existsByPathAndSite(relativeUrlChild, siteData)
                            || pageDataStore.stream().anyMatch(p -> p.getPath().equals(relativeUrlChild))) {
                        continue;
                    }
                    pageDataChild = new PageData(siteData, relativeUrlChild, 0, "");
                    pageDataStore.add(pageDataChild);
                }
                SiteCrawler siteCrawler = new SiteCrawler(pageDataChild, pageDataStore, indexService);
                siteCrawler.fork();
                siteCrawlerList.add(siteCrawler);
            }
            return siteCrawlerList;
        }
    }