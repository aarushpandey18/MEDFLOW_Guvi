package com.medflow.servlet;

import com.medflow.service.AppointmentService;
import com.medflow.service.ReminderService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Starts the reminder thread once when Tomcat starts the app, stops it on shutdown. */
@WebListener
public class AppListener implements ServletContextListener {
    private ReminderService reminders;

    @Override
    public void contextInitialized(ServletContextEvent e) {
        reminders = new ReminderService();
        reminders.start();
        e.getServletContext().setAttribute("reminders", reminders);
        e.getServletContext().setAttribute("appointmentService", new AppointmentService(reminders));
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        if (reminders != null) reminders.stop();
    }
}
