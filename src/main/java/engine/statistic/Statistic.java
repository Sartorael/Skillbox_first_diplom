package engine.statistic;

import engine.models.Site;
import engine.models.Status;
import engine.service.LemmaRepositoryServ;
import engine.service.PageRepositoryServ;
import engine.service.SiteRepositoryServ;
import engine.service.StatisticServ;
import engine.service.indRespEnt.Detail;
import engine.service.indRespEnt.Statt;
import engine.service.indRespEnt.entityConstructor;
import engine.service.response.StatRespServ;
import org.apache.commons.logging.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Statistic implements StatisticServ {

    private static final Log log = LogFactory.getLog(Statistic.class);

    private final SiteRepositoryServ siteRepositoryServ;
    private final LemmaRepositoryServ lemmaRepositoryServ;
    private final PageRepositoryServ pageRepositoryServ;

    public Statistic(SiteRepositoryServ siteRepositoryServ,
                     LemmaRepositoryServ lemmaRepositoryServ,
                     PageRepositoryServ pageRepositoryServ) {
        this.siteRepositoryServ = siteRepositoryServ;
        this.lemmaRepositoryServ = lemmaRepositoryServ;
        this.pageRepositoryServ = pageRepositoryServ;
    }

    public StatRespServ getStatistic(){
        entityConstructor entityConstructor = getTotal();
        List<Site> siteList = siteRepositoryServ.getAllSites();
        Detail[] details = new Detail[siteList.size()];
        for (int i = 0; i < siteList.size(); i++) {
            details[i] = getDetailed(siteList.get(i));
        }
        log.info("Получение статистики.");
        return new StatRespServ(true, new Statt(entityConstructor, details));
    }

    private entityConstructor getTotal(){
        long sites = siteRepositoryServ.siteCount();
        long lemmas = lemmaRepositoryServ.lemmaCount();
        long pages = pageRepositoryServ.pageCount();
        boolean isIndexing = isSitesIndexing();
        return new entityConstructor(sites, pages, lemmas, isIndexing);

    }

    private Detail getDetailed(Site site){
        String url = site.getUrl();
        String name = site.getName();
        Status status = site.getStatus();
        long statusTime = site.getStatusTime().getTime();
        String error = site.getLastError();
        long pages = pageRepositoryServ.pageCount(site.getId());
        long lemmas = lemmaRepositoryServ.lemmaCount(site.getId());
        return new Detail(url, name, status, statusTime, error, pages, lemmas);
    }

    private boolean isSitesIndexing(){
        boolean is = true;
        for(Site s : siteRepositoryServ.getAllSites()){
            if(!s.getStatus().equals(Status.INDEXED)){
                is = false;
                break;
            }
        }
    return is;
    }
}
