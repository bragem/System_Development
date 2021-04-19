package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * User class. Contains {@link TaskList}
 * 
 * @author hasanro, Bragemi, trthingnes
 */
@Entity
@Table(name="APP_USER")
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String passwordHash;

    @NotBlank
    private String passwordSalt;

    @Email
    private String email;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @OneToOne
    private final TaskList taskList = new TaskList();

    /**
     * Constructor of User class
     * 
     * @param username
     *            Username of user
     * @param firstname
     *            First name of user
     * @param lastname
     *            Surname of user
     * @param email
     *            email address of user
     */
    public User(String username, String firstname, String lastname, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public User() {

    }

    public String getUsername() {
        return username;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
}
