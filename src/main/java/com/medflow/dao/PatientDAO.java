package com.medflow.dao;

import com.medflow.exception.DatabaseException;
import com.medflow.model.Patient;
import com.medflow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDAO implements GenericDAO<Patient, Integer> {

    @Override
    public int save(Patient p) throws DatabaseException {
        String sql = "INSERT INTO patients (name, email, age, phone) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getEmail());
            ps.setInt(3, p.getAge());
            ps.setString(4, p.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException
                    || e.getErrorCode() == 1062) {
                throw new DatabaseException("A patient with this email already exists.", e);
            }
            throw new DatabaseException("Could not save patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Patient> findById(Integer id) throws DatabaseException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not read patient: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> findAll() throws DatabaseException {
        List<Patient> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM patients ORDER BY id DESC")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Could not list patients: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM patients WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete patient: " + e.getMessage(), e);
        }
    }

    private Patient map(ResultSet rs) throws SQLException {
        return new Patient(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                           rs.getInt("age"), rs.getString("phone"));
    }
}
