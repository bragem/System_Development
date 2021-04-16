package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.factory.DialogFactory;
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
 * @author tobiasth, bragemi
 */
public class OverviewController implements TaskListController {
    @FXML
    private JFXTextField searchField;
    @FXML
    private ListView<String> categoryList;
    @FXML
    private TableView<Task> taskList;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private VBox taskDetailPanel;

    private final List<Task> displayedTasks = new ArrayList<>();
    private final List<String> displayedCategories = new ArrayList<>();

    /**
     * Initializes task and category lists and user info labels.
     *
     * Runs on scene load.
     */
    @FXML
    public void initialize() {
        // Fill user info
        usernameLabel.setText(Session.getActiveUser().getUsername());
        emailLabel.setText(Session.getActiveUser().getEmail());

        // Initialize task list
        TableColumnFactory<Task, String> columnFactory = new TableColumnFactory<>();
        TableColumn<Task, String> titleColumn = columnFactory.getTableColumn("Title", "title");
        TableColumn<Task, String> priorityColumn = columnFactory.getTableColumn("Priority", "priority");
        TableColumn<Task, String> categoryColumn = columnFactory.getTableColumn("Category", "category");
        taskList.getColumns().addAll(List.of(titleColumn, priorityColumn, categoryColumn));


        // Link task list table to the task list.
        taskList.getSelectionModel().selectedItemProperty().addListener((task) -> showTaskDetails());
        refreshAndFilterTaskList();

        // Link category list view to category list.
        refreshCategoryList();

        // Make detail panel grow to fill right menu
        taskDetailPanel.setPrefHeight(Double.MAX_VALUE);
    }

    /**
     * Shows a popup allowing the user to add a task, then refreshes the task list.
     */
    @FXML
    public void showAddTask() {
        try {
            Stage popupWindow = PopupWindowFactory.getPopupWindow("add-task");
            popupWindow.setTitle("Add new task");
            popupWindow.showAndWait();
            refreshAndFilterTaskList();
            taskList.refresh();
        } catch (IOException e) {
            DialogFactory.getOKDialog("Add task failed", "Unable to open add task window.").show();
        }
    }

    /**
     * Refreshes the task list and applies the search query if there is one.
     */
    @FXML
    public void refreshAndFilterTaskList() {
        String query = searchField.getText().toLowerCase(Locale.ROOT);

        List<Task> tasksToDisplay = new ArrayList<>();
        if(!query.isBlank()) {
            for(Task task : Session.getActiveUser().getTaskList().getTasks()) {
                if(task.getTitle().toLowerCase(Locale.ROOT).contains(query) ||
                        task.getDescription().toLowerCase(Locale.ROOT).contains(query) ||
                        task.getCategory().getTitle().toLowerCase(Locale.ROOT).contains(query)
                ) {
                    tasksToDisplay.add(task);
                }
            }
        }
        else {
            tasksToDisplay = Session.getActiveUser().getTaskList().getTasks();
        }

        displayedTasks.clear();
        displayedTasks.addAll(tasksToDisplay);
        taskList.setItems(FXCollections.observableList(displayedTasks));
        taskList.refresh();
    }

    /**
     * Shows a popup allowing the user to add a category, then refreshes the category list.
     */
    @FXML
    public void showAddCategory() {
        try {
            Stage popupWindow = PopupWindowFactory.getPopupWindow("add-category");
            popupWindow.showAndWait();
            refreshCategoryList();
            showTaskDetails();
        } catch (IOException e) {
            DialogFactory.getOKDialog("Add category failed", "Unable to open add category window.").show();
        }

    }

    /**
     * Refreshes the category list.
     */
    private void refreshCategoryList() {
        displayedCategories.clear();
        displayedCategories.addAll(Session.getActiveUser().getTaskList().getCategories()
                .stream()
                .map(c -> String.format("%s %s", c.getIcon(), c.getTitle()))
                .sorted()
                .collect(Collectors.toList()));

        categoryList.setItems(FXCollections.observableList(displayedCategories));
    }

    /**
     * Shows task details in the right side of the screen.
     *
     * This method runs on task list selection.
     */
    private void showTaskDetails()  {
        Task selectedTask = taskList.getSelectionModel().getSelectedItem();

        try {
            Objects.requireNonNull(selectedTask);
            taskDetailPanel.getChildren().clear();
            taskDetailPanel.getChildren().add(new TaskDetailsController(selectedTask, this).getRootContainer());
        } catch (IOException | NullPointerException ignored) {/*TODO*/}
    }

    /**
     * Shows a popup allowing user to change settings.
     */
    @FXML
    public void showSettings() {
        try {
            Stage popupWindow = PopupWindowFactory.getPopupWindow("settings");
            popupWindow.show();
        } catch (IOException e) {
            DialogFactory.getOKDialog("Open settings failed", "Unable to open add settings window.").show();
        }
    }

    /**
     * Takes the user back to the login screen.
     */
    @FXML
    public void logout() {
        try {
            Session.setLocation("login");
        } catch (IOException e) {
            DialogFactory.getOKDialog("Logout failed", "Unable to set location to login screen.").show();
        }
    }
}