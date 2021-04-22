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
    @FXML
    private JFXComboBox colorPicker;

    public void initialize() {
        for (FontAwesomeIcon icon : FontAwesomeIcon.values()) {
            Label label = new Label(icon.toString());
            iconPicker.getItems().add(label);
        }
        fillColors();
    }

    /**
     * Fills the colorPicker with desired colors
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

    /**
     * Sets the background color of the colorPicker to the chosen color
     */
    public void showChosenColor() {
        Label label = (Label) colorPicker.getSelectionModel().getSelectedItem();
        String style = label.getStyle();
        String color = style.substring(22, style.length() - 1);

        colorPicker.setStyle("-fx-background-color: " + color + ";");
    }

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
            DialogFactory.getOKDialog("Category add failed", "Unable to add category.\n(" + e.getMessage() + ")")
                    .show();
        }
    }
}
