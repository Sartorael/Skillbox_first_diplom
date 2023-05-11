    package search_engine.repository;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import search_engine.model.LemmaData;
    import search_engine.model.SiteData;
    import java.util.List;

    @Repository
    public interface LemmaRepository extends JpaRepository<LemmaData, Integer> {

        LemmaData findFirstByLemmaAndSite(String lemma, SiteData siteData);
        List<LemmaData> findAllByLemma(String lemma);

        List<LemmaData> findAllBySite(SiteData siteData);
        int countBySite(SiteData siteData);
    }
