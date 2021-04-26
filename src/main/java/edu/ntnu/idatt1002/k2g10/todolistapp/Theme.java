package edu.ntnu.idatt1002.k2g10.todolistapp;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum containing application themes.
 */
public enum Theme {
    LIGHT("Light theme", "light-theme"), DARK("Dark theme", "dark-theme"), CONTRAST("Contrast theme", "contrast-theme"),
    PASTEL("Pastel theme", "pastel-theme");

    private final String displayName;
    private final String fileName;

    /**
     * Constructs a new theme instance.
     * 
     * @param displayName
     *            Theme display name.
     * @param fileName
     *            Theme CSS file name.
     */
    Theme(String displayName, String fileName) {
        this.displayName = displayName;
        this.fileName = fileName;
    }

    /**
     * Gets the theme with the given display name.
     * 
     * @param displayName
     *            Theme display name.
     * 
     * @return Theme.
     */
    public static Theme get(String displayName) {
        Optional<Theme> theme = Arrays.stream(Theme.values()).filter(t -> t.displayName.equals(displayName))
                .findFirst();
        return theme.orElse(Theme.LIGHT);
    }

    /**
     * Returns theme file name.
     * 
     * @return File name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns theme display name.
     * 
     * @return Display name.
     */
    public String getDisplayName() {
        return displayName;
    }
}
