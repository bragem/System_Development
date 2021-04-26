package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.Theme;
import edu.ntnu.idatt1002.k2g10.todolistapp.daos.UserDAO;
import edu.ntnu.idatt1002.k2g10.todolistapp.exceptions.IncorrectPasswordException;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.*;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.crypto.PasswordHashAlgorithm;
import javafx.fxml.FXML;

import javax.persistence.EntityNotFoundException;

/**
 * Controller for the login window.
 *
 * @author trthingnes
 */
public class LoginController {
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXComboBox<String> themePicker;

    /**
     * Initializes the view on load.
     */
    @FXML
    public void initialize() {
        for (Theme theme : Theme.values()) {
            themePicker.getItems().add(theme.getDisplayName());
            themePicker.getSelectionModel().select(Session.getTheme().getDisplayName());
        }
    }

    /**
     * Attempts to log the user in.
     */
    @FXML
    public void login() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            UserDAO userDAO = new UserDAO(Session.getEntityManager());
            Optional<User> optionalUser = userDAO.get(username);
            if (optionalUser.isEmpty()) {
                throw new EntityNotFoundException("User does not exist.");
            }
            User user = optionalUser.get();
            if (!PasswordHashAlgorithm.PBKDF2.verifyHash(password, user.getPasswordSalt(), user.getPasswordHash())) {
                throw new IncorrectPasswordException("Incorrect password for user.");
            }

            Session.setTheme(user.getTheme());
            Session.setActiveUser(user);
            Session.getLogger().info("Logged in as user " + user.getUsername());

            Session.setLocation("taskview");
        } catch (EntityNotFoundException | IncorrectPasswordException e) {
            Session.getLogger().info(String.format("Login failed with the message '%s'.", e.getMessage()));
            DialogFactory.getOKDialog("Login failed", "Incorrect username or password.").show();
        } catch (SQLException | IOException e) {
            String content = String.format("An internal error occured while logging in.%nError message: '%s'",
                    e.getMessage());
            Session.getLogger().severe(content);
            DialogFactory.getOKDialog("Login failed", content).show();
        }

    }

    /**
     * Go to the signup page.
     * 
     * @throws IOException
     *             If signup page fails to load.
     */
    @FXML
    public void signup() throws IOException {
        Session.setLocation("signup");
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
