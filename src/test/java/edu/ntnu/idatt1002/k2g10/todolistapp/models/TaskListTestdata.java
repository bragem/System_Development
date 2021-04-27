package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class TaskListTestdata {
    private TaskListTestdata() {
        // not called
    }

    public static TaskList fillTasklistWithData(TaskList tasks) {
        Category testC1 = new Category("a1", '\uf17b');
        Category testC2 = new Category("c2", '\uf17b');
        Category testC3 = new Category("c3", '\uf17b');

        tasks.getCategories().add(testC1);
        tasks.getCategories().add(testC2);
        tasks.getCategories().add(testC3);

        LocalDate today = LocalDate.now();

        Task task1 = new Task("aaaTask1", "test task", today, today.plusDays(2), Priority.NONE, testC1);
        Task task2 = new Task("task2", "test task", today, today.plusDays(3), Priority.HIGH, testC2);
        Task task3 = new Task("task3", "test task", today, today.plusDays(5), Priority.LOW, testC3);
        Task task4 = new Task("task4", "test task", today, today.plusDays(9), Priority.MEDIUM);
        Task task5 = new Task("task5", "test task", today, today.plusDays(1), Priority.NONE);
        tasks.getTasks().add(task1);
        tasks.getTasks().add(task2);
        tasks.getTasks().add(task3);
        tasks.getTasks().add(task4);
        tasks.getTasks().add(task5);

        return tasks;
    }
}
