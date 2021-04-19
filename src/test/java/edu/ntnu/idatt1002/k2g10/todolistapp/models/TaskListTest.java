package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

    @Test
    @DisplayName("Set tasks as completed")
    void setTaskAsCompleted() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        tasks.setCompleted(tasks.getTasks().get(0));
        assertTrue(tasks.getTasks().get(0).getCompleted());
    }

    @Test
    @DisplayName("Set tasks as not completed")
    void setTaskAsNotCompleted() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());

        tasks.setCompleted(tasks.getTasks().get(0));
        tasks.setNotCompleted(tasks.getTasks().get(0));
        assertFalse(tasks.getTasks().get(0).getCompleted());
    }

    @Test
    @DisplayName("Sort tasks by category")
    void sortTasksByCategory() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());
        Category category = tasks.getTasks().get(0).getCategory();
        ArrayList<Task> sortedList = tasks.filterByCategory(category);
        assertEquals(tasks.getTasks().get(0), sortedList.get(0));
    }

    @Test
    @DisplayName("Sort tasks by priority")
    void sortTasksByPriority() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());
        ArrayList<Task> sortedList = tasks.sortByPriority();
        assertEquals(tasks.getTasks().get(1).getTitle(), sortedList.get(0).getTitle());
    }

    @Test
    @DisplayName("Sort tasks by date")
    void sortTasksByDate() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());
        ArrayList<Task> sortedList = tasks.sortByEndDate();
        assertEquals(tasks.getTasks().get(4).getTitle(), sortedList.get(0).getTitle());
    }

    @Test
    @DisplayName("Adding a non-existent task when supposed to")
    void addingNonExistingTaskWhenSupposedTo() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());
        int sizeBefore = tasks.getTasks().size();
        Task newTask = new Task("tfe", "efef", tasks.getTasks().get(0).getStartTime(),
                tasks.getTasks().get(0).getEndTime(), tasks.getTasks().get(0).getPriority());
        tasks.addTask(newTask);

        int sizeAfter = tasks.getTasks().size();
        assertEquals(sizeBefore + 1, sizeAfter);

    }

    @Test
    @DisplayName("Sorts tasks alphabetically")
    void sortTasksAlphabetically() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());
        Task taskA = tasks.getTasks().get(0);
        ArrayList<Task> list = tasks.sortByName();
        assertEquals(taskA, list.get(0));
    }

    @Test
    @DisplayName("Sorts tasks alphabetically by category name")
    void sortTasksAlphabeticallyByCategory() {
        TaskList tasks = TaskListTestdata.fillTasklistWithData(new TaskList());
        Task taskA = tasks.getTasks().get(0);
        ArrayList<Task> list = tasks.sortByCategory();
        assertEquals(taskA, list.get(0));
    }
}