package com.medflow.model;

/** Abstract base class (Inheritance + Abstraction). */
public abstract class User implements Notifiable {
    protected int id;
    protected String name;
    protected String email;

    protected User(int id, String name, String email) {
        this.id = id; this.name = name; this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    /** Each subclass defines its own role (Polymorphism). */
    public abstract String getRole();

    @Override
    public String toString() { return getRole() + "[" + id + ", " + name + "]"; }
}
