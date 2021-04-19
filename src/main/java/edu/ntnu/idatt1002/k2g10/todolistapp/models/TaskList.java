package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import edu.ntnu.idatt1002.k2g10.todolistapp.exceptions.DuplicateTaskException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * TaskList is contained in {@link User}
 * 
 * @author hasanro, trthingnes, bragemi, andetel
 */
@Entity
public class TaskList implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToMany
    private final List<Task> tasks;

    @NotNull
    @OneToMany
    private final List<Category> categories;

    /**
     * Constructor of TaskList class
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Category> getCategories() {
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
        tasks.get(index).setCompleted(true);
    }

    /**
     * Sets task as not completed
     *
     * @param task
     *            task object
     */
    public void setNotCompleted(Task task) {
        int index = tasks.indexOf(task);
        tasks.get(index).setCompleted(false);
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
     * Method for sorting {@link Task} objects in {@link TaskList} alphabetically
     *
     * @return a sorted ArrayList
     *
     * @author andetel
     */
    public ArrayList<Task> sortByName() {
        ArrayList<Task> sortedList;
        sortedList = (ArrayList<Task>) tasks.stream().sorted(Comparator.comparing(Task::getTitle))
                .collect(Collectors.toList());
        return sortedList;
    }

    /**
     * Method for sorting {@link Task} objects in {@link TaskList} by category alphabetically
     *
     * @return a sorted ArrayList
     *
     * @author andetel, bragemi, trthingnes
     */
    public ArrayList<Task> sortByCategory() {
        ArrayList<Task> sortedList;
        sortedList = (ArrayList<Task>) tasks.stream().sorted(Comparator.comparing(task -> {
            if (Objects.isNull(task.getCategory())) {
                // tasks without a category will be put at the end of the list,
                // by returning a "~" which has the highest ASCII-value before delete
                return "~";
            }
            return task.getCategory().getTitle();
        })).collect(Collectors.toList());
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
    public ArrayList<Task> filterByCategory(Category category) {
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

        sortedList.sort((task1, task2) ->
                Integer.compare(task2.getPriority().ordinal(), task1.getPriority().ordinal())
        );

        return sortedList;
    }
}
