package edu.ntnu.idatt1002.k2g10.factories;

import edu.ntnu.idatt1002.k2g10.App;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Generates {@link FXMLLoader} objects.
 * 
 * @author trthingnes
 */
public class FXMLLoaderFactory {
    /**
     * Gets an FXML loader for the given FXML file.
     *
     * @param name
     *            Name of the FXML file in the FXML folder.
     * 
     * @return FXML loader
     * 
     * @throws IOException
     *             If file read fails.
     */
    public static FXMLLoader getFXMLLoader(String name) throws IOException {
        String path = String.format("/fxml/%s.fxml", name);
        return new FXMLLoader(App.class.getResource(path));
    }
}
