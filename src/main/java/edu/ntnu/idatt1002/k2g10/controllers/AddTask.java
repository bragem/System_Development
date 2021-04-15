package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.*;
import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.exceptions.DuplicateTaskException;
import edu.ntnu.idatt1002.k2g10.models.Category;
import edu.ntnu.idatt1002.k2g10.models.Priority;
import edu.ntnu.idatt1002.k2g10.models.Task;
import edu.ntnu.idatt1002.k2g10.utils.crypto.EncryptionException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Custom controller for adding a new task
 *
 * @author chrisoss
 */
public class AddTask {
    @FXML
    private JFXTextField titleField;
    @FXML
    private JFXTextArea descriptionField;
    @FXML
    private JFXComboBox<String> priorityDropdown;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private JFXComboBox<String> categoryDropdown;

    public void initialize() {
        for (Priority priority : Priority.values()) {
            priorityDropdown.getItems().add(priority.toString());
        }
        priorityDropdown.getSelectionModel().select(Priority.MEDIUM.toString());

        for (Category category : Session.getActiveUser().getTaskList().getCategories()) {
            categoryDropdown.getItems().add(category.getTitle());
        }

    }

    /**
     * Method which adds new task when user presses "add task"-button. Also saves the userdata to file upon completion.
     *
     * @throws DuplicateTaskException
     * @throws IOException
     * @throws EncryptionException
     *
     * @author bragem
     */

    // TODO method throws encryptionException and its not supposed to
    // TODO popup does not close after addTask is pressed
    public void addTask() throws DuplicateTaskException, IOException, EncryptionException {
        String title = titleField.getText();
        String desc = descriptionField.getText();
        LocalDateTime startDate = startDatePicker.getValue().atStartOfDay();
        LocalDateTime endDate = endDatePicker.getValue().atStartOfDay();
        Priority priority = Priority.values()[priorityDropdown.getSelectionModel().getSelectedIndex()];
        Category category = null;

        for (Category c : Session.getActiveUser().getTaskList().getCategories()) {
            if (c.getTitle().equals(categoryDropdown.getSelectionModel().getSelectedItem())) {
                category = c;
            }
        }

        Task newTask = new Task(title, desc, startDate, endDate, priority, category);
        Session.getActiveUser().getTaskList().addTask(newTask);

        Stage popup = (Stage) Session.getPopup().getWindow();
        ((TaskListController) popup.getUserData()).update();

        Session.save(); // Saves user to file.
        popup.close();
    }

}
