package model.dao;

import model.Validatable;

import java.util.List;

/**
 * Generic CRUD contract implemented by DAOs.
 *
 * @param <T>  entity type
 * @param <ID> identifier type
 */
public interface CrudDAO<T, ID> {

    T create(T entity) throws Validatable.ValidationException;

    T read(ID id);

    T update(T entity) throws Validatable.ValidationException;

    void delete(ID id);

    List<T> list();
}