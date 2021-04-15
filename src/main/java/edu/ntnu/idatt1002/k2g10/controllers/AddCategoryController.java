package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.models.Category;
import edu.ntnu.idatt1002.k2g10.utils.crypto.EncryptionException;
import edu.ntnu.idatt1002.k2g10.utils.icons.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author andetel
 */
public class AddCategoryController {
    @FXML
    private JFXComboBox<Label> iconPicker;
    @FXML
    private JFXTextField titleField;

    public void initialize() {
        for (FontAwesomeIcon icon : FontAwesomeIcon.values()) {
            Label label = new Label(icon.toString());
            iconPicker.getItems().add(label);
        }
    }

    public void onSubmit() {
        // Get current stage from a field
        Stage stage = (Stage) titleField.getScene().getWindow();

        String categoryTitle = titleField.getText();
        char categoryIcon = iconPicker.getSelectionModel().getSelectedItem().getText().charAt(0);

        try {
            Category newCategory = new Category(categoryTitle, categoryIcon);
            Session.getActiveUser().getTaskList().addCategory(newCategory);
            Session.save(); // Saves user to file.
            stage.close();
        } catch (IOException e) {
            //TODO: Exception handling.
            e.printStackTrace();
        } catch (EncryptionException e) {
            //TODO: Exception handling.
            e.printStackTrace();
        }
    }
}
