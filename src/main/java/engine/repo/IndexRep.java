package engine.repo;

import engine.models.Indexing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRep extends CrudRepository<Indexing, Integer> {
    List<Indexing> findByLemmaId (int lemmaId);
    List<Indexing> findByPageId (int pageId);
    Indexing findByLemmaIdAndPageId (int lemmaId, int pageId);


}

