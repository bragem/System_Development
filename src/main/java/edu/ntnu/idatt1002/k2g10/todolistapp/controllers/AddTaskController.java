package edu.ntnu.idatt1002.k2g10.todolistapp.controllers;

import com.jfoenix.controls.*;
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

/**
 * Custom controller for adding a new task
 *
 * @author chrisoss
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

    public void initialize() {
        // Add priorities and categories to dropdowns
        Arrays.stream(Priority.values()).forEach(p -> priorityDropdown.getItems().add(p.toString()));
        Session.getActiveUser().getTaskList().getCategories()
                .forEach(c -> categoryDropdown.getItems().add(c.getTitle()));
    }

    /**
     * Method which adds new task when user presses "add task"-button. Also saves the userdata to file upon completion.
     *
     * @author bragem, trthingnes
     */
    public void onSubmit() {
        // Get current stage from a field
        Stage stage = (Stage) titleField.getScene().getWindow();

        String title = titleField.getText();
        String desc = descriptionField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Priority priority = Priority
                .valueOf(priorityDropdown.getSelectionModel().getSelectedItem().toUpperCase(Locale.ROOT));
        Category category = Session.getActiveUser().getTaskList().getCategories().stream()
                .filter(c -> c.getTitle().equals(categoryDropdown.getSelectionModel().getSelectedItem())).findAny()
                .orElse(null);

        if ((endDate == null || endDate.isBefore(startDate))) {
            DialogFactory
                    .getOKDialog("Task Not Added",
                            "Start Date can not be set after End Date\n\nStart Date and or End Date can not be null")
                    .show();
            return;
        }

        try {
            Task newTask = new Task(title, desc, startDate, endDate, priority, category);
            Session.getActiveUser().getTaskList().addTask(newTask);
            Session.save();
            stage.close();
        } catch (SQLException e) {
            DialogFactory.getOKDialog("Task add failed", "Unable to add task.\n(" + e.getMessage() + ")").show();
        }
    }
}
