package engine.service;

import engine.models.Field;

import java.util.List;

public interface FieldRepositoryServ {
    Field getFieldByName(String fieldName);
    void save(Field field);
    List<Field> getAllField();


}
