package edu.ntnu.idatt1002.k2g10.todolistapp.daos;

import edu.ntnu.idatt1002.k2g10.todolistapp.models.User;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * Data access object for {@link User}.
 * 
 * @author hasanoma, trthingnes
 */
public class UserDAO implements DAO<User> {
    private final EntityManager em;

    /**
     * Creates a new user DAO.
     * 
     * @param em
     *            Persistence entity manager.
     */
    public UserDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Gets user with the given username.
     * 
     * @param username
     *            Username.
     * 
     * @return User.
     * 
     * @throws SQLException
     *             If query fails.
     */
    @Override
    public Optional<User> get(String username) throws SQLException {
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.username LIKE :username")
                    .setParameter("username", username);
            return Optional.of(Objects.requireNonNull((User) query.getSingleResult()));
        } catch (NoResultException | NullPointerException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(String.format("Unable to get user '%s': %s", username, e.getMessage()));
        }
    }

    /**
     * Create the given user.
     * 
     * @param user
     *            User.
     * 
     * @throws SQLException
     *             If query fails.
     */
    @Override
    public void create(User user) throws SQLException {
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new SQLException(String.format("Unable to create user '%s': %s", user.getUsername(), e.getMessage()));
        }
    }

    /**
     * Updates the given user.
     * 
     * @param user
     *            User.
     * 
     * @throws SQLException
     *             If query fails.
     */
    @Override
    public void update(User user) throws SQLException {
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new SQLException(String.format("Unable to update user '%s': %s", user.getUsername(), e.getMessage()));
        }
    }

    /**
     * Deletes the given user.
     * 
     * @param user
     *            User.
     * 
     * @throws SQLException
     *             If query fails.
     */
    @Override
    public void delete(User user) throws SQLException {
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new SQLException(String.format("Unable to delete user '%s': %s", user.getUsername(), e.getMessage()));
        }
    }

    /**
     * Gets whether user exists or not.
     * 
     * @param user
     *            User.
     * 
     * @return True if exists, false if not.
     * 
     * @throws SQLException
     *             If query fails.
     */
    @Override
    public boolean exists(User user) throws SQLException {
        return get(user.getUsername()).isPresent();
    }
}
