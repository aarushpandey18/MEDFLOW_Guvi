package com.medflow.dao;

import com.medflow.exception.DatabaseException;
import java.util.List;
import java.util.Optional;

/** Generic DAO interface (Generics + Interfaces). */
public interface GenericDAO<T, ID> {
    int save(T entity) throws DatabaseException;
    Optional<T> findById(ID id) throws DatabaseException;
    List<T> findAll() throws DatabaseException;
    boolean delete(ID id) throws DatabaseException;
}
