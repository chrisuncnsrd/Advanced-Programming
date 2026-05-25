package Lecture4_interfaces_abstract_classes;

/**
 * Custom checked exception for insufficient-funds errors.
 * Extends Exception (not RuntimeException) making it a checked exception —
 * callers are forced to handle or declare it.
 */
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
