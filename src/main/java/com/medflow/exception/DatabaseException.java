package com.medflow.exception;

/** Checked exception wrapping all SQL errors (Exception Handling). */
public class DatabaseException extends Exception {
    public DatabaseException(String message, Throwable cause) { super(message, cause); }
}
