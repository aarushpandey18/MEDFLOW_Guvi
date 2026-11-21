package com.medflow.servlet;

import com.medflow.service.AppointmentService;
import com.medflow.service.ReminderService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.medflow.util.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Starts the reminder thread once when Tomcat starts the app, stops it on shutdown. */
@WebListener
public class AppListener implements ServletContextListener {
    private ReminderService reminders;

    @Override
    public void contextInitialized(ServletContextEvent e) {
        initializeDatabase(e);
        reminders = new ReminderService();
        reminders.start();
        e.getServletContext().setAttribute("reminders", reminders);
        e.getServletContext().setAttribute("appointmentService", new AppointmentService(reminders));
    }

    private void initializeDatabase(ServletContextEvent e) {
        String[] statements = {
            "CREATE TABLE IF NOT EXISTS patients ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "email VARCHAR(100) NOT NULL UNIQUE,"
                + "age INT NOT NULL,"
                + "phone VARCHAR(15))",
            "CREATE TABLE IF NOT EXISTS doctors ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "email VARCHAR(100) NOT NULL UNIQUE,"
                + "specialization VARCHAR(100) NOT NULL)",
            "CREATE TABLE IF NOT EXISTS appointments ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "patient_id INT NOT NULL,"
                + "doctor_id INT NOT NULL,"
                + "appointment_time DATETIME NOT NULL,"
                + "status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',"
                + "FOREIGN KEY (patient_id) REFERENCES patients(id),"
                + "FOREIGN KEY (doctor_id) REFERENCES doctors(id),"
                + "UNIQUE KEY uq_doctor_slot (doctor_id, appointment_time))",
            "INSERT IGNORE INTO doctors (name, email, specialization) VALUES "
                + "('Dr. Sharma','sharma@medflow.com','Cardiology'),"
                + "('Dr. Verma','verma@medflow.com','General Physician')"
        };

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        } catch (SQLException ex) {
            e.getServletContext().setAttribute("databaseError", ex.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        if (reminders != null) reminders.stop();
    }
}
