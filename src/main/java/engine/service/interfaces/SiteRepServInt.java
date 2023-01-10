package engine.service.interfaces;

import engine.models.Site;
import engine.repo.SiteRepository;
import engine.service.SiteRepositoryServ;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SiteRepServInt implements SiteRepositoryServ {

    private final SiteRepository siteRepository;

    public SiteRepServInt(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public Site getSite(String url) {
        return siteRepository.findByUrl(url);
    }
    public Site getSite(int siteId) {
        Optional<Site> optional = siteRepository.findById(siteId);
        Site site = null;
        if(optional.isPresent()){
            site = optional.get();
        }
        return site;
    }

    @Override
    public synchronized void save(Site site) {
        try {
            siteRepository.save(site);
        } catch (Exception e) {}
    }

    @Override
    public long siteCount(){
        return siteRepository.count();
    }

    @Override
    public List<Site> getAllSites() {
        List<Site> siteList = new ArrayList<>();
        Iterable<Site> it = siteRepository.findAll();
        it.forEach(siteList::add);
        return siteList;
    }
}
