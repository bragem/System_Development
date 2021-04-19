package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.Theme;
import edu.ntnu.idatt1002.k2g10.todolistapp.daos.UserDAO;
import edu.ntnu.idatt1002.k2g10.todolistapp.exceptions.DuplicateTaskException;
import edu.ntnu.idatt1002.k2g10.todolistapp.exceptions.IncorrectPasswordException;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.*;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.crypto.PasswordHashAlgorithm;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.icons.FontAwesomeIcon;
import javafx.fxml.FXML;

import javax.persistence.EntityNotFoundException;

/**
 * Controller for the Login view.
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
     * Runs when the view is loaded.
     */
    @FXML
    public void initialize() {
        for (Theme theme : Theme.values()) {
            themePicker.getItems().add(theme.getDisplayName());
            themePicker.getSelectionModel().select(Session.getTheme().getDisplayName());
        }
    }

    /**
     * Event handler for login events.
     */
    @FXML
    public void login() {
        // TODO - Debug code start - !Remove this before production!
        // Allow login without username and password.
        if (usernameField.getText().isBlank() && passwordField.getText().isBlank()) {
            try {
                User johndoe = new User("johndoe", "John", "Doe", "johdoe@stud.ntnu.no", "password");

                Category school = new Category("School", FontAwesomeIcon.BOOK.getChar());
                Category work = new Category("Work", FontAwesomeIcon.BRIEFCASE.getChar());
                Category shopping = new Category("Shopping", FontAwesomeIcon.SHOPPING_CART.getChar());
                Category freetime = new Category("Free time", FontAwesomeIcon.USER.getChar());
                johndoe.getTaskList().addCategory(school);
                johndoe.getTaskList().addCategory(work);
                johndoe.getTaskList().addCategory(shopping);
                johndoe.getTaskList().addCategory(freetime);

                TaskList list = johndoe.getTaskList();
                list.addTask(
                        new Task("Title A", "Desc", LocalDate.of(2021, 1, 1), LocalDate.now(), Priority.HIGH, school));
                list.addTask(
                        new Task("Title B", "Desc", LocalDate.of(2021, 2, 1), LocalDate.now(), Priority.LOW, work));
                list.addTask(new Task("Title C", "Desc", LocalDate.of(2021, 3, 1), LocalDate.now(), Priority.NONE,
                        shopping));
                list.addTask(new Task("Title D", "Desc", LocalDate.of(2021, 4, 1), LocalDate.now(), Priority.MEDIUM,
                        freetime));

                Session.setActiveUser(johndoe);
                Session.setLocation("taskview");
            } catch (IOException | DuplicateTaskException e) {
                e.printStackTrace();
            }

            return;
        }
        // TODO - Debug code end

        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            UserDAO userDAO = new UserDAO(Session.getEntityManager());
            Optional<User> optionalUser = userDAO.get(username);
            if(optionalUser.isEmpty()) {
                throw new EntityNotFoundException("User does not exist.");
            }
            User user = optionalUser.get();
            if(!PasswordHashAlgorithm.PBKDF2.verifyHash(password, user.getPasswordSalt(), user.getPasswordHash())) {
                throw new IncorrectPasswordException("Incorrect password for user.");
            }

            Session.setActiveUser(user);
            Session.getLogger().info("Logged in as user " + user.getUsername());

            Session.setLocation("taskview");
        } catch (EntityNotFoundException | IncorrectPasswordException e) {
            Session.getLogger().warning(e.getMessage());
            DialogFactory.getOKDialog("Login failed", "Incorrect username or password.").show();
        } catch (SQLException | IOException e) {
            Session.getLogger().severe(e.getMessage());
            DialogFactory.getOKDialog("Login failed",
                    "Unable to log in because of an internal error. (" + e.getMessage() + ")").show();
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
