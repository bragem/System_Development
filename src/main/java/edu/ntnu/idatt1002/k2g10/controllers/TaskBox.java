package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.ntnu.idatt1002.k2g10.models.Task;
import edu.ntnu.idatt1002.k2g10.models.TaskList;
import edu.ntnu.idatt1002.k2g10.utils.files.FXMLFile;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Custom controller for displaying a {@link Task} in a {@link TaskList}.
 * 
 * @author jonathhl, trthingnes
 */
public class TaskBox extends Pane {
    @FXML
    private HBox container;
    @FXML
    private Label titleLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private JFXCheckBox completedBox;

    private final TaskListController parentController;
    private final Task task;

    /**
     * Creates a new instance of {@link TaskBox}. This instance cannot be added to a {@link Node} directly. To add this
     * custom module, use the {@link HBox} from {@link #getContainer}.
     *
     * @param task
     *            Task to display in box.
     * 
     * @throws IOException
     *             If FXML file cannot be read.
     */
    public TaskBox(Task task, TaskListController parentController) throws IOException {
        FXMLFile.load("taskbox", this);

        this.parentController = parentController;
        this.task = task;
        updateLabels();

        // Set width to make the container always grow.
        container.setPrefWidth(Double.MAX_VALUE);

        // Set the TaskBox CSS ID to task title for easier event handling.
        container.setId(task.getTitle());
    }

    /**
     * Update labels with latest information from {@link Task}.
     */
    public void updateLabels() {
        titleLabel.setText(task.getTitle());
        categoryLabel.setText(String.valueOf(task.getCategory().getIcon()));
        priorityLabel.setText(task.getPriority().toString());
        completedBox.selectedProperty().setValue(task.getCompleted());
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
     * Get the {@link HBox} container of the {@link TaskBox}. This can be used to add the box to another {@link Node}.
     * 
     * @return {@link HBox} containing {@link TaskBox} content.
     */
    public HBox getContainer() {
        return container;
    }

    /**
     * Attempts to show {@link Task} detail view on click.
     * 
     * @param event
     *            Click event.
     * 
     * @throws IOException
     *             If {@link DetailTaskBox} cannot be created.
     */
    @FXML
    public void showTaskDetails(Event event) throws IOException {
        parentController.showTaskDetails(task);
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

        // Updates the view in the parent controller.
        parentController.update();
    }
}
