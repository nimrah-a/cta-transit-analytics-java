package com.ctaanalytics.model;

public class Rider {
    private final int id;
    private String name;
    private String email;

    public Rider(int id, String name, String email) {
        this.id = id;
        setName(name);
        setEmail(email);
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public void setName(String name) { this.name = require(name, "Rider name"); }
    public void setEmail(String email) {
        String value = require(email, "Email");
        if (!value.contains("@") || !value.contains(".")) throw new IllegalArgumentException("Enter a valid email address.");
        this.email = value;
    }
    private static String require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " cannot be blank.");
        return value.trim();
    }
    @Override public String toString() { return String.format("#%d | %s | %s", id, name, email); }
}
