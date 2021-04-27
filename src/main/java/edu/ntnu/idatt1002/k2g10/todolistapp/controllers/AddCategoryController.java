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

import java.awt.*;
import java.sql.SQLException;

/**
 * Controller for the add category window.
 *
 * @author andetel
 */
public class AddCategoryController {
    @FXML
    private JFXComboBox<Label> iconPicker;
    @FXML
    private JFXTextField titleField;

    /**
     * Initializes the view on load.
     */
    @FXML
    public void initialize() {
        for (FontAwesomeIcon icon : FontAwesomeIcon.values()) {
            Label label = new Label(icon.toString());
            iconPicker.getItems().add(label);
        }
    }

    /**
     * Submits the new category and closes the window.
     */
    public void onSubmit() {
        // Get current stage from a field
        Stage stage = (Stage) titleField.getScene().getWindow();

        String categoryTitle = titleField.getText();
        char categoryIcon = iconPicker.getSelectionModel().getSelectedItem().getText().charAt(0);

        try {
            Category newCategory = new Category(categoryTitle, categoryIcon);
            Session.getActiveUser().getTaskList().getCategories().add(newCategory);
            Session.save(); // Saves user data.
            stage.close();
        } catch (SQLException e) {
            String content = String.format("Unable to add category.%nError message: '%s'", e.getMessage());
            Session.getLogger().warning(content);
            DialogFactory.getOKDialog("Category add failed", content).show();
        }
    }
}
