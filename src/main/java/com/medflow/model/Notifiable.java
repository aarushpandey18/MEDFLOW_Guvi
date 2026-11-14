package com.medflow.model;

/** Interface: anyone who can receive a notification. */
public interface Notifiable {
    String notifyUser(String message);
}
