package com.ctaanalytics.model;

public class Station {
    private final int id;
    private String name;
    private String neighborhood;

    public Station(int id, String name, String neighborhood) {
        this.id = id;
        setName(name);
        setNeighborhood(neighborhood);
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getNeighborhood() { return neighborhood; }
    public void setName(String name) { this.name = require(name, "Station name"); }
    public void setNeighborhood(String neighborhood) { this.neighborhood = require(neighborhood, "Neighborhood"); }
    private static String require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " cannot be blank.");
        return value.trim();
    }
    @Override public String toString() { return String.format("#%d | %s | %s", id, name, neighborhood); }
}
