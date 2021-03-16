package edu.ntnu.idatt1002.k2g10;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Logger logger;
    private static Scene scene;
    private static String theme = "light";

    /**
     * Get an application wide logger.
     *
     * Will create a new logger if one does not already exist. If one has already been made this will be returned. The
     * returned logger will log to a file named {@code [current date and time].log} and log all events with source
     * method, level and message.
     *
     * @return Application wide logger
     * 
     * @author trthingnes
     */
    public static Logger getLogger() {
        // If a logger has not been created, do that before returning.
        if (Objects.isNull(logger)) {
            try {
                // Get logger and set level (= what level of log elements to include in the log)
                logger = Logger.getLogger(App.class.getName());
                logger.setLevel(Level.ALL);

                // Get log file for handler
                String filename = LocalDateTime.now().toString().replace(":", "-");
                FileHandler handler = new FileHandler(String.format("src/main/resources/logs/%s.log", filename));

                // Implement custom formatter on handler.
                handler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return String.format("[%s::%s] %s: %s\n",
                                record.getSourceClassName().replace("edu.ntnu.idatt1002.k2g10.", ""),
                                record.getSourceMethodName(), record.getLevel(), record.getMessage());
                    }
                });

                // Add handler to logger
                logger.addHandler(handler);
            } catch (IOException e) {
                System.err.println("Could not initiate logger.");
            }
        }

        return logger;
    }

    /**
     * Loads an FXML file from resources folder.
     *
     * @param name
     *            Name of FXML file
     * 
     * @return JavaFX {@link Parent} object
     * 
     * @throws IOException
     *             If file cannot be loaded
     * 
     * @author trthingnes
     */
    private static Parent loadFXML(String name) throws IOException {
        String path = String.format("/%s/fxml/%s.fxml", App.class.getPackageName(), name);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(path));
        return fxmlLoader.load();
    }

    /**
     * Starts the JavaFX application.
     *
     * @param stage
     *            Stage sent by JavaFX
     * 
     * @throws Exception
     *             If there is a problem starting the application
     * 
     * @author trthingnes
     */
    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("login"));

        scene.getStylesheets().add(String.format("/%s/css/%s-theme.css", App.class.getPackageName(), theme));
        getLogger().info(String.format("Loaded %s theme", theme));

        // stage.setMaximized(true); This feels unnecessary.
        stage.setScene(scene);
        stage.setTitle("Todo-list application");
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
        stage.show();
    }
}
