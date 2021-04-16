package edu.ntnu.idatt1002.k2g10.factories;

import edu.ntnu.idatt1002.k2g10.App;
import edu.ntnu.idatt1002.k2g10.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PopupWindowFactory {
    public static Stage getPopupWindow(String name) throws IOException {
        Stage popupWindow = new Stage();
        FXMLLoader loader = FXMLLoaderFactory.getFXMLLoader(name);

        popupWindow.setScene(new Scene(loader.load()));
        popupWindow.getScene().getStylesheets().add(String.format("/css/%s.css", Session.getTheme().getFileName()));
        popupWindow.getIcons().add(new Image(App.class.getResource("/img/icon.png").toString()));
        popupWindow.initModality(Modality.APPLICATION_MODAL);

        return popupWindow;
    }
}
