package edu.ntnu.idatt1002.k2g10.todolistapp.exceptions;

/**
 * Exception thrown to indicate a given password does not match the record.
 *
 * @author trthingnes
 */
public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException() {
        super();
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectPasswordException(Throwable cause) {
        super(cause);
    }
}
