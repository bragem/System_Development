package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.todolistapp.Session;
import edu.ntnu.idatt1002.k2g10.todolistapp.factories.DialogFactory;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Category;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Priority;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * Controller for the add task window.
 *
 * @author chrisoss, jonathhl, andetel
 */
public class AddTaskController {
    @FXML
    private JFXTextField titleField;
    @FXML
    private JFXTextArea descriptionField;
    @FXML
    private JFXComboBox<String> priorityDropdown;
    @FXML
    private JFXComboBox<String> categoryDropdown;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    /**
     * Initializes the view on load.
     */
    public void initialize() {
        // Add priorities and categories to dropdowns
        Arrays.stream(Priority.values()).forEach(p -> priorityDropdown.getItems().add(p.toString()));
        Session.getActiveUser().getTaskList().getCategories()
                .forEach(c -> categoryDropdown.getItems().add(c.getTitle()));
    }

    /**
     * Adds new task when user presses "add task"-button.
     * <p>
     * Also saves user data after adding task.
     *
     * @author bragem, trthingnes
     */
    public void onSubmit() {
        // Get current stage from a field.
        Stage stage = (Stage) titleField.getScene().getWindow();

        String title = titleField.getText();
        String desc = descriptionField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Priority priority = Priority.NONE;
        if (!Objects.isNull(priorityDropdown.getSelectionModel().getSelectedItem())) {
            priority = Priority
                    .valueOf(priorityDropdown.getSelectionModel().getSelectedItem().toUpperCase(Locale.ROOT));
        }
        Category category = Session.getActiveUser().getTaskList().getCategories().stream()
                .filter(c -> c.getTitle().equals(categoryDropdown.getSelectionModel().getSelectedItem())).findAny()
                .orElse(null);

        if (title.isBlank() || category == null) {
            String content = "Some of the data entered was invalid, please correct it and try again.";
            DialogFactory.getOKDialog("Add task failed", content).show();
            return;
        }

        if (startDate != null) {
            if (endDate == null || endDate.isBefore(startDate)) {
                String content = "End date can not be empty, and it has to be after start date.";
                DialogFactory.getOKDialog("Add task failed", content).show();
                return;
            }
        }

        try {
            Task newTask = new Task(title, desc, startDate, endDate, priority, category);
            Session.getActiveUser().getTaskList().getTasks().add(newTask);
            Session.save();
            stage.close();
        } catch (SQLException e) {
            String content = String.format("Unable to add task.%nError message: '%s'", e.getMessage());
            Session.getLogger().warning(content);
            DialogFactory.getOKDialog("Task add failed", content).show();
        }
    }
}
