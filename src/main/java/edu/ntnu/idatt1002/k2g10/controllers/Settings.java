package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.Theme;
import javafx.fxml.FXML;

/**
 * @author hasanro
 */
public class Settings {
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
     * Updates the theme based on the users choice.
     */
    @FXML
    public void changeTheme() {
        Theme newTheme = Theme.get(themePicker.getSelectionModel().getSelectedItem());
        Session.setTheme(newTheme);
        themePicker.getSelectionModel().select(newTheme.getDisplayName());
    }
}
