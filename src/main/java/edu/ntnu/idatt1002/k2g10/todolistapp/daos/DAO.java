package edu.ntnu.idatt1002.k2g10.todolistapp.daos;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface for data access objects.
 * 
 * @param <T>
 *            Entity type the DAO interacts with.
 * 
 * @author hasanoma
 */
public interface DAO<T> {
    /**
     * Gets object T with given identifier.
     *
     * @param identifier
     *            Identifier.
     *
     * @return T
     *
     * @throws SQLException
     *             If query fails.
     */
    Optional<T> get(String identifier) throws SQLException;

    /**
     * Create the given object T.
     *
     * @param entity
     *            T.
     *
     * @throws SQLException
     *             If query fails.
     */
    void create(T entity) throws SQLException;

    /**
     * Updates the given object T.
     *
     * @param entity
     *            T.
     *
     * @throws SQLException
     *             If query fails.
     */
    void update(T entity) throws SQLException;

    /**
     * Deletes the given object T.
     *
     * @param entity
     *            T.
     *
     * @throws SQLException
     *             If query fails.
     */
    void delete(T entity) throws SQLException;

    /**
     * Gets whether object T exists or not.
     *
     * @param entity
     *            T.
     *
     * @return True if exists, false if not.
     *
     * @throws SQLException
     *             If query fails.
     */
    boolean exists(T entity) throws SQLException;
}
