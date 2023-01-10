package engine.service.interfaces;

import engine.models.Page;
import engine.repo.PageRep;
import engine.service.PageRepositoryServ;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageRepServInt implements PageRepositoryServ {

    private final PageRep pageRep;

    public PageRepServInt(PageRep pageRep) {
        this.pageRep = pageRep;
    }

    @Override
    public Page getPage(String pagePath) {
        Page page = new Page();
        try {
            page = pageRep.findByPath(pagePath);
        } catch (Exception e) {}
        return page;
    }

    @Override
    public synchronized void save(Page page) {
        pageRep.save(page);
    }

    @Override
    public Optional<Page> findPageById(int id) {
        return pageRep.findById(id);
    }

    @Override
    public Optional<Page> findPageByPageIdAndSiteId(int pageId, int siteId) {
        return pageRep.findByIdAndSiteId(pageId, siteId);
    }

    @Override
    public long pageCount(){
        return pageRep.count();
    }

    @Override
    public long pageCount(long siteId){
        return pageRep.count(siteId);
    }

    @Override
    public void deletePage(Page page) {
        pageRep.delete(page);
    }

}
