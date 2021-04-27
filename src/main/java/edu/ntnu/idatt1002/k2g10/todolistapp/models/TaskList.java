package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import edu.ntnu.idatt1002.k2g10.todolistapp.utils.icons.FontAwesomeIcon;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity contained in {@link User}.
 * 
 * @author hasanro, trthingnes, bragemi, andetel
 */
@Entity
public class TaskList implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToMany(cascade = { CascadeType.ALL })
    private final List<Task> tasks;

    @NotNull
    @OneToMany(cascade = { CascadeType.ALL })
    private final List<Category> categories;

    /**
     * Constructor of TaskList class
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.categories = new ArrayList<>();
        categories.add(new Category("Uncategorized", FontAwesomeIcon.QUESTION_CIRCLE.getChar()));
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

        sortedList
                .sort((task1, task2) -> Integer.compare(task2.getPriority().ordinal(), task1.getPriority().ordinal()));

        return sortedList;
    }
}
