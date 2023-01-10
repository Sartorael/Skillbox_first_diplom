package engine.service;

import engine.models.Indexing;
import engine.models.Lemma;

import java.util.List;

public interface LemmaRepositoryServ {
    List<Lemma> getLemma (String lemmaName);
    void save(Lemma lemma);
    long lemmaCount();
    long lemmaCount(long siteId);
    void deleteAllLemmas(List<Lemma> lemmaList);
    List<Lemma> findLemmasByIndexing(List<Indexing> indexingList);
}
