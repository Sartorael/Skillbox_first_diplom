package engine.repo;

import engine.models.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRep extends CrudRepository<Field, Integer> {
    Field findByName(String name);
}