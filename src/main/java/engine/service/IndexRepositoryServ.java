package engine.service;

import engine.models.Indexing;

import java.util.List;

public interface IndexRepositoryServ {
    List<Indexing> getAllIndexingByLemmaId(int lemmaId);
    List<Indexing> getAllIndexingByPageId(int pageId);
    void deleteAllIndexing(List<Indexing> indexingList);
    Indexing getIndexing (int lemmaId, int pageId);
    void save(Indexing indexing);

}
