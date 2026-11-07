package com.ctaanalytics.model;

public class Route {
    private final int id;
    private String name;
    private String mode;

    public Route(int id, String name, String mode) {
        this.id = id;
        setName(name);
        setMode(mode);
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getMode() { return mode; }
    public void setName(String name) { this.name = require(name, "Route name"); }
    public void setMode(String mode) { this.mode = require(mode, "Mode"); }
    private static String require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " cannot be blank.");
        return value.trim();
    }
    @Override public String toString() { return String.format("#%d | %s (%s)", id, name, mode); }
}
