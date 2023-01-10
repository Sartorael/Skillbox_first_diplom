package engine.service.interfaces;

import engine.models.Field;
import engine.repo.FieldRep;
import engine.service.FieldRepositoryServ;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FieldRepServInt implements FieldRepositoryServ {

    private final FieldRep fieldRep;

    public FieldRepServInt(FieldRep fieldRep) {
        this.fieldRep = fieldRep;
    }

    @Override
    public Field getFieldByName(String fieldName) {
        return fieldRep.findByName(fieldName);
    }
    @Override
    public synchronized void save(Field field) {
        fieldRep.save(field);
    }

    @Override
    public List<Field> getAllField() {
        List<Field> list = new ArrayList<>();
        Iterable<Field> iterable = fieldRep.findAll();
        iterable.forEach(list::add);
        return list;
    }
}
