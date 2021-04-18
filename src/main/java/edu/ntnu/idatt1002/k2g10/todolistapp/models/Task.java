package edu.ntnu.idatt1002.k2g10.todolistapp.models;

import javax.persistence.*;
import java.io.Serializable;

import java.time.LocalDate;

/**
 * Task is contained in {@link TaskList} and is associated with {@link Category}
 * 
 * @author hasanro, trthingnes, bragemi
 */
@Entity
public class Task implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String description;
    private LocalDate startTime;
    private LocalDate endTime;
    private Priority priority;
    @ManyToOne
    private Category category;
    private boolean completed = false;

    /**
     * Constructor of Task class
     * 
     * @param title
     *            Title of the task
     * @param description
     *            Description of a task
     * @param startTime
     *            Time start of the task
     * @param endTime
     *            Deadline of the task
     * @param priority
     *            Task priority
     * @param category
     *            Category of the task object
     */
    public Task(String title, String description, LocalDate startTime, LocalDate endTime, Priority priority,
            Category category) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.category = category;
    }

    /**
     * Constructor of Task class
     * 
     * @param title
     *            Title of the task
     * @param description
     *            Description of a task
     * @param startTime
     *            Time start of the task
     * @param endTime
     *            Deadline of the task
     * @param priority
     *            Task priority
     */
    public Task(String title, String description, LocalDate startTime, LocalDate endTime, Priority priority) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.category = null;
    }

    public Task() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public Category getCategory() {
        return category;
    }

    public void removeCategory(Category category) {
        category = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
