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
    private ColorPicker colorPicker;

    public void initialize() {
        for (FontAwesomeIcon icon : FontAwesomeIcon.values()) {
            Label label = new Label(icon.toString());
            iconPicker.getItems().add(label);
        }
    }

    public void addCategory() throws IOException, EncryptionException {
        String categoryTitle = title.getText();
        String categoryColor = colorPicker.getValue().toString();
        char categoryIcon = iconPicker.getSelectionModel().getSelectedItem().getText().charAt(0);

        Session.getActiveUser().getTaskList().addCategory(new Category(categoryTitle, categoryIcon, categoryColor));

        Stage popup = (Stage) Session.getPopup().getWindow();
        ((TaskListController) popup.getUserData()).update();

        Session.save(); // Saves user to file.
        popup.close();
    }
}
