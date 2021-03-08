package edu.ntnu.idatt1002.k2g10;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  private static Scene scene;

  /**
   * Loads an FXML file from resources folder.
   *
   * @param name Name of FXML file
   * @return JavaFX {@link Parent} object
   * @throws IOException If file cannot be loaded
   * @author trthingnes
   */
  private static Parent loadFXML(String name) throws IOException {
    String path = String.format("/%s/fxml/%s.fxml", App.class.getPackageName(), name);
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(path));
    return fxmlLoader.load();
  }

  /**
   * Starts the application.
   *
   * @param stage Stage sent by JavaFX
   * @throws Exception If there is a problem starting the application
   * @author trthingnes
   */
  @Override
  public void start(Stage stage) throws Exception {
    scene = new Scene(loadFXML("login"));

    // Set the theme given in the string.
    String theme = "dark";
    scene
        .getStylesheets()
        .add(String.format("/%s/css/%s-theme.css", App.class.getPackageName(), theme));

    stage.setMaximized(true);
    stage.setScene(scene);
    stage.setTitle("Todo-list application");
    stage.setMinWidth(1200);
    stage.setMinHeight(800);
    stage.show();
  }
}
