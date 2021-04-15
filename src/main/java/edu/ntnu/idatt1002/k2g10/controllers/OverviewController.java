package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.factory.PopupWindowFactory;
import edu.ntnu.idatt1002.k2g10.factory.TableColumnFactory;
import edu.ntnu.idatt1002.k2g10.models.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for Overview view.
 *
 * @author tobiasth
 */
public class OverviewController {
    @FXML
    private JFXTextField searchField;
    @FXML
    private ListView<String> categoryList;
    @FXML
    private TableView<Task> taskList;
    @FXML
    private VBox taskDetailPanel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;

    private List<String> categoryTitles;

    /**
     * Runs when the view is loaded.
     *
     * @throws IOException
     *             If updating the view fails.
     */
    @FXML
    public void initialize() throws IOException {
        // Fill user info
        usernameLabel.setText(Session.getActiveUser().getUsername());
        emailLabel.setText(Session.getActiveUser().getEmail());

        // Initialize task list
        TableColumnFactory<Task, String> columnFactory = new TableColumnFactory<>();
        TableColumn<Task, String> titleColumn = columnFactory.getTableColumn("Title", "title");
        TableColumn<Task, String> priorityColumn = columnFactory.getTableColumn("Priority", "priority");
        TableColumn<Task, String> categoryColumn = columnFactory.getTableColumn("Category", "category");
        taskList.getColumns().addAll(List.of(titleColumn, priorityColumn, categoryColumn));
        taskList.setItems(FXCollections.observableList(Session.getActiveUser().getTaskList().getTasks()));

        // Initialize category list
        categoryTitles = Session.getActiveUser().getTaskList().getCategories()
                .stream()
                .map(c -> String.format("%s %s", c.getIcon(), c.getTitle()))
                .sorted()
                .collect(Collectors.toList());
        categoryList.setItems(FXCollections.observableList(categoryTitles));

        // Make detail panel grow to fill right menu
        taskDetailPanel.setPrefHeight(Double.MAX_VALUE);
    }

    @FXML
    public void showAddTask() throws IOException {
        Stage popupWindow = PopupWindowFactory.getPopupWindow("add-task");
        popupWindow.showAndWait();

        taskList.refresh();
    }

    @FXML
    public void showAddCategory() throws IOException {
        Stage popupWindow = PopupWindowFactory.getPopupWindow("add-category");
        popupWindow.showAndWait();

        categoryTitles.clear();
        categoryTitles.addAll(Session.getActiveUser().getTaskList().getCategories()
                .stream()
                .map(c -> String.format("%s %s", c.getIcon(), c.getTitle()))
                .sorted()
                .collect(Collectors.toList()));

        categoryList.refresh();
    }

    /**
     * Opens a settings popup window.
     *
     * @throws IOException
     *             If the FXML file fails to load.
     */
    @FXML
    public void showSettings() throws IOException {
        Stage popupWindow = PopupWindowFactory.getPopupWindow("settings");
        popupWindow.show();
    }

    /**
     * Takes the user to the login screen.
     *
     * @throws IOException
     *             If login screen fails to load.
     */
    @FXML
    public void logout() throws IOException {
        Session.setLocation("login");
    }

    //TODO: Search logic
}