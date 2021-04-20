package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.PopupWindowFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Controller for Overview view.
 *
 * @author tobiasth, bragemi
 */
public class TaskViewController {
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXComboBox filterBox;
    @FXML
    private ListView<TaskViewMode> viewModeList;
    @FXML
    private ListView<String> categoryList;
    @FXML
    private VBox taskList;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private VBox taskDetailPanel;

    private TaskDetailsController activeTaskDetailsBox;
    private final List<Task> displayedTasks = new ArrayList<>();
    private final List<String> displayedCategories = new ArrayList<>();

    private TaskViewMode viewMode = TaskViewMode.OVERVIEW;

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

        refreshAndFilterTaskList();

        // Initialize view mode list.
        viewModeList.setItems(FXCollections.observableList(List.of(TaskViewMode.values())));
        viewModeList.getSelectionModel().selectedItemProperty().addListener(mode -> changeTaskViewMode());

        // Link category list view to category list.
        refreshCategoryList();

        // Make detail panel grow to fill right menu.
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
        List<Task> tasksToDisplay = new ArrayList<>(Session.getActiveUser().getTaskList().getTasks());

        switch (viewMode) {
        case OVERVIEW: {
            break;
        }

        case UPCOMING: {
            tasksToDisplay.sort(Comparator.comparing(Task::getEndTime));
            break;
        }

        case DAY: {
            tasksToDisplay = tasksToDisplay.stream().filter(task -> task.getStartTime()
                    .datesUntil(task.getEndTime().plusDays(1)).anyMatch(date -> date.equals(LocalDate.now())))
                    .collect(Collectors.toList());
            break;
        }

        case WEEK: {
            List<LocalDate> nextWeekDates = LocalDate.now().datesUntil(LocalDate.now().plusDays(8))
                    .collect(Collectors.toList());

            tasksToDisplay = tasksToDisplay.stream().filter(task -> task.getStartTime()
                    .datesUntil(task.getEndTime().plusDays(1)).anyMatch(nextWeekDates::contains))
                    .collect(Collectors.toList());
            break;
        }

        case MONTH: {
            List<LocalDate> nextMonthDates = LocalDate.now().datesUntil(LocalDate.now().plusMonths(1).plusDays(1))
                    .collect(Collectors.toList());

            tasksToDisplay = tasksToDisplay.stream().filter(task -> task.getStartTime()
                    .datesUntil(task.getEndTime().plusDays(1)).anyMatch(nextMonthDates::contains))
                    .collect(Collectors.toList());
            break;
        }

        default: {
            Session.getLogger().log(Level.INFO, "The view mode {} has not been implemented yet.", viewMode);
        }
        }

        if (!query.isBlank()) {
            tasksToDisplay = tasksToDisplay.stream()
                    .filter(task -> task.getTitle().toLowerCase(Locale.ROOT).contains(query)
                            || task.getDescription().toLowerCase(Locale.ROOT).contains(query)
                            || task.getCategory().getTitle().toLowerCase(Locale.ROOT).contains(query))
                    .collect(Collectors.toList());
        }

        displayedTasks.clear();
        displayedTasks.addAll(tasksToDisplay);

        taskList.getChildren().clear();
        for (Task task : displayedTasks) {
            try {
                TaskBoxController taskbox = new TaskBoxController(task, this);
                taskList.getChildren().add(taskbox.getRootContainer());
            } catch (IOException e) {
                DialogFactory.getOKDialog("Task box insertion failed", "Could not insert taskbox.\n" + e.getMessage())
                        .show();
            }
        }
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
            activeTaskDetailsBox.updateLabels();
        } catch (IOException e) {
            DialogFactory.getOKDialog("Add category failed", "Unable to open add category window.").show();
        }

    }

    /**
     * Refreshes the category list.
     */
    private void refreshCategoryList() {
        displayedCategories.clear();
        displayedCategories.addAll(Session.getActiveUser().getTaskList().getCategories().stream()
                .map(c -> String.format("%s %s", c.getIcon(), c.getTitle())).sorted().collect(Collectors.toList()));

        categoryList.setItems(FXCollections.observableList(displayedCategories));
        categoryList.refresh();
    }

    /**
     * Changes the task view mode and refreshes the task list.
     */
    private void changeTaskViewMode() {
        viewMode = viewModeList.getSelectionModel().getSelectedItem();
        refreshAndFilterTaskList();
    }

    /**
     * Shows task details in the right side of the screen.
     *
     * @param selectedTask
     *            Selected task.
     */
    protected void showTaskDetails(Task selectedTask) {
        try {
            Objects.requireNonNull(selectedTask);
            taskDetailPanel.getChildren().clear();
            activeTaskDetailsBox = new TaskDetailsController(selectedTask, this);
            taskDetailPanel.getChildren().add(activeTaskDetailsBox.getRootContainer());
        } catch (IOException e) {
            DialogFactory.getOKDialog("Task detail failed", "Failed to show detailed task view.").show();
        } catch (NullPointerException ignored) {
        }
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

    enum TaskViewMode {
        OVERVIEW, UPCOMING, DAY, WEEK, MONTH
    }

    /**
     * Fills the desired filters into the filterBox in the GUI
     */
    public void addFilters() {
        filterBox.getItems().clear();
        String[] filters = { "Name (A - Z)", "Name (Z - A)", "Category (A - Z)", "Category (Z - A)",
                "Priority (High - Low)", "Priority (Low - High)" };
        filterBox.getItems().addAll(filters);
    }

    /**
     * Filters the tasks by the chosen filter
     *
     * @throws IOException
     *
     * @author andetel
     */
    public void filterTasks() throws IOException {

        if (filterBox.getValue() != null) {

            taskList.getChildren().clear();
            if (filterBox.getValue().toString().equals("Priority (High - Low)")) {
                for (Task task : Session.getActiveUser().getTaskList().sortByPriority()) {
                    TaskBoxController taskBoxController = new TaskBoxController(task, this);
                    taskList.getChildren().add(taskBoxController.getRootContainer());
                }
            } else if (filterBox.getValue().toString().equals("Priority (Low - High)")) {
                ArrayList<Task> prioritySorted = Session.getActiveUser().getTaskList().sortByPriority();
                Collections.reverse(prioritySorted);
                for (Task task : prioritySorted) {
                    TaskBoxController taskBoxController = new TaskBoxController(task, this);
                    taskList.getChildren().add(taskBoxController.getRootContainer());
                }
            } else if (filterBox.getValue().toString().equals("Category (A - Z)")) {
                for (Task task : Session.getActiveUser().getTaskList().sortByCategory()) {
                    TaskBoxController taskBoxController = new TaskBoxController(task, this);
                    taskList.getChildren().add(taskBoxController.getRootContainer());
                }
            } else if (filterBox.getValue().toString().equals("Category (Z - A)")) {
                ArrayList<Task> categorySorted = Session.getActiveUser().getTaskList().sortByCategory();
                Collections.reverse(categorySorted);
                for (Task task : categorySorted) {
                    TaskBoxController taskBoxController = new TaskBoxController(task, this);
                    taskList.getChildren().add(taskBoxController.getRootContainer());
                }
            } else if (filterBox.getValue().toString().equals("Name (A - Z)")) {
                for (Task task : Session.getActiveUser().getTaskList().sortByName()) {
                    TaskBoxController taskBoxController = new TaskBoxController(task, this);
                    taskList.getChildren().add(taskBoxController.getRootContainer());
                }
            } else if (filterBox.getValue().toString().equals("Name (Z - A)")) {
                ArrayList<Task> nameSorted = Session.getActiveUser().getTaskList().sortByName();
                Collections.reverse(nameSorted);
                for (Task task : nameSorted) {
                    TaskBoxController taskBoxController = new TaskBoxController(task, this);
                    taskList.getChildren().add(taskBoxController.getRootContainer());
                }
            }

        }
    }

}