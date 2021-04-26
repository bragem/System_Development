package edu.ntnu.idatt1002.k2g10.todolistapp.factories;

import edu.ntnu.idatt1002.k2g10.todolistapp.App;
import javafx.fxml.FXMLLoader;

/**
 * Factory for {@link FXMLLoader} objects.
 * 
 * @author trthingnes
 */
public class FXMLLoaderFactory {
    private FXMLLoaderFactory() {
    }

    /**
     * Gets an FXML loader for the given FXML file.
     *
     * @param name
     *            Name of the FXML file in the FXML folder.
     * 
     * @return FXML loader.
     */
    public static FXMLLoader getFXMLLoader(String name) {
        String path = String.format("fxml/%s.fxml", name);
        return new FXMLLoader(App.class.getResource(path));
    }
}
