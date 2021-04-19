package edu.ntnu.idatt1002.k2g10.todolistapp.factories;

import edu.ntnu.idatt1002.k2g10.todolistapp.App;
import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Generates popup window stages.
 * 
 * @author trthingnes
 */
public class PopupWindowFactory {
    /**
     * Gets a popup window with the given fxml name.
     * 
     * @param fxml
     *            FXML file to load into window.
     * 
     * @return Popup window stage.
     * 
     * @throws IOException
     *             If FXML file fails to load.
     */
    public static Stage getPopupWindow(String fxml) throws IOException {
        Stage popupWindow = new Stage();
        FXMLLoader loader = FXMLLoaderFactory.getFXMLLoader(fxml);

        popupWindow.setScene(new Scene(loader.load()));
        String path = String.format("css/%s.css", Session.getTheme().getFileName());
        popupWindow.getScene().getStylesheets().add(App.class.getResource(path).toString());
        popupWindow.getIcons().add(new Image(App.class.getResource("img/icon.png").toString()));
        popupWindow.initModality(Modality.APPLICATION_MODAL);

        return popupWindow;
    }
}
