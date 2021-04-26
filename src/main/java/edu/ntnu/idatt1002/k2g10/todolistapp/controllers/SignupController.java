package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.Theme;
import edu.ntnu.idatt1002.k2g10.todolistapp.daos.UserDAO;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.User;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Controller for the signup window.
 *
 * @author chrisoss, trthingnes
 */
public class SignupController {
    @FXML
    private JFXTextField firstnameField;
    @FXML
    private JFXTextField lastnameField;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXPasswordField confirmPasswordField;
    @FXML
    private JFXComboBox<String> themePicker;

    /**
     * Runs when the view is loaded.
     */
    @FXML
    public void initialize() {
        // Fill theme picker.
        for (Theme theme : Theme.values()) {
            themePicker.getItems().add(theme.getDisplayName());
            themePicker.getSelectionModel().select(Session.getTheme().getDisplayName());
        }
    }

    /**
     * Attempt to register a new user and log them in.
     */
    @FXML
    public void signup() {
        // Input retrieval
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Input validation
        try {
            if (firstname.isBlank() || lastname.isBlank()) {
                throw new IllegalArgumentException("First or last name was blank.");
            }

            if (!Objects.equals(password, confirmPasswordField.getText())) {
                throw new IllegalArgumentException("Password was not repeated correctly.");
            }

            if (!email.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$")) {
                throw new IllegalArgumentException("The email provided is not valid.");
            }

            if (!username.matches("([0-9A-Za-zæøåÆØÅ\\-_.])+")) {
                throw new IllegalArgumentException("The username contains invalid characters.");
            }
        } catch (IllegalArgumentException e) {
            Session.getLogger().info("Registration failed: " + e.getMessage());
            DialogFactory.getOKDialog("Registration failed", e.getMessage()).show();
            return;
        }

        // Registration process
        User newUser = new User(username, firstname, lastname, email, password, Session.getTheme());
        try {
            UserDAO userDAO = new UserDAO(Session.getEntityManager());
            userDAO.create(newUser);
        } catch (SQLException e) {
            Session.getLogger().severe("Registration failed: " + e.getMessage());
            DialogFactory.getOKDialog("Registration failed", e.getMessage()).show();
            return;
        }

        // Login process
        Session.setActiveUser(newUser);
        try {
            Session.setLocation("taskview");
        } catch (IOException e) {
            String content = "The account was registered, but the redirect failed.";
            Session.getLogger().warning(content);
            DialogFactory.getOKDialog("Registration successful", content).show();
        }
    }

    /**
     * Go to the login page.
     * 
     * @throws IOException
     *             If the login page fails to load.
     */
    @FXML
    public void login() throws IOException {
        Session.setLocation("login");
    }

    /**
     * Updates the theme based on the users choice.
     */
    @FXML
    public void changeTheme() {
        Theme newTheme = Theme.get(themePicker.getSelectionModel().getSelectedItem());
        Session.setTheme(newTheme);
        themePicker.getSelectionModel().select(newTheme.getDisplayName());
    }
}