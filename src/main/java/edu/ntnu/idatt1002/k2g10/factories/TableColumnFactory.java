package edu.ntnu.idatt1002.k2g10.factories;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Generates {@link TableColumn} objects.
 * 
 * @param <S>
 *            Type of the {@link javafx.scene.control.TableView} generic type.
 * @param <T>
 *            Type of the content in all cells of the column.
 * 
 * @author trthingnes
 */
public class TableColumnFactory<S, T> {
    /**
     * Gets a table column with the given header and property value.
     *
     * @param header
     *            Column header.
     * @param propertyValue
     *            Property value of column.
     * 
     * @return Table column.
     */
    public TableColumn<S, T> getTableColumn(String header, String propertyValue) {
        TableColumn<S, T> column = new TableColumn<>(header);

        column.setCellValueFactory(new PropertyValueFactory<>(propertyValue));

        return column;
    }
}
