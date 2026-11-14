package com.medflow.model;

public class Patient extends User {
    private int age;
    private String phone;

    public Patient(int id, String name, String email, int age, String phone) {
        super(id, name, email);
        this.age = age; this.phone = phone;
    }
    public int getAge() { return age; }
    public String getPhone() { return phone; }

    @Override public String getRole() { return "PATIENT"; }

    @Override public String notifyUser(String message) {
        return "SMS to " + phone + " (" + name + "): " + message;
    }
}
