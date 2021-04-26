package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import edu.ntnu.idatt1002.k2g10.todolistapp.Theme;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.crypto.HashException;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.crypto.PasswordHashAlgorithm;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User entity.
 * 
 * @author hasanro, Bragemi, trthingnes
 */
@Entity
@Table(name = "APP_USER")
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String passwordHash;

    @NotBlank
    private String passwordSalt;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @OneToOne(cascade = { CascadeType.ALL })
    private final TaskList taskList = new TaskList();

    @NotNull
    private Theme theme;

    /**
     * Constructs a new user instance.
     * 
     * @param username
     *            Username of user.
     * @param firstname
     *            First name of user.
     * @param lastname
     *            Last name of user.
     * @param email
     *            Email address of user.
     */
    public User(String username, String firstname, String lastname, String email, String password, Theme theme) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.passwordSalt = PasswordHashAlgorithm.PBKDF2.generateSalt();
        try {
            this.passwordHash = PasswordHashAlgorithm.PBKDF2.getSaltedHash(password, passwordSalt);
        } catch (HashException ignored) {
            /* This will throw SQL exception in DAO anyway. */}
        this.theme = theme;
    }

    /**
     * Constructs a new user element.
     * 
     * Used by persistence.
     * 
     * @see #User(String, String, String, String, String, Theme)
     */
    public User() {

    }

    /**
     * Verifies that the given password matches the one on record.
     * 
     * @param password
     *            Password to verify.
     * 
     * @return True if password matches, false if not.
     */
    public boolean verifyPassword(String password) {
        return PasswordHashAlgorithm.PBKDF2.verifyHash(password, passwordSalt, passwordHash);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setPassword(String password) {
        this.passwordSalt = PasswordHashAlgorithm.PBKDF2.generateSalt();
        try {
            this.passwordHash = PasswordHashAlgorithm.PBKDF2.getSaltedHash(password, passwordSalt);
        } catch (HashException ignored) {
            /* This will throw SQL exception in DAO anyway. */
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
