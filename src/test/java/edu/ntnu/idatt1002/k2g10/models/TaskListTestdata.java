package edu.ntnu.idatt1002.k2g10.models;

import edu.ntnu.idatt1002.k2g10.exceptions.DuplicateTaskException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class TaskListTestdata {
    private TaskListTestdata() {
        // not called
    }

    public static TaskList fillTasklistWithData(TaskList tasks) {
        Category testC1 = new Category("a1", '\uf17b', "#324ea8");
        Category testC2 = new Category("c2", '\uf17b', "#324ea8");
        Category testC3 = new Category("c3", '\uf17b', "#324ea8");

        tasks.addCategory(testC1);
        tasks.addCategory(testC2);
        tasks.addCategory(testC3);

        LocalDateTime today = LocalDateTime.now();

        Task task1 = new Task("aaaTask1", "test task", today, today.plus(4, ChronoUnit.DAYS), Priority.NONE, testC1);
        Task task2 = new Task("task2", "test task", today, today.plus(3, ChronoUnit.DAYS), Priority.HIGH, testC2);
        Task task3 = new Task("task3", "test task", today, today.plus(5, ChronoUnit.DAYS), Priority.LOW, testC3);
        Task task4 = new Task("task4", "test task", today, today.plus(9, ChronoUnit.DAYS), Priority.MEDIUM);
        Task task5 = new Task("task5", "test task", today, today.plus(2, ChronoUnit.DAYS), Priority.NONE);
        try {
            tasks.addTask(task1);
            tasks.addTask(task2);
            tasks.addTask(task3);
            tasks.addTask(task4);
            tasks.addTask(task5);
        } catch (DuplicateTaskException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
