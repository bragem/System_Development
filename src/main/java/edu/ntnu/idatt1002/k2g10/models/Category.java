package edu.ntnu.idatt1002.k2g10.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Category is contained in {@link TaskList} and is associated with {@link Task}.
 * 
 * @author hasanro, trthingnes, bragemi
 */
public class Category implements Serializable {
    private String title;
    private String color;
    private char icon;

    /**
     * Construct a new category instance.
     * 
     * @param title
     *            Category title to display
     * @param icon
     *            Category symbol/emoji to display
     */
    public Category(String title, char icon, String color) {
        this.title = title;
        this.color = color;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public char getIcon() {
        return icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIcon(char icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Category category = (Category) o;
        return Objects.equals(title, category.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, icon);
    }
}
