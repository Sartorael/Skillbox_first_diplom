package engine.repo;

import engine.models.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends CrudRepository<Site, Integer> {
    Site findByUrl (String url);
}
