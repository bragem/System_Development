package edu.ntnu.idatt1002.k2g10.exceptions;

import edu.ntnu.idatt1002.k2g10.models.Task;
import edu.ntnu.idatt1002.k2g10.models.TaskList;

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
