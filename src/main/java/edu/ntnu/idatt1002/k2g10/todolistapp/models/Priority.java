package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import java.io.Serializable;
import java.util.Locale;

/**
 * Priority is associated with {@link Task}.
 * 
 * @author hasanro, trthingnes, bragemi
 */
public enum Priority implements Serializable {
    NONE, LOW, MEDIUM, HIGH;

    /**
     * Get string version of priority.
     * 
     * @return String name.
     */
    @Override
    public String toString() {
        String first = name().substring(0, 1);
        String rest = name().substring(1);
        return first.toUpperCase(Locale.ROOT) + rest.toLowerCase(Locale.ROOT);
    }
}