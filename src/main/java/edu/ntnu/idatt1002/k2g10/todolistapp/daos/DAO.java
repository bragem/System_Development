package edu.ntnu.idatt1002.k2g10.todolistapp.daos;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface for data access objects.
 * @param <T> Entity type of the DAO.
 * @author hasanoma
 */
public interface DAO<T> {
    Optional<T> get(String identifier) throws SQLException;

    void create(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    boolean exists(T entity) throws SQLException;
}
