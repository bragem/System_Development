package edu.ntnu.idatt1002.k2g10.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Comparator;
import java.util.stream.Collectors;

import edu.ntnu.idatt1002.k2g10.exceptions.DuplicateTaskException;

/**
 * TaskList is contained in {@link User}
 * 
 * @author hasanro, trthingnes, bragemi, andetel
 */
public class TaskList implements Serializable {
    private String name;
    private final ArrayList<Task> tasks;
    private final HashSet<Category> categories;

    /**
     * Constructor of TaskList class
     */
    public TaskList() {
        this.name = "Task list";
        this.tasks = new ArrayList<>();
        this.categories = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public HashSet<Category> getCategories() {
        return categories;
    }

    /**
     * This method adds a new Category to the category-hashset
     * 
     * @author hasanro
     */
    public boolean addCategory(Category category) {
        return categories.add(category);
    }

    /**
     * This method removes a specific category
     * 
     * @param category
     *            Category to remove
     * 
     * @author hasanro
     */
    public boolean removeCategory(Category category) {
        return categories.remove(category);
    }

    /**
     * Method that adds a task to the tasklist. If the task already exists a {@link DuplicateTaskException} is thrown
     * 
     * @param newTask
     *            Task to be added
     * 
     * @throws DuplicateTaskException
     *             Exception thrown if object variable already exists
     * 
     * @author bragemi
     */
    public void addTask(Task newTask) throws DuplicateTaskException {
        if (tasks.stream().anyMatch(someTask -> someTask.getTitle().equals(newTask.getTitle()))) {
            throw new DuplicateTaskException("Task already exists");
        } else {
            tasks.add(newTask);
        }

    }

    /**
     * Method sets a task as completed
     * 
     * @param task
     *            task object
     */
    public void setCompleted(Task task) {
        int index = tasks.indexOf(task);
        tasks.get(index).setCompleted();
    }

    /**
     * Sets task as not completed
     * 
     * @param task
     *            task object
     */
    public void setNotCompleted(Task task) {
        int index = tasks.indexOf(task);
        tasks.get(index).setNotCompleted();
    }

    /**
     * Method for sorting {@link Task} objects in {@link TaskList} by date
     * 
     * @return A sorted ArrayList
     * 
     * @author bragemi, hasanro
     */
    public ArrayList<Task> sortByEndDate() {
        ArrayList<Task> sortedList = new ArrayList<>(tasks);

        sortedList.sort(Comparator.comparing(Task::getEndTime));
        return sortedList;
    }

    /**
     * Method for sorting {@link Task} objects in {@link TaskList} by category
     * 
     * @param category
     *            category
     * 
     * @return A sorted ArrayList
     * 
     * @author bragemi, hasanro
     */
    public ArrayList<Task> sortByCategory(Category category) {
        ArrayList<Task> sortedList;
        sortedList = (ArrayList<Task>) tasks.stream().filter(task -> task.getCategory() == category)
                .collect(Collectors.toList());
        return sortedList;
    }

    /**
     * Method for sorting {@link Task} objects in {@link TaskList} by Priority from high to low
     * 
     * @return a sorted Arraylist
     * 
     * @author bragemi, hasanro
     */
    public ArrayList<Task> sortByPriority() {
        ArrayList<Task> sortedList = new ArrayList<>(tasks);

        sortedList.sort((task1, task2) -> {
            if (task1.getPriority().ordinal() > task2.getPriority().ordinal()) {
                return -1;
            } else if (task1.getPriority().ordinal() == task2.getPriority().ordinal()) {
                return 0;
            } else {
                return 1;
            }
        });

        return sortedList;
    }

}
