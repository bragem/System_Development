package edu.ntnu.idatt1002.k2g10.controllers;

import edu.ntnu.idatt1002.k2g10.models.Task;
import edu.ntnu.idatt1002.k2g10.models.User;

import java.io.IOException;
import java.util.List;

/**
 * Interface for controllers that contain a task list. This interface is used as the type of the parent controller
 * element in {@link TaskBox} and {@link DetailTaskBox}. Therefore all controller classes that contain a task list or
 * detail view must implement this.
 *
 * @author trthingnes
 */
public interface TaskListController {
    /**
     * Shows the add task window as a popup.
     *
     * @throws IOException
     *             If {@link AddTask} popup fails to display.
     */
    void showAddTask() throws IOException;

    /**
     * Shows the add category window as a popup.
     *
     * @throws IOException
     *             If {@link AddCategory} popup fails to display.
     */
    void showAddCategory() throws IOException;

    /**
     * Fills the task list with instances of {@link TaskBox}.
     *
     * @param tasks
     *            The tasks to display in list.
     *
     * @throws IOException
     *             If {@link TaskBox} fails to load.
     */
    void fillTaskList(List<Task> tasks) throws IOException;

    /**
     * Shows a {@link DetailTaskBox} with the information for the given {@link Task}.
     *
     * @param task
     *            Task to display.
     *
     * @throws IOException
     *             If {@link DetailTaskBox} fails to load.
     */
    void showTaskDetails(Task task) throws IOException;

    /**
     * Updates the view. This action includes refreshing the {@link DetailTaskBox}, {@link TaskBox} list and
     * {@link User} info.
     *
     * @throws IOException
     *             If a problem is encountered while handling files.
     */
    void update() throws IOException;
}
