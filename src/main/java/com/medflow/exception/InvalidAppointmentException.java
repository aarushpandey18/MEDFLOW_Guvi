package com.medflow.exception;

/** Custom business-rule exception (past time, slot already taken, unknown doctor...). */
public class InvalidAppointmentException extends Exception {
    public InvalidAppointmentException(String message) { super(message); }
}
