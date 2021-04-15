package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.*;
import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.models.Category;
import edu.ntnu.idatt1002.k2g10.models.Priority;
import edu.ntnu.idatt1002.k2g10.models.Task;
import edu.ntnu.idatt1002.k2g10.utils.files.FXMLFile;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Controller for displaying {@link Task} details.
 *
 * @author jonathhl, trthingnes
 */
public class DetailTaskBox {
    @FXML
    private VBox container;
    @FXML
    private Label titleLabel;
    @FXML
    private JFXComboBox<String> categoryDropdown;
    @FXML
    private JFXComboBox<String> priorityDropdown;
    @FXML
    private JFXCheckBox completedBox;
    @FXML
    private JFXTextArea descriptionArea;
    @FXML
    private Label iconLabel;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    private final Task task;

    /**
     * Creates a new instance of {@link DetailTaskBox}. This instance cannot be added to a {@link Node} directly. To add
     * this custom module, use the {@link VBox} from {@link #getContainer}.
     *
     * @param task
     *            Task to show detailed view for.
     * 
     * @throws IOException
     *             If {@link DetailTaskBox} fails to load.
     */
    public DetailTaskBox(Task task) throws IOException {
        FXMLFile.load("detail-taskbox", this);

        this.task = task;
        updateLabels();

        // Set checkbox ID to task title.
        completedBox.setId(task.getTitle());
    }

    /**
     * Update labels with latest information from {@link Task}.
     */
    public void updateLabels() {
        titleLabel.setText(task.getTitle());
        categoryDropdown.getItems().addAll(Session.getActiveUser().getTaskList().getCategories().stream()
                .map(Category::getTitle).collect(Collectors.toList()));
        categoryDropdown.getSelectionModel().select(task.getCategory().getTitle());
        priorityDropdown.getItems()
                .addAll(Arrays.stream(Priority.values()).map(Priority::toString).collect(Collectors.toList()));
        priorityDropdown.getSelectionModel().select(task.getPriority().toString());
        completedBox.selectedProperty().set(task.getCompleted());
        descriptionArea.setText(task.getDescription());
        startDate.setValue(task.getStartTime().toLocalDate());
        endDate.setValue(task.getEndTime().toLocalDate());
        iconLabel.setText(String.valueOf(task.getCategory().getIcon()));
    }

    /**
     * Gets the task from the box.
     * 
     * @return Task contained in box.
     */
    public Task getTask() {
        return task;
    }

    /**
     * Get the {@link VBox} container of the {@link DetailTaskBox}. This can be used to add the box to another
     * {@link Node}.
     *
     * @return {@link VBox} containing {@link DetailTaskBox} content.
     */
    public VBox getContainer() {
        return container;
    }

    @FXML
    public void saveChanges() throws IOException {
        task.setTitle(titleLabel.getText());
        task.setDescription(descriptionArea.getText());
        Category category = Session.getActiveUser().getTaskList().getCategories().stream()
                .filter(c -> c.getTitle().equals(categoryDropdown.getSelectionModel().getSelectedItem())).findAny()
                .get();
        task.setPriority(Arrays.stream(Priority.values())
                .filter(p -> p.toString().equals(priorityDropdown.getSelectionModel().getSelectedItem())).findAny()
                .get());
        task.setCategory(category);
        task.setStartTime(startDate.getValue().atStartOfDay());
        task.setEndTime(endDate.getValue().atStartOfDay());
    }

    /**
     * Updated the completed status of the given event on checkbox check.
     *
     * @param event
     *            Click event.
     */
    @FXML
    public void updateCompletedStatus(Event event) throws IOException {
        JFXCheckBox checkBox = (JFXCheckBox) event.getTarget();

        if (checkBox.selectedProperty().get()) {
            task.setCompleted();
        } else {
            task.setNotCompleted();
        }
    }
}
