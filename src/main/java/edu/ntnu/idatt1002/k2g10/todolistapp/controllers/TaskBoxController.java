package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.FXMLLoaderFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Task;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.TaskList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Custom controller for displaying a {@link Task} in a {@link TaskList}.
 *
 * @author jonathhl, trthingnes
 */
public class TaskBoxController {
    @FXML
    private HBox container;
    @FXML
    private Label titleLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private JFXCheckBox completedBox;

    private final Task task;
    private final TaskViewController parentController;

    /**
     * Creates a new instance of {@link TaskBoxController}. This instance cannot be added to a {@link Node} directly. To
     * add this custom module, use the {@link HBox} from {@link #getRootContainer}.
     *
     * @param task
     *            Task to display in box.
     *
     * @throws IOException
     *             If FXML file cannot be read.
     */
    public TaskBoxController(Task task, TaskViewController parentController) throws IOException {
        FXMLLoader loader = FXMLLoaderFactory.getFXMLLoader("taskbox");
        loader.setController(this);
        loader.load();

        this.parentController = parentController;
        this.task = task;
        updateLabels();

        // Set width to make the container always grow.
        container.setPrefWidth(Double.MAX_VALUE);
    }

    @FXML
    public void showTaskDetails() {
        parentController.showTaskDetails(task);
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
        parentController.showTaskDetails(task);
        parentController.refreshAndFilterTaskList();
    }

    /**
     * Update labels with latest information from {@link Task}.
     */
    public void updateLabels() {
        titleLabel.setText(task.getTitle());
        categoryLabel.setText(String.valueOf(task.getCategory().getIcon()));
        categoryLabel.setStyle(String.format("-fx-text-fill: %s !important; -fx-font-size: 14px!important;", task.getCategory().getColor()));
        priorityLabel.setStyle(String.format("-fx-text-fill: %s !important", task.getPriority().getColor()));
        completedBox.selectedProperty().setValue(task.getCompleted());

        // Build and insert dates.
        StringBuilder dateBuilder = new StringBuilder();
        if (Objects.nonNull(task.getStartTime())) {
            dateBuilder.append(task.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            dateBuilder.append(" until ");
        }
        dateBuilder.append(task.getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        dateLabel.setText(dateBuilder.toString());
    }

    /**
     * Get the {@link HBox} container of the {@link TaskBoxController}. This can be used to add the box to another
     * {@link Node}.
     *
     * @return {@link HBox} containing {@link TaskBoxController} content.
     */
    public HBox getRootContainer() {
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
