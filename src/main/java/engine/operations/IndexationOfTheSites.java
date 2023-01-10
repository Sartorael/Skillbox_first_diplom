package engine.operations;

import engine.settings.SearchSettings;
import engine.models.*;
import engine.service.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class IndexationOfTheSites extends Thread{
    private final Site site;
    private final SearchSettings searchSettings;
    private final FieldRepositoryServ fieldRepositoryServ;
    private final SiteRepositoryServ siteRepositoryServ;
    private final IndexRepositoryServ indexRepositoryServ;
    private final PageRepositoryServ pageRepositoryServ;
    private final LemmaRepositoryServ lemmaRepositoryServ;
    private final boolean allSite;

    public IndexationOfTheSites(Site site,
                                SearchSettings searchSettings,
                                FieldRepositoryServ fieldRepositoryServ,
                                SiteRepositoryServ siteRepositoryServ,
                                IndexRepositoryServ indexRepositoryServ,
                                PageRepositoryServ pageRepositoryServ,
                                LemmaRepositoryServ lemmaRepositoryServ,
                                boolean allSite) {
        this.site = site;
        this.searchSettings = searchSettings;
        this.fieldRepositoryServ = fieldRepositoryServ;
        this.siteRepositoryServ = siteRepositoryServ;
        this.indexRepositoryServ = indexRepositoryServ;
        this.pageRepositoryServ = pageRepositoryServ;
        this.lemmaRepositoryServ = lemmaRepositoryServ;
        this.allSite = allSite;
    }



    @Override
    public void run() {
        try {
            if (allSite) {
                runAllIndexing();
            } else {
                runOneSiteIndexing(site.getUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void runAllIndexing() {
        site.setStatus(Status.INDEXING);
        site.setStatusTime(new Date());
        siteRepositoryServ.save(site);
        SiteMapBuilder builder = new SiteMapBuilder(site.getUrl(), this.isInterrupted());
        builder.builtSiteMap();
        List<String> allSiteUrls = builder.getSiteMap();
        for(String url : allSiteUrls) {
            runOneSiteIndexing(url);
        }
    }

    public void runOneSiteIndexing(String searchUrl) {
        site.setStatus(Status.INDEXING);
        site.setStatusTime(new Date());
        siteRepositoryServ.save(site);
        List<Field> fieldList = getFieldListFromDB();
        try {
            Page page = getSearchPage(searchUrl, site.getUrl(), site.getId());
            Page checkPage = pageRepositoryServ.getPage(searchUrl.replaceAll(site.getUrl(), ""));
            if (checkPage != null){
                prepareDbToIndexing(checkPage);
            }
            TreeMap<String, Integer> map = new TreeMap<>();
            TreeMap<String, Float> indexing = new TreeMap<>();
            for (Field field : fieldList){
                String name = field.getName();
                float weight = field.getWeight();
                String stringByTeg = getStringByTeg(name, page.getContent());
                Morph analyzer = new Morph();
                TreeMap<String, Integer> tempMap = analyzer.textAnalyzer(stringByTeg);
                map.putAll(tempMap);
                indexing.putAll(indexingLemmas(tempMap, weight));
            }
            lemmaToDB(map, site.getId());
            map.clear();
            pageToDb(page);
            indexingToDb(indexing, page.getPath());
            indexing.clear();
        }
        catch (UnsupportedMimeTypeException e) {
            site.setLastError("Формат страницы не поддерживается: " + searchUrl);
            site.setStatus(Status.FAILED);
        }
        catch (IOException e) {
            site.setLastError("Ошибка чтения страницы: " + searchUrl + "\n" + e.getMessage());
            site.setStatus(Status.FAILED);
        }
        finally {
            siteRepositoryServ.save(site);
        }
        site.setStatus(Status.INDEXED);
        siteRepositoryServ.save(site);
    }


    private void pageToDb(Page page) {
            pageRepositoryServ.save(page);
    }

    private Page getSearchPage(String url, String baseUrl, int siteId) throws IOException {
        Page page = new Page();
        Connection.Response response = Jsoup.connect(url)
                .userAgent(searchSettings.getAgent())
                .referrer("http://www.google.com")
                .execute();

        String content = response.body();
        String path = url.replaceAll(baseUrl, "");
        int code = response.statusCode();
        page.setCode(code);
        page.setPath(path);
        page.setContent(content);
        page.setSiteId(siteId);
        return page;
    }

    private List<Field> getFieldListFromDB() {
        List<Field> list = new ArrayList<>();
        Iterable<Field> iterable = fieldRepositoryServ.getAllField();
        iterable.forEach(list::add);
        return list;
    }

    private String getStringByTeg (String teg, String html) {
        String string = "";
        Document document = Jsoup.parse(html);
        Elements elements = document.select(teg);
        StringBuilder builder = new StringBuilder();
        elements.forEach(element -> builder.append(element.text()).append(" "));
        if (!builder.isEmpty()){
            string = builder.toString();
        }
        return string;
    }

    private void lemmaToDB (TreeMap<String, Integer> lemmaMap, int siteId) {
        for (Map.Entry<String, Integer> lemma : lemmaMap.entrySet()) {
            String lemmaName = lemma.getKey();
            List<Lemma> lemma1 = lemmaRepositoryServ.getLemma(lemmaName);
            Lemma lemma2 = lemma1.stream().
                    filter(lemma3 -> lemma3.getSiteId() == siteId).
                    findFirst().
                    orElse(null);
            if (lemma2 == null){
                Lemma newLemma = new Lemma(lemmaName, 1, siteId);
                lemmaRepositoryServ.save(newLemma);
            } else {
                        int count = lemma2.getFrequency();
                        lemma2.setFrequency(++count);
                        lemmaRepositoryServ.save(lemma2);
            }
        }
    }

    private TreeMap<String, Float> indexingLemmas (TreeMap<String, Integer> lemmas, float weight) {
        TreeMap<String, Float> map = new TreeMap<>();
        for (Map.Entry<String, Integer> lemma : lemmas.entrySet()) {
            String name = lemma.getKey();
            float w;
            if (!map.containsKey(name)) {
                w = (float) lemma.getValue() * weight;
            } else {
                w = map.get(name) + ((float) lemma.getValue() * weight);
            }
            map.put(name, w);
        }
        return map;
    }

    private void indexingToDb (TreeMap<String, Float> map, String path){
        Page page = pageRepositoryServ.getPage(path);
        int pathId = page.getId();
        int siteId = page.getSiteId();
        for (Map.Entry<String, Float> lemma : map.entrySet()) {

            String lemmaName = lemma.getKey();
            List<Lemma> lemma1 = lemmaRepositoryServ.getLemma(lemmaName);
            for (Lemma l : lemma1) {
                if (l.getSiteId() == siteId) {
                    int lemmaId = l.getId();
                    Indexing indexing = new Indexing(pathId, lemmaId, lemma.getValue());
                    indexRepositoryServ.save(indexing);
                }
            }
        }
    }

    private void prepareDbToIndexing(Page page) {
        List<Indexing> indexingList = indexRepositoryServ.getAllIndexingByPageId(page.getId());
        List<Lemma> allLemmasIdByPage = lemmaRepositoryServ.findLemmasByIndexing(indexingList);
        lemmaRepositoryServ.deleteAllLemmas(allLemmasIdByPage);
        indexRepositoryServ.deleteAllIndexing(indexingList);
        pageRepositoryServ.deletePage(page);
    }
}
