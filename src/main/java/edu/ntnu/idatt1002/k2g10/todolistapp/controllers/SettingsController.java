package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.todolistapp.App;
import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.Theme;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for the settings window.
 *
 * @author hasanro
 */
public class SettingsController {
    @FXML
    public JFXTextField emailTextField;
    @FXML
    public JFXTextField userNameTextField;
    @FXML
    public JFXPasswordField setPasswordTextField;
    @FXML
    public JFXPasswordField verifyPassword;
    @FXML
    public JFXButton cancelButton;
    @FXML
    private JFXComboBox<String> themePicker;

    /**
     * Runs when the view is loaded.
     */
    @FXML
    public void initialize() {
        emailTextField.setText(Session.getActiveUser().getEmail());
        userNameTextField.setText(Session.getActiveUser().getUsername());
        for (Theme theme : Theme.values()) {
            themePicker.getItems().add(theme.getDisplayName());
            themePicker.getSelectionModel().select(Session.getTheme().getDisplayName());
        }
    }

    /**
     * Updates the theme based on the users choice.
     */
    @FXML
    public void changeTheme() {
        Scene scene = themePicker.getScene();

        Theme newTheme = Theme.get(themePicker.getSelectionModel().getSelectedItem());
        Session.setTheme(newTheme);

        scene.getStylesheets().clear();
        String path = String.format("css/%s.css", Session.getTheme().getFileName());
        scene.getStylesheets().add(App.class.getResource(path).toString());

        themePicker.getSelectionModel().select(newTheme.getDisplayName());
    }

    /**
     * Saves changes to user if all data is valid.
     */
    @FXML
    public void applyChanges() {
        if (Session.getActiveUser().verifyPassword(verifyPassword.getText())) {
            if (!emailTextField.getText().matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$")) {
                String content = "The email provided is not valid.";
                DialogFactory.getOKDialog("Save failed", content).show();
                return;
            }

            if (!userNameTextField.getText().matches("([0-9A-Za-zæøåÆØÅ\\-_.])+")) {
                String content = "The username contains invalid characters.";
                DialogFactory.getOKDialog("Save failed", content).show();
                return;
            }

            if (!setPasswordTextField.getText().trim().isBlank()) {
                Session.getActiveUser().setPassword(setPasswordTextField.getText());
            }

            Session.getActiveUser().setEmail(emailTextField.getText());
            Session.getActiveUser().setUsername(userNameTextField.getText());
        } else {
            DialogFactory.getOKDialog("Save failed", "Password was incorrect.").show();
        }
    }

    /**
     * Closes the popup window.
     */
    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
