package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Category is contained in {@link TaskList} and is associated with {@link Task}.
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
    public Category(String title, char icon) {
        this.title = title;
        this.icon = icon;
    }

    public Category() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public char getIcon() {
        return icon;
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
