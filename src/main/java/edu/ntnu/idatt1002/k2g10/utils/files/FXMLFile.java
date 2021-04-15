package edu.ntnu.idatt1002.k2g10.utils.files;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FXMLFile {
    /**
     * Loads an FXML file from the resources folder.
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
    public static Parent load(String name) throws IOException {
        String path = String.format("/fxml/%s.fxml", name);
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLFile.class.getResource(path));
        return fxmlLoader.load();
    }

    /**
     * Loads an FXML file from the resources folder.
     *
     * @param name
     *            Name of FXML file.
     *
     * @param controller
     *            Controller class to assign to FXML file.
     *
     * @return JavaFX {@link Parent} object.
     *
     * @throws IOException
     *             If file cannot be loaded.
     *
     * @author trthingnes
     */
    public static Parent load(String name, Object controller) throws IOException {
        String path = String.format("/fxml/%s.fxml", name);
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLFile.class.getResource(path));
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }
}
