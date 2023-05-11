    package search_engine.repository;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import search_engine.model.SiteData;
    import search_engine.model.SiteStatus;

    @Repository
    public interface SiteRepository extends JpaRepository<SiteData, Integer> {

        SiteData findFirstByName(String name);
        SiteData findFirstByUrl(String url);
        boolean existsByStatus(SiteStatus status);
    }
