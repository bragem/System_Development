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
    @FXML
    private JFXComboBox colorPicker;

    /**
     * Initializes the view on load.
     */
    @FXML
    public void initialize() {
        for (FontAwesomeIcon icon : FontAwesomeIcon.values()) {
            Label label = new Label(icon.toString());
            iconPicker.getItems().add(label);
        }
        fillColors();
    }

    /**
     * Sets the background color of the colorPicker to the chosen color.
     */
    @FXML
    public void showChosenColor() {
        Label label = (Label) colorPicker.getSelectionModel().getSelectedItem();
        String style = label.getStyle();
        String color = style.substring(22, style.length() - 1);

        colorPicker.setStyle("-fx-background-color: " + color + ";");
    }

    /**
     * Submits the new category and closes the window.
     */
    @FXML
    public void onSubmit() {
        // Get current stage from a field
        Stage stage = (Stage) titleField.getScene().getWindow();

        String categoryTitle = titleField.getText();
        char categoryIcon = iconPicker.getSelectionModel().getSelectedItem().getText().charAt(0);
        String categoryColor = colorPicker.getStyle().substring(22, colorPicker.getStyle().length() - 1);

        try {
            Category newCategory = new Category(categoryTitle, categoryIcon, categoryColor);
            Session.getActiveUser().getTaskList().addCategory(newCategory);
            Session.save(); // Saves user data.
            stage.close();
        } catch (SQLException e) {
            String content = String.format("Unable to add category.%nError message: '%s'", e.getMessage());
            Session.getLogger().warning(content);
            DialogFactory.getOKDialog("Category add failed", content).show();
        }
    }

    /**
     * Fills the color picker with desired colors.
     */
    public void fillColors() {
        String[] colors = { "#33abae", "#f79f3f", "#ff6c47", "#bd4583", "#5d5ca2" };

        colorPicker.getItems().clear();
        for (String cl : colors) {
            Label label = new Label();
            label.setStyle("-fx-background-color: " + cl + "!important;");
            label.setPrefHeight(30);
            label.setPrefWidth(100);
            colorPicker.getItems().add(label);
        }
    }
}
