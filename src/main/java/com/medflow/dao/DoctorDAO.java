package com.medflow.dao;

import com.medflow.exception.DatabaseException;
import com.medflow.model.Doctor;
import com.medflow.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDAO implements GenericDAO<Doctor, Integer> {

    @Override
    public int save(Doctor d) throws DatabaseException {
        String sql = "INSERT INTO doctors (name, email, specialization) VALUES (?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getSpecialization());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not save doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Doctor> findById(Integer id) throws DatabaseException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM doctors WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not read doctor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Doctor> findAll() throws DatabaseException {
        List<Doctor> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM doctors ORDER BY name")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Could not list doctors: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM doctors WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete doctor: " + e.getMessage(), e);
        }
    }

    private Doctor map(ResultSet rs) throws SQLException {
        return new Doctor(rs.getInt("id"), rs.getString("name"),
                          rs.getString("email"), rs.getString("specialization"));
    }
}
