package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.*;
import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.FXMLLoaderFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Category;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Priority;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controller for displaying {@link Task} details.
 *
 * @author jonathhl, trthingnes
 */
public class TaskDetailsController {
    @FXML
    private VBox container;
    @FXML
    private JFXTextField titleField;
    @FXML
    private JFXTextArea descriptionArea;
    @FXML
    private JFXCheckBox completedBox;
    @FXML
    private JFXComboBox<String> categoryDropdown;
    @FXML
    private Label categoryIconLabel;
    @FXML
    private JFXComboBox<String> priorityDropdown;
    @FXML
    private Label priorityIconLabel;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    private final Task task;
    private final TaskViewController parentController;

    /**
     * Creates a new instance of {@link TaskDetailsController}. This instance cannot be added to a {@link Node}
     * directly. To add this custom module, use the {@link VBox} from {@link #getRootContainer}.
     *
     * @param task
     *            Task to show detailed view for.
     * 
     * @throws IOException
     *             If {@link TaskDetailsController} fails to load.
     */
    public TaskDetailsController(Task task, TaskViewController parentController) throws IOException {
        this.parentController = parentController;
        this.task = task;

        FXMLLoader loader = FXMLLoaderFactory.getFXMLLoader("task-details");
        loader.setController(this);
        loader.load();
    }

    @FXML
    public void initialize() {
        updateLabels();

        // Set handler functions for changes in combo boxes
        categoryDropdown.getSelectionModel().selectedItemProperty().addListener(c -> updateIcons());
        priorityDropdown.getSelectionModel().selectedItemProperty().addListener(c -> updateIcons());

        // Set checkbox ID to task title.
        completedBox.setId(task.getTitle());
    }

    /**
     * Save changes to {@link Task}.
     */
    @FXML
    public void saveTaskChanges() {
        task.setTitle(titleField.getText());
        task.setDescription(descriptionArea.getText());

        Category category = Session.getActiveUser().getTaskList().getCategories().stream()
                .filter(c -> c.getTitle().equals(categoryDropdown.getSelectionModel().getSelectedItem())).findAny()
                .orElse(task.getCategory());
        task.setCategory(category);
        categoryIconLabel.setText(String.valueOf(task.getCategory().getIcon()));

        Priority priority = Arrays.stream(Priority.values())
                .filter(p -> p.toString().equals(priorityDropdown.getSelectionModel().getSelectedItem())).findAny()
                .orElse(task.getPriority());
        task.setPriority(priority);

        if ((endDate == null || endDate.getValue().isBefore(startDate.getValue()))) {
            DialogFactory.getOKDialog("Task Not Updated",
                    "Start Date cannot be set after End Date/\nEnd Date cannot be null").show();
            return;
        }
        task.setStartTime(startDate.getValue());
        task.setEndTime(endDate.getValue());

        parentController.refreshAndFilterTaskList();

        // Saves user to DB.
        try {
            Session.save();
        } catch (SQLException e) {
            DialogFactory.getOKDialog("Database save failed", "Could not save to database: " + e.getMessage()).show();
        }
    }

    /**
     * Updated the completed status of the given event on checkbox check.
     *
     * @param event
     *            Click event.
     */
    @FXML
    public void saveTaskCompletedStatus(Event event) {
        task.setCompleted(completedBox.selectedProperty().get());
        parentController.refreshAndFilterTaskList();
    }

    /**
     * Update labels with latest information from {@link Task}.
     */
    public void updateLabels() {
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        completedBox.selectedProperty().set(task.getCompleted());

        List<String> categoryNames = Session.getActiveUser().getTaskList().getCategories().stream()
                .map(Category::getTitle).collect(Collectors.toList());
        categoryDropdown.getItems().addAll(categoryNames);
        categoryDropdown.getSelectionModel().select(task.getCategory().getTitle());

        List<String> priorityNames = Arrays.stream(Priority.values()).map(Priority::toString)
                .collect(Collectors.toList());
        priorityDropdown.getItems().addAll(priorityNames);
        priorityDropdown.getSelectionModel().select(task.getPriority().toString());

        // Update icons to match selected item.
        updateIcons();

        startDate.setValue(task.getStartTime());
        endDate.setValue(task.getEndTime());
    }

    /**
     * Updated the category icon with the icon for the selected category.
     */
    private void updateIcons() {
        Category category = Session.getActiveUser().getTaskList().getCategories().stream()
                .filter(c -> c.getTitle().equals(categoryDropdown.getSelectionModel().getSelectedItem())).findAny()
                .orElse(task.getCategory());
        categoryIconLabel.setText(String.valueOf(category.getIcon()));

        Priority priority = Priority
                .valueOf(priorityDropdown.getSelectionModel().getSelectedItem().toUpperCase(Locale.ROOT));
        priorityIconLabel.setStyle(String.format("-fx-text-fill: %s !important", priority.getColor()));
    }

    /**
     * Get the {@link VBox} container of the {@link TaskDetailsController}. This can be used to add the box to another
     * {@link Node}.
     *
     * @return {@link VBox} containing {@link TaskDetailsController} content.
     */
    public VBox getRootContainer() {
        return container;
    }

    /**
     * Gets the task from the box.
     *
     * @return Task contained in box.
     */
    public Task getTask() {
        return task;
    }
}
