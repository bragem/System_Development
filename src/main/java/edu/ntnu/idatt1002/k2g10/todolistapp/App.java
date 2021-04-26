package edu.ntnu.idatt1002.k2g10.todolistapp;

import edu.ntnu.idatt1002.k2g10.todolistapp.factories.FXMLLoaderFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Application entry point.
 *
 * @author K2G10
 */
public class App extends Application {
    private static final String WINDOW_TITLE = "Todo list application";
    private static final int WINDOW_MIN_WIDTH = 1200;
    private static final int WINDOW_MIN_HEIGHT = 800;

    /**
     * Starts the JavaFX application.
     *
     * @param stage
     *            Stage sent by JavaFX.
     * 
     * @throws Exception
     *             If there is a problem starting the application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("font/fontawesome.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("font/BebasNeue-Regular.ttf"), 12);

        FXMLLoader loader = FXMLLoaderFactory.getFXMLLoader("login");
        Session.setScene(new Scene(loader.load()));
        Session.setTheme(Theme.LIGHT);
        stage.setScene(Session.getScene());

        // Add settings
        stage.setTitle(WINDOW_TITLE);
        stage.getIcons().add(new Image(App.class.getResource("img/icon.png").toString()));
        stage.show();
        stage.setMinWidth(WINDOW_MIN_WIDTH);
        stage.setMinHeight(WINDOW_MIN_HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
