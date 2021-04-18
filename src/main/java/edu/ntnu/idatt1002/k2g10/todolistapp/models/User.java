package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import java.io.Serializable;

/**
 * User class. Contains {@link TaskList}
 * 
 * @author hasanro, Bragemi, trthingnes
 */
public class User implements Serializable {
    private final String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private final TaskList taskList;

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
     * @param phone
     *            phone number of the user
     */
    public User(String username, String firstname, String lastname, String email, String phone) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.taskList = new TaskList();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TaskList getTaskList() {
        return taskList;
    }
}
