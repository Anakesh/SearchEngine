package databaseComunication.repository;

import java.util.List;

/**
 * Created by Pavel on 26.01.2019.
 */
public interface IRepository<E> {
    void add(E entity);
    List<E> readAll();
    E getById(Long entityId);
    void deleteById(Long entityId);
}
