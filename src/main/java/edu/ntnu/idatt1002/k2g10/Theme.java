package edu.ntnu.idatt1002.k2g10;

import java.util.Arrays;
import java.util.Optional;

public enum Theme {
    LIGHT("Light theme", "light-theme"), DARK("Dark theme", "dark-theme"),
    CONTRAST("High contrast theme", "contrast-theme");

    private final String displayName;
    private final String fileName;

    Theme(String displayName, String fileName) {
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public static Theme get(String displayName) {
        Optional<Theme> theme = Arrays.stream(Theme.values()).filter(t -> t.displayName.equals(displayName))
                .findFirst();
        return theme.orElse(Theme.LIGHT);
    }

    public String getFileName() {
        return fileName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
