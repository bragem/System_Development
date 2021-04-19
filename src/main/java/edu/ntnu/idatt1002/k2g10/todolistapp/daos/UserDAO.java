package edu.ntnu.idatt1002.k2g10.todolistapp.daos;

import edu.ntnu.idatt1002.k2g10.todolistapp.models.User;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * Data access object for {@link User}.
 */
public class UserDAO implements DAO<User> {
    private final EntityManager em;

    public UserDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> get(String username) throws SQLException {
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE u.username LIKE :username").setParameter("username", username);
            return Optional.of(Objects.requireNonNull((User) query.getSingleResult()));
        }
        catch (NoResultException | NullPointerException e) {
            return Optional.empty();
        }
        catch (Exception e) {
            throw new SQLException(String.format("Unable to get user '%s': %s", username, e.getMessage()));
        }
    }

    @Override
    public void create(User user) throws SQLException {
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch(Exception e) {
            if(Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new SQLException(String.format("Unable to create user '%s': %s", user.getUsername(), e.getMessage()));
        }
    }

    @Override
    public void update(User user) throws SQLException {
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(user);
            transaction.commit();
        } catch(Exception e) {
            if(Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new SQLException(String.format("Unable to update user '%s': %s", user.getUsername(), e.getMessage()));
        }
    }

    @Override
    public void delete(User user) throws SQLException {
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.remove(user);
            transaction.commit();
        } catch(Exception e) {
            if(Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new SQLException(String.format("Unable to delete user '%s': %s", user.getUsername(), e.getMessage()));
        }
        }

    @Override
    public boolean exists(User user) throws SQLException {
        return get(user.getUsername()).isPresent();
    }
}
