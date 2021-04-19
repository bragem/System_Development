package edu.ntnu.idatt1002.k2g10.todolistapp;

import edu.ntnu.idatt1002.k2g10.todolistapp.models.AppUser;
import edu.ntnu.idatt1002.k2g10.todolistapp.daos.UserFileDAO;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.crypto.EncryptionException;
import edu.ntnu.idatt1002.k2g10.todolistapp.utils.files.FXMLFile;
import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.*;

public class Session {
    private Session() {
    }

    private static Logger logger;
    private static Scene scene;
    private static Theme theme = Theme.LIGHT;
    private static AppUser activeUser;
    private static String activePassword;

    public static void save() throws IOException, EncryptionException {
        UserFileDAO.save(activeUser, activePassword);
    }

    public static void setLocation(String fxml) throws IOException {
        scene.setRoot(FXMLFile.load(fxml));
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        Session.scene = scene;
    }

    public static Theme getTheme() {
        return theme;
    }

    public static void setTheme(Theme theme) {
        // Remove old CSS.
        Session.scene.getStylesheets().clear();

        // Change theme.
        Session.theme = theme;

        // Add new CSS.
        String path = String.format("css/%s.css", Session.getTheme().getFileName());
        Session.scene.getStylesheets().add(App.class.getResource(path).toString());

        // Log the change.
        Session.getLogger().info("Loaded theme: " + theme.getDisplayName());
    }

    public static AppUser getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(AppUser activeUser) {
        Session.activeUser = activeUser;
    }

    public static String getActivePassword() {
        return activePassword;
    }

    public static void setActivePassword(String activePassword) {
        Session.activePassword = activePassword;
    }

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
                // Attempt to make logs directory
                File logsFolder = new File("src/main/resources/logs/");
                logsFolder.mkdir();

                // Get logger and set level (= what level of log elements to include in the log)
                logger = Logger.getLogger(App.class.getName());
                logger.setLevel(Level.ALL);

                // Get log file for handler
                String filename = LocalDateTime.now().toString().replace(":", "-");
                FileHandler handler = new FileHandler(String.format("%s/%s.log", logsFolder.getPath(), filename));

                // Implement custom formatter on handler.
                handler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return String.format("[%s::%s] %s: %s%n",
                                record.getSourceClassName().replace("edu.ntnu.idatt1002.k2g10.todolistapp", ""),
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
}
