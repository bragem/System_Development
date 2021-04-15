package edu.ntnu.idatt1002.k2g10.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.models.Category;
import edu.ntnu.idatt1002.k2g10.models.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

/**
 * Controller for Upcoming view.
 *
 * @author jonathhl, trthingnes, bragemi
 */
public class Upcoming implements TaskListController {
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXComboBox filterBox;
    @FXML
    private VBox categoryList;
    @FXML
    private VBox taskList;
    @FXML
    private VBox taskDetailPanel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;

    private DetailTaskBox detailTaskBox;

    /**
     * Runs when the view is loaded.
     *
     * @throws IOException
     *             If updating the view fails.
     */
    @FXML
    public void initialize() throws IOException {
        // Make detail panel grow to fill right menu.
        taskDetailPanel.setPrefHeight(Double.MAX_VALUE);

        update();
    }

    /**
     * Opens a settings popup window.
     *
     * @throws IOException
     *             If the FXML file fails to load.
     */
    @FXML
    public void settings() throws IOException {
        Session.popup("Settings", "settings");
    }

    /**
     * Takes the user to the login screen.
     *
     * @throws IOException
     *             If login screen fails to load.
     */
    @FXML
    public void logout() throws IOException {
        Session.setLocation("login");
    }

    /**
     * Filter the tasks in the {@link TaskBox} by the search query provided by the user.
     *
     * @throws IOException
     *             If a problem is encountered while handling files.
     */
    @FXML
    public void filterTasksBySearch() throws IOException {
        String text = searchField.getText();
        String textLC = text.toLowerCase(Locale.ROOT);
        ArrayList<Task> tasks = new ArrayList<>();

        if (text.equals("")) {
            update();
        } else {
            for (Task task : Session.getActiveUser().getTaskList().getTasks()) {
                if (task.getTitle().toLowerCase(Locale.ROOT).contains(textLC)
                        || task.getDescription().toLowerCase(Locale.ROOT).contains(textLC)
                        || task.getCategory().getTitle().toLowerCase(Locale.ROOT).contains(textLC)) {
                    tasks.add(task);
                }
            }
            fillTaskList(tasks);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     *             If {@link AddTask} popup fails to display.
     */
    @FXML
    @Override
    public void showAddTask() throws IOException {
        Session.popup("Add new task", "add-task", this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     *             If {@link AddCategory} popup fails to display.
     */
    @FXML
    @Override
    public void showAddCategory() throws IOException {
        Session.popup("Add new category", "add-category", this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     *             If a problem is encountered while handling files.
     */
    @Override
    public void update() throws IOException {
        fillCategoryList(Session.getActiveUser().getTaskList().getCategories());
        fillTaskList(Session.getActiveUser().getTaskList().getTasks());
        fillUserInfo();
        addFilters();
        if (Objects.nonNull(detailTaskBox)) {
            showTaskDetails(detailTaskBox.getTask());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param task
     *            Task to display.
     * 
     * @throws IOException
     *             If {@link DetailTaskBox} fails to load.
     */
    @Override
    public void showTaskDetails(Task task) throws IOException {
        detailTaskBox = new DetailTaskBox(task, this);
        taskDetailPanel.getChildren().clear();
        taskDetailPanel.getChildren().add(detailTaskBox.getContainer());
    }

    /**
     * Fills all the added categories into the GUI
     *
     * @param categories HashSet of Category objects
     */
    public void fillCategoryList(HashSet<Category> categories) throws IOException{
        categoryList.getChildren().clear();
        for (Category c : categories) {
            HBox hbox = new HBox();
            hbox.getChildren().add(new Label(Character.toString(c.getIcon())));
            hbox.getChildren().add(new Label(c.getTitle()));

            // Adds some spacing between the icon and the label
            hbox.getChildren().get(1).setStyle("-fx-padding: 0px 0px 0px 5px");

            hbox.getStyleClass().add("menu-option");
            hbox.setStyle("-fx-padding: 5px 0px 5px 10px");
            categoryList.getChildren().add(hbox);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param tasks
     *            The tasks to display in list.
     * 
     * @throws IOException
     *             If {@link TaskBox} fails to load.
     */
    @Override
    public void fillTaskList(List<Task> tasks) throws IOException {
        taskList.getChildren().clear();

        for (Task task : tasks) {
            TaskBox taskBox = new TaskBox(task, this);
            taskList.getChildren().add(taskBox.getContainer());
        }
    }

    /**
     * Fills the user information into the corresponding labels in the GUI.
     */
    private void fillUserInfo() {
        usernameLabel.setText(Session.getActiveUser().getUsername());
        emailLabel.setText(Session.getActiveUser().getEmail());
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
                    TaskBox taskBox = new TaskBox(task, this);
                    taskList.getChildren().add(taskBox.getContainer());
                }
            } else if (filterBox.getValue().toString().equals("Priority (Low - High)")) {
                ArrayList<Task> prioritySorted = Session.getActiveUser().getTaskList().sortByPriority();
                Collections.reverse(prioritySorted);
                for (Task task : prioritySorted) {
                    TaskBox taskBox = new TaskBox(task, this);
                    taskList.getChildren().add(taskBox.getContainer());
                }
            } else if (filterBox.getValue().toString().equals("Category (A - Z)")) {
                for (Task task : Session.getActiveUser().getTaskList().sortByCategory()) {
                    TaskBox taskBox = new TaskBox(task, this);
                    taskList.getChildren().add(taskBox.getContainer());
                }
            } else if (filterBox.getValue().toString().equals("Category (Z - A)")) {
                ArrayList<Task> categorySorted = Session.getActiveUser().getTaskList().sortByCategory();
                Collections.reverse(categorySorted);
                for (Task task : categorySorted) {
                    TaskBox taskBox = new TaskBox(task, this);
                    taskList.getChildren().add(taskBox.getContainer());
                }
            } else if (filterBox.getValue().toString().equals("Name (A - Z)")) {
                for (Task task : Session.getActiveUser().getTaskList().sortByName()) {
                    TaskBox taskBox = new TaskBox(task, this);
                    taskList.getChildren().add(taskBox.getContainer());
                }
            } else if (filterBox.getValue().toString().equals("Name (Z - A)")) {
                ArrayList<Task> nameSorted = Session.getActiveUser().getTaskList().sortByName();
                Collections.reverse(nameSorted);
                for (Task task : nameSorted) {
                    TaskBox taskBox = new TaskBox(task, this);
                    taskList.getChildren().add(taskBox.getContainer());
                }
            }

        }
    }

}
