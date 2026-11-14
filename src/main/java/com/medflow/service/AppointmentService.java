package com.medflow.service;

import com.medflow.dao.AppointmentDAO;
import com.medflow.dao.DoctorDAO;
import com.medflow.dao.PatientDAO;
import com.medflow.exception.DatabaseException;
import com.medflow.exception.InvalidAppointmentException;
import com.medflow.model.Appointment;
import com.medflow.model.Doctor;
import com.medflow.model.Notifiable;
import com.medflow.model.Patient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/** Business logic layer: validation, synchronized booking, Collections/Generics usage. */
public class AppointmentService {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final ReminderService reminders;

    /** Cache of doctors: Map<Integer, Doctor> (Collections + Generics). */
    private final Map<Integer, Doctor> doctorCache = new HashMap<>();

    public AppointmentService(ReminderService reminders) {
        this.reminders = reminders;
    }

    /**
     * synchronized: two requests (threads) can never book the same doctor slot
     * at the same time inside one JVM. The UNIQUE key in MySQL protects across servers.
     */
    public synchronized Appointment book(int patientId, int doctorId, LocalDateTime time)
            throws InvalidAppointmentException, DatabaseException {

        if (time == null || time.isBefore(LocalDateTime.now()))
            throw new InvalidAppointmentException("Appointment time must be in the future.");

        Patient patient = patientDAO.findById(patientId)
                .orElseThrow(() -> new InvalidAppointmentException("Patient not found: " + patientId));
        Doctor doctor = getDoctor(doctorId)
                .orElseThrow(() -> new InvalidAppointmentException("Doctor not found: " + doctorId));

        if (appointmentDAO.slotTaken(doctorId, time))
            throw new InvalidAppointmentException(doctor.getName() + " is already booked at " + time);

        int id = appointmentDAO.save(new Appointment(0, patientId, doctorId, time, "BOOKED"));

        // Polymorphism: same call, different behaviour (SMS vs Email)
        List<Notifiable> targets = List.of(patient, doctor);
        for (Notifiable t : targets)
            reminders.schedule(t, "Appointment #" + id + " confirmed for " + time);

        return new Appointment(id, patientId, doctorId, time, "BOOKED");
    }

    public synchronized Optional<Doctor> getDoctor(int id) throws DatabaseException {
        if (doctorCache.containsKey(id)) return Optional.of(doctorCache.get(id));
        Optional<Doctor> d = doctorDAO.findById(id);
        d.ifPresent(doc -> doctorCache.put(id, doc));
        return d;
    }

    public List<Appointment> allAppointments() throws DatabaseException {
        return appointmentDAO.findAll();
    }

    /** Group appointments by doctor id using streams + Map<Integer, List<Appointment>>. */
    public Map<Integer, List<Appointment>> groupByDoctor() throws DatabaseException {
        return appointmentDAO.findAll().stream()
                .collect(Collectors.groupingBy(Appointment::getDoctorId));
    }
}
