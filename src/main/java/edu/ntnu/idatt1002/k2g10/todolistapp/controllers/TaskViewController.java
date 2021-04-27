package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.PopupWindowFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Category;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Task;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for the overview window.
 *
 * @author trthingnes, bragemi
 */
public class TaskViewController {
    @FXML
    private JFXTextField searchField;
    @FXML
    private ListView<TaskViewMode> viewModeList;
    @FXML
    private ListView<Category> categoryList;
    @FXML
    private ComboBox<String> categoryDeleteDropdown;
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

    private TaskViewMode viewMode = TaskViewMode.OVERVIEW;
    private Category categoryFilter = null;

    /**
     * Enum for task filtering modes.
     */
    enum TaskViewMode {
        OVERVIEW, UPCOMING, DAY, WEEK, MONTH, COMPLETED
    }

    /**
     * Initializes {@link Task} and {@link Category} lists and {@link User} info labels on load.
     */
    @FXML
    public void initialize() {
        // Fill user info
        usernameLabel.setText(Session.getActiveUser().getUsername());
        emailLabel.setText(Session.getActiveUser().getEmail());

        refreshAndFilterTaskList();

        // Initialize view mode list.
        viewModeList.getItems().addAll(TaskViewMode.values());
        viewModeList.getSelectionModel().selectedItemProperty().addListener(mode -> changeTaskViewMode());

        // Link category list view to category list.
        categoryList.getSelectionModel().selectedItemProperty().addListener(category -> changeCategoryViewMode());
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
            Session.getLogger()
                    .severe(String.format("Unable to open add task window.%nError message: '%s'", e.getMessage()));
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

        // Filter by view mode.
        switch (viewMode) {
        case OVERVIEW: {
            tasksToDisplay = tasksToDisplay.stream().filter(task -> !task.getCompleted()).collect(Collectors.toList());
            break;
        }

        case UPCOMING: {
            tasksToDisplay = tasksToDisplay.stream().filter(task -> !task.getCompleted()).collect(Collectors.toList());
            tasksToDisplay.sort(Comparator.comparing(Task::getEndTime));
            break;
        }

        case DAY: {
            tasksToDisplay = tasksToDisplay.stream().filter(task -> !task.getCompleted()).collect(Collectors.toList());
            tasksToDisplay = tasksToDisplay.stream().filter(task -> task.getStartTime()
                    .datesUntil(task.getEndTime().plusDays(1)).anyMatch(date -> date.equals(LocalDate.now())))
                    .collect(Collectors.toList());
            break;
        }

        case WEEK: {
            tasksToDisplay = tasksToDisplay.stream().filter(task -> !task.getCompleted()).collect(Collectors.toList());
            List<LocalDate> nextWeekDates = LocalDate.now().datesUntil(LocalDate.now().plusDays(8))
                    .collect(Collectors.toList());

            tasksToDisplay = tasksToDisplay.stream().filter(task -> task.getStartTime()
                    .datesUntil(task.getEndTime().plusDays(1)).anyMatch(nextWeekDates::contains))
                    .collect(Collectors.toList());
            break;
        }

        case MONTH: {
            tasksToDisplay = tasksToDisplay.stream().filter(task -> !task.getCompleted()).collect(Collectors.toList());
            List<LocalDate> nextMonthDates = LocalDate.now().datesUntil(LocalDate.now().plusMonths(1).plusDays(1))
                    .collect(Collectors.toList());

            tasksToDisplay = tasksToDisplay.stream().filter(task -> task.getStartTime()
                    .datesUntil(task.getEndTime().plusDays(1)).anyMatch(nextMonthDates::contains))
                    .collect(Collectors.toList());
            break;
        }

        case COMPLETED: {
            tasksToDisplay = tasksToDisplay.stream().filter(Task::getCompleted).collect(Collectors.toList());
            break;
        }

        default: {
            Session.getLogger().log(Level.WARNING, "The view mode {} has not been implemented yet.", viewMode);
        }
        }

        // Filter by selected category.
        tasksToDisplay = tasksToDisplay.stream()
                .filter(task -> Objects.isNull(categoryFilter) || task.getCategory() == categoryFilter)
                .collect(Collectors.toList());

        // Filter by search
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
                Session.getLogger()
                        .severe(String.format("Unable to insert task box.%nError message: '%s'", e.getMessage()));
                DialogFactory.getOKDialog("Refresh failed", "Unable to refresh task list.").show();
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
            popupWindow.setTitle("Add new category");
            popupWindow.showAndWait();
            refreshCategoryList();
            if (Objects.nonNull(activeTaskDetailsBox)) {
                activeTaskDetailsBox.updateLabels();
            }
        } catch (IOException e) {
            Session.getLogger()
                    .severe(String.format("Unable to open add category window.%nError message: '%s'", e.getMessage()));
            DialogFactory.getOKDialog("Add category failed", "Unable to open add category window.").show();
        }

    }

    /**
     * Changes the category filter and refreshes the task list.
     */
    @FXML
    public void changeCategoryViewMode() {
        categoryFilter = categoryList.getSelectionModel().getSelectedItem();

        refreshAndFilterTaskList();
    }

    /**
     * Deletes the selected category on user confirmation.
     */
    @FXML
    public void deleteSelectedCategory() {
        if (!Session.getActiveUser().getTaskList().getTasks().isEmpty()
                && Session.getActiveUser().getTaskList().getCategories().size() == 1) {
            DialogFactory.getOKDialog("Category remove failed", "Cannot remove last category when tasks exist.").show();
            return;
        }

        Category category = Session.getActiveUser().getTaskList().getCategories().stream()
                .filter(c -> c.getTitle().equals(categoryDeleteDropdown.getSelectionModel().getSelectedItem()))
                .findAny().orElse(null);

        if (Objects.nonNull(category)) {
            String content = String.format("Are you sure you want to delete '%s'", category.getTitle());
            Dialog<ButtonType> dialog = DialogFactory.getYesNoDialog("Delete category?", content);
            Optional<ButtonType> buttonChoice = dialog.showAndWait();

            if (buttonChoice.isPresent() && buttonChoice.get().equals(ButtonType.YES)) {
                // Get all tasks that will lose their category.
                Stream<Task> needsNewCategory = Session.getActiveUser().getTaskList().getTasks().stream()
                        .filter(task -> task.getCategory().equals(category));

                // Remove the category.
                Session.getActiveUser().getTaskList().getCategories().remove(category);

                // Assign a new category to the tasks that need it.
                if (!Session.getActiveUser().getTaskList().getCategories().isEmpty()) {
                    Category newCategory = Session.getActiveUser().getTaskList().getCategories().get(0);
                    needsNewCategory.forEach(task -> task.setCategory(newCategory));
                }

                try {
                    Session.save();
                } catch (SQLException e) {
                    Session.getLogger().warning("Unable to delete category from database.");
                    DialogFactory.getOKDialog("Category delete failed.", "Unable to save to account.").show();
                }
                refreshCategoryList();
                refreshAndFilterTaskList();
            }
        }
    }

    /**
     * Refreshes the category list.
     */
    private void refreshCategoryList() {
        categoryList.getItems().clear();
        categoryList.getItems().addAll(Session.getActiveUser().getTaskList().getCategories());

        // Add categories to delete list.
        List<String> categoryNames = Session.getActiveUser().getTaskList().getCategories().stream()
                .map(Category::getTitle).collect(Collectors.toList());
        categoryDeleteDropdown.getItems().clear();
        categoryDeleteDropdown.getItems().addAll(categoryNames);
    }

    /**
     * Changes the task view mode and refreshes the task list.
     */
    private void changeTaskViewMode() {
        viewMode = viewModeList.getSelectionModel().getSelectedItem();

        // Reset category filter
        categoryFilter = null;

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
            Session.getLogger()
                    .severe(String.format("Failed to show detail task box.%nError message: '%s'", e.getMessage()));
            DialogFactory.getOKDialog("Task detail failed", "Failed to show detailed task view.").show();
        } catch (NullPointerException ignored) {
            /* Do not try to show detail task box if task is not defined */
        }
    }

    /**
     * Shows a popup allowing user to change settings.
     */
    @FXML
    public void showSettings() {
        try {
            Stage popupWindow = PopupWindowFactory.getPopupWindow("settings");
            popupWindow.showAndWait();
            usernameLabel.setText(Session.getActiveUser().getUsername());
            emailLabel.setText(Session.getActiveUser().getEmail());
            refreshAndFilterTaskList();
        } catch (IOException e) {
            Session.getLogger()
                    .severe(String.format("Unable to open settings window.%nError message: '%s'", e.getMessage()));
            DialogFactory.getOKDialog("Add task failed", "Unable to open settings window.").show();
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
            Session.getLogger()
                    .severe(String.format("Unable to open login screen%nError message: '%s'", e.getMessage()));
            DialogFactory.getOKDialog("Logout failed", "Unable to go to login screen.").show();
        }
    }
}