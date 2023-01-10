package engine.service.interfaces;

import engine.models.Indexing;
import engine.repo.IndexRep;
import engine.service.IndexRepositoryServ;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexRepoServInt implements IndexRepositoryServ {

    private final IndexRep indexRep;

    public IndexRepoServInt(IndexRep indexRep) {
        this.indexRep = indexRep;
    }

    @Override
    public List<Indexing> getAllIndexingByLemmaId(int lemmaId) {
        return indexRep.findByLemmaId(lemmaId);
    }

    @Override
    public List<Indexing> getAllIndexingByPageId(int pageId) {
        return indexRep.findByPageId(pageId);
    }

    @Override
    public synchronized void deleteAllIndexing(List<Indexing> indexingList){
        indexRep.deleteAll(indexingList);
    }

    @Override
    public Indexing getIndexing(int lemmaId, int pageId) {
        Indexing indexing = null;
        try{
            indexing = indexRep.findByLemmaIdAndPageId(lemmaId, pageId);
        } catch (Exception e) {
            System.out.println("lemmaId: " + lemmaId + " + pageId: " + pageId + " not unique");
            e.printStackTrace();
        }
        return indexing;
    }

    @Override
    public synchronized void save(Indexing indexing) {
        indexRep.save(indexing);
    }

}
