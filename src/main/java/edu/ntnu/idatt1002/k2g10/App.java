package edu.ntnu.idatt1002.k2g10;

import edu.ntnu.idatt1002.k2g10.models.*;
import edu.ntnu.idatt1002.k2g10.utils.files.FXMLFile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class App extends Application {
    private static final String WINDOW_TITLE = "Todo list application";
    private static final int WINDOW_MIN_WIDTH = 1200;
    private static final int WINDOW_MIN_HEIGHT = 800;

    /**
     * Starts the JavaFX application.
     *
     * @param stage
     *            Stage sent by JavaFX
     * 
     * @throws Exception
     *             If there is a problem starting the application
     */
    @Override
    public void start(Stage stage) throws Exception {
        Session.setScene(new Scene(FXMLFile.load("login")));
        Session.setTheme(Theme.LIGHT);
        stage.setScene(Session.getScene());

        // Add settings
        stage.setTitle(WINDOW_TITLE);
        stage.setMinWidth(WINDOW_MIN_WIDTH);
        stage.setMinHeight(WINDOW_MIN_HEIGHT);
        stage.getIcons().add(new Image(App.class.getResource("/img/icon.png").toString()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
