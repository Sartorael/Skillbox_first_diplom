    package search_engine.repository;

    import org.springframework.data.jpa.repository.JpaRepository;
    import search_engine.model.IndexData;
    import search_engine.model.PageData;

    public interface IndexRepository extends JpaRepository<IndexData, Integer> {

        IndexData findFirstByLemma_LemmaAndPage(String lemma, PageData pageData);
        boolean existsByLemma_LemmaAndPage(String lemma, PageData pageData);
    }
