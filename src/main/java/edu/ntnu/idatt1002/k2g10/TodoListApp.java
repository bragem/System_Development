package edu.ntnu.idatt1002.k2g10;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TodoListApp extends Application {
  @Override
  public void start(Stage primaryStage) {
    try {
      Text messageText = new Text("If you can read this, JavaFX is properly set up! Good job!");

      StackPane root = new StackPane();
      root.getChildren().add(messageText);

      Scene scene = new Scene(root, 400, 400);
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
