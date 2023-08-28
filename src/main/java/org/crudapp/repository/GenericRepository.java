package org.crudapp.repository;

import org.crudapp.exceptions.NotFoundException;
import org.crudapp.exceptions.StatusDeletedException;

import java.util.List;

public interface GenericRepository<T, ID> {
    T getById(ID id) throws NotFoundException;
    List<T> getAll();
    T save(T t);
    T update(T t,ID id) throws NotFoundException, StatusDeletedException;
    void deleteById(ID id) throws NotFoundException, StatusDeletedException;
}
