package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entity contained in {@link TaskList} and associated with {@link Task}.
 * 
 * @author hasanro, trthingnes, bragemi
 */
@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    private String title;
    private String color;

    @NotBlank
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

    /**
     * Construct a new category instance.
     */
    public Category() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public String getTitle() {
        return title;
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

    @Override
    public String toString() {
        return String.format("%s %s", icon, title);
    }
}
