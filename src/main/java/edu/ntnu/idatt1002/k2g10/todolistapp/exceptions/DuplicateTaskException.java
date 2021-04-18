package edu.ntnu.idatt1002.k2g10.todolistapp.exceptions;

import edu.ntnu.idatt1002.k2g10.todolistapp.models.Task;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.TaskList;

/**
 * A DuplicateTaskException is thrown when a {@link Task} already exists in a {@link TaskList}
 */
public class DuplicateTaskException extends Exception {
    public DuplicateTaskException() {
    }

    public DuplicateTaskException(String errormessage) {
        super(errormessage);
    }
}
