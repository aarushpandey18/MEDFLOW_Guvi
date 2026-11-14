package com.medflow.dao;

import com.medflow.exception.DatabaseException;
import com.medflow.model.Appointment;
import com.medflow.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDAO implements GenericDAO<Appointment, Integer> {

    @Override
    public int save(Appointment a) throws DatabaseException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_time, status) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(a.getTime()));
            ps.setString(4, a.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not save appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appointment> findById(Integer id) throws DatabaseException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM appointments WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not read appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Appointment> findAll() throws DatabaseException {
        List<Appointment> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM appointments ORDER BY appointment_time")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Could not list appointments: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM appointments WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete appointment: " + e.getMessage(), e);
        }
    }

    /** True if the doctor already has an appointment at this exact time. */
    public boolean slotTaken(int doctorId, LocalDateTime time) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_time = ? AND status <> 'CANCELLED'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setTimestamp(2, Timestamp.valueOf(time));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not check slot: " + e.getMessage(), e);
        }
    }

    private Appointment map(ResultSet rs) throws SQLException {
        return new Appointment(rs.getInt("id"), rs.getInt("patient_id"), rs.getInt("doctor_id"),
                rs.getTimestamp("appointment_time").toLocalDateTime(), rs.getString("status"));
    }
}
