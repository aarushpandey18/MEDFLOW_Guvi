package com.medflow.model;

public class Doctor extends User {
    private String specialization;

    public Doctor(int id, String name, String email, String specialization) {
        super(id, name, email);
        this.specialization = specialization;
    }
    public String getSpecialization() { return specialization; }

    @Override public String getRole() { return "DOCTOR"; }

    @Override public String notifyUser(String message) {
        return "EMAIL to " + email + " (" + name + "): " + message;
    }
}
