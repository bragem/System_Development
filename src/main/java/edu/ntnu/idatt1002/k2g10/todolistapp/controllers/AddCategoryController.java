package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Category;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.icons.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

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
            Session.save(); // Saves user data.
            stage.close();
        } catch (SQLException e) {
            DialogFactory.getOKDialog("Category add failed", "Unable to add category.\n(" + e.getMessage() + ")")
                    .show();
        }
    }
}
