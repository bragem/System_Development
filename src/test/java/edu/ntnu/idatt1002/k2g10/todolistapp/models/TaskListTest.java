package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    @Test
    @DisplayName("Task is completed after being marked as completed")
    void setTaskAsCompleted() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        tasks.getTasks().get(0).setCompleted(true);

        assertTrue(tasks.getTasks().get(0).getCompleted());
    }

    @Test
    @DisplayName("Task is not completed after being marked as not completed")
    void setTaskAsNotCompleted() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        tasks.getTasks().get(0).setCompleted(true);
        tasks.getTasks().get(0).setCompleted(false);

        assertFalse(tasks.getTasks().get(0).getCompleted());
    }

    @Test
    @DisplayName("Tasks sorted by category is sorted correctly")
    void sortTasksByCategory() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());
        Category category = tasks.getTasks().get(0).getCategory();

        ArrayList<Task> sortedList = tasks.filterByCategory(category);

        assertEquals(tasks.getTasks().get(0), sortedList.get(0));
    }

    @Test
    @DisplayName("Tasks sorted by priority is sorted correctly")
    void sortTasksByPriority() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        ArrayList<Task> sortedList = tasks.sortByPriority();

        assertEquals(tasks.getTasks().get(1).getTitle(), sortedList.get(0).getTitle());
    }

    @Test
    @DisplayName("Tasks sorted by date is sorted correctly")
    void sortTasksByDate() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        ArrayList<Task> sortedList = tasks.sortByEndDate();

        assertEquals(tasks.getTasks().get(4).getTitle(), sortedList.get(0).getTitle());
    }

    @Test
    @DisplayName("Tasks sorted A-Z by title is sorted correctly")
    void sortTasksAlphabetically() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        Task taskA = tasks.getTasks().get(0);
        ArrayList<Task> list = tasks.sortByName();

        assertEquals(taskA, list.get(0));
    }

    @Test
    @DisplayName("Tasks sorted A-Z by category title is sorted correctly")
    void sortTasksAlphabeticallyByCategory() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        Task taskA = tasks.getTasks().get(0);
        ArrayList<Task> list = tasks.sortByCategory();

        assertEquals(taskA, list.get(0));
    }
}