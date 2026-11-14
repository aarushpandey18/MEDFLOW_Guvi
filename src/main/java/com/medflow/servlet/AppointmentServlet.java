package com.medflow.servlet;

import com.medflow.dao.DoctorDAO;
import com.medflow.dao.PatientDAO;
import com.medflow.exception.DatabaseException;
import com.medflow.exception.InvalidAppointmentException;
import com.medflow.service.AppointmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    private AppointmentService service() {
        return (AppointmentService) getServletContext().getAttribute("appointmentService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("appointments", service().allAppointments());
            req.setAttribute("doctors", doctorDAO.findAll());
            req.setAttribute("patients", patientDAO.findAll());
        } catch (DatabaseException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/appointments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int patientId = Integer.parseInt(req.getParameter("patientId"));
            int doctorId = Integer.parseInt(req.getParameter("doctorId"));
            LocalDateTime time = LocalDateTime.parse(req.getParameter("time")); // from datetime-local input
            service().book(patientId, doctorId, time);
            resp.sendRedirect(req.getContextPath() + "/appointments");
        } catch (InvalidAppointmentException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        } catch (NumberFormatException | DateTimeParseException e) {
            req.setAttribute("error", "Invalid input. Please check the form.");
            doGet(req, resp);
        } catch (DatabaseException e) {
            req.setAttribute("error", "Database problem: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
