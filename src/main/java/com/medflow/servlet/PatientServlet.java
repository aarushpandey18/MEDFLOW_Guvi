package com.medflow.servlet;

import com.medflow.dao.PatientDAO;
import com.medflow.exception.DatabaseException;
import com.medflow.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private final PatientDAO dao = new PatientDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("patients", dao.findAll());
        } catch (DatabaseException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/patients.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                dao.delete(Integer.parseInt(req.getParameter("id")));
            } else {
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                String ageText = req.getParameter("age");
                String phone = req.getParameter("phone");
                if (name == null || name.isBlank()
                        || email == null || email.isBlank()
                        || ageText == null || ageText.isBlank()) {
                    req.setAttribute("error", "Name, email, and age are required.");
                    doGet(req, resp);
                    return;
                }
                int age = Integer.parseInt(ageText);
                if (age < 0 || age > 120) {
                    req.setAttribute("error", "Age must be between 0 and 120.");
                    doGet(req, resp);
                    return;
                }
                Patient p = new Patient(0,
                        name.trim(),
                        email.trim(),
                        age,
                        phone == null ? "" : phone.trim());
                dao.save(p);
            }
            resp.sendRedirect(req.getContextPath() + "/patients");
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Age must be a number.");
            doGet(req, resp);
        } catch (DatabaseException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
