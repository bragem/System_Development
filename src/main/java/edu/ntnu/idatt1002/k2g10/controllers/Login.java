package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.time.LocalDateTime;

import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.Theme;
import edu.ntnu.idatt1002.k2g10.exceptions.DuplicateTaskException;
import edu.ntnu.idatt1002.k2g10.factory.DialogFactory;
import edu.ntnu.idatt1002.k2g10.models.*;
import edu.ntnu.idatt1002.k2g10.repositories.UserRepository;
import edu.ntnu.idatt1002.k2g10.utils.crypto.EncryptionException;
import edu.ntnu.idatt1002.k2g10.utils.crypto.IncorrectPasswordException;
import edu.ntnu.idatt1002.k2g10.utils.icons.FontAwesomeIcon;
import javafx.fxml.FXML;

/**
 * Controller for the Login view.
 *
 * @author trthingnes
 */
public class Login {
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
                User johndoe = new User("johndoe", "John", "Doe", "johdoe@stud.ntnu.no", "12345678");

                Category school = new Category("School", FontAwesomeIcon.BOOK.getChar());
                Category work = new Category("Work", FontAwesomeIcon.BRIEFCASE.getChar());
                Category shopping = new Category("Shopping", FontAwesomeIcon.SHOPPING_CART.getChar());
                Category freetime = new Category("Free time", FontAwesomeIcon.USER.getChar());
                johndoe.getTaskList().addCategory(school);
                johndoe.getTaskList().addCategory(work);
                johndoe.getTaskList().addCategory(shopping);
                johndoe.getTaskList().addCategory(freetime);

                TaskList list = johndoe.getTaskList();
                list.addTask(new Task("Title A", "Desc", LocalDateTime.of(2021, 1, 1, 0, 0), LocalDateTime.now(),
                        Priority.HIGH, school));
                list.addTask(new Task("Title B", "Desc", LocalDateTime.of(2021, 2, 1, 0, 0), LocalDateTime.now(),
                        Priority.LOW, work));
                list.addTask(new Task("Title C", "Desc", LocalDateTime.of(2021, 3, 1, 0, 0), LocalDateTime.now(),
                        Priority.NONE, shopping));
                list.addTask(new Task("Title D", "Desc", LocalDateTime.of(2021, 4, 1, 0, 0), LocalDateTime.now(),
                        Priority.MEDIUM, freetime));

                Session.setActiveUser(johndoe);
                Session.setActivePassword("password");
                Session.setLocation("overview");
            } catch (IOException | DuplicateTaskException e) {
                e.printStackTrace();
            }

            return;
        }
        // TODO - Debug code end

        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = UserRepository.load(username, password);
            Session.setActiveUser(user);
            Session.setActivePassword(password);
            Session.getLogger().info("Logged in as user " + user.getUsername());

            Session.setLocation("upcoming");
        } catch (EncryptionException | IOException e) {
            Session.getLogger().severe(e.getMessage());
            DialogFactory.getOKDialog("Login failed", "Unable to log in because of an internal error. (" + e.getMessage() + ")").show();
        } catch (IncorrectPasswordException e) {
            Session.getLogger().warning(e.getMessage());
            DialogFactory.getOKDialog("Login failed", "Incorrect password for the given user.").show();
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
