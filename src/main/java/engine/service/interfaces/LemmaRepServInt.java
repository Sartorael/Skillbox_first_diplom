package engine.service.interfaces;

import engine.models.Indexing;
import engine.models.Lemma;
import engine.repo.LemmaRep;
import engine.service.LemmaRepositoryServ;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LemmaRepServInt implements LemmaRepositoryServ {

    private final LemmaRep lemmaRep;

    public LemmaRepServInt(LemmaRep lemmaRep) {
        this.lemmaRep = lemmaRep;
    }

    @Override
    public List<Lemma> getLemma(String lemmaName) {
        List<Lemma> lemmas = null;
        try{
            lemmas = lemmaRep.findByLemma(lemmaName);
        } catch (Exception e) {
            System.out.println(lemmaName);
            e.printStackTrace();
        }
        return lemmas;
    }

    @Override
    public synchronized void save(Lemma lemma) {
        lemmaRep.save(lemma);
    }

    @Override
    public long lemmaCount(){
        return lemmaRep.count();
    }

    @Override
    public long lemmaCount(long siteId){
        return lemmaRep.count(siteId);
    }

    @Override
    public synchronized void deleteAllLemmas(List<Lemma> lemmaList){
        lemmaRep.deleteAll(lemmaList);
    }

    @Override
    public List<Lemma> findLemmasByIndexing(List<Indexing> indexingList){
        int[] lemmaIdList = new int[indexingList.size()];
        for (int i = 0; i < indexingList.size(); i++) {
            lemmaIdList[i] = indexingList.get(i).getLemmaId();
        }
        return lemmaRep.findById(lemmaIdList);
    }
}
