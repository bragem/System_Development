package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.models.Category;
import edu.ntnu.idatt1002.k2g10.utils.crypto.EncryptionException;
import edu.ntnu.idatt1002.k2g10.utils.icons.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author andetel
 */
public class AddCategory {
    @FXML
    private JFXComboBox<Label> iconPicker;
    @FXML
    private JFXTextField title;
    @FXML
    private JFXComboBox colorPicker;

    public void initialize() {
        for (FontAwesomeIcon icon : FontAwesomeIcon.values()) {
            Label label = new Label(icon.toString());
            iconPicker.getItems().add(label);
        }
    }

    public void showChosenColor() {
        Label label = (Label) colorPicker.getSelectionModel().getSelectedItem();
        String style = label.getStyle();
        String color = style.substring(22, style.length() - 1);

        colorPicker.setStyle("-fx-background-color: " + color + ";");
    }

    public void addCategory() throws IOException, EncryptionException {
        String categoryTitle = title.getText();
        String categoryColor = colorPicker.getStyle().substring(22, colorPicker.getStyle().length() - 1);

        char categoryIcon = iconPicker.getSelectionModel().getSelectedItem().getText().charAt(0);

        Session.getActiveUser().getTaskList().addCategory(new Category(categoryTitle, categoryIcon, categoryColor));

        Stage popup = (Stage) Session.getPopup().getWindow();
        ((TaskListController) popup.getUserData()).update();

        Session.save(); // Saves user to file.
        popup.close();
    }
}
