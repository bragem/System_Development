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
    private Character symbol;

    /**
     * Construct a new category instance.
     * 
     * @param title
     *            Category title to display
     * @param symbol
     *            Category symbol/emoji to display
     */
    public Category(String title, Character symbol) {
        this.title = title;
        this.symbol = symbol;
    }

    public String getTitle() {
        return title;
    }

    public Character getSymbol() {
        return symbol;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
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
        return Objects.hash(title, symbol);
    }
}
