package com.ctaanalytics.ui;

import com.ctaanalytics.model.*;
import com.ctaanalytics.service.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ConsoleApp {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Scanner input = new Scanner(System.in);
    private final TransitService service;
    private final AnalyticsService analytics;

    public ConsoleApp(TransitService service) {
        this.service = Objects.requireNonNull(service);
        this.analytics = new AnalyticsService(service);
    }

    public void run() {
        System.out.println("CTA Transit Analytics System | Demo data loaded");
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Riders   2. Routes   3. Stations   4. Trips");
            System.out.println("5. Search trips   6. Analytics   0. Exit");
            int choice = readInt("Choose: ");
            if (choice == 0) { System.out.println("Goodbye!"); return; }
            try {
                switch (choice) {
                    case 1 -> manageRiders();
                    case 2 -> manageRoutes();
                    case 3 -> manageStations();
                    case 4 -> manageTrips();
                    case 5 -> searchTrips();
                    case 6 -> showAnalytics();
                    default -> System.out.println("Please choose a listed option.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void manageRiders() {
        switch (crudChoice("RIDERS")) {
            case 1 -> printAll(service.riders());
            case 2 -> System.out.println("Added: " + service.addRider(readText("Name: "), readText("Email: ")));
            case 3 -> {
                int id = readInt("Rider ID: "); service.rider(id);
                service.updateRider(id, readText("New name: "), readText("New email: "));
                System.out.println("Rider updated.");
            }
            case 4 -> { service.deleteRider(readInt("Rider ID: ")); System.out.println("Rider deleted."); }
            default -> { }
        }
    }
    private void manageRoutes() {
        switch (crudChoice("ROUTES")) {
            case 1 -> printAll(service.routes());
            case 2 -> System.out.println("Added: " + service.addRoute(readText("Route name: "), readText("Mode (Rail/Bus): ")));
            case 3 -> {
                int id = readInt("Route ID: "); service.route(id);
                service.updateRoute(id, readText("New name: "), readText("New mode: "));
                System.out.println("Route updated.");
            }
            case 4 -> { service.deleteRoute(readInt("Route ID: ")); System.out.println("Route deleted."); }
            default -> { }
        }
    }
    private void manageStations() {
        switch (crudChoice("STATIONS")) {
            case 1 -> printAll(service.stations());
            case 2 -> System.out.println("Added: " + service.addStation(readText("Station name: "), readText("Neighborhood: ")));
            case 3 -> {
                int id = readInt("Station ID: "); service.station(id);
                service.updateStation(id, readText("New name: "), readText("New neighborhood: "));
                System.out.println("Station updated.");
            }
            case 4 -> { service.deleteStation(readInt("Station ID: ")); System.out.println("Station deleted."); }
            default -> { }
        }
    }
    private void manageTrips() {
        switch (crudChoice("TRIPS")) {
            case 1 -> printTrips(service.trips());
            case 2 -> { printTripReferences(); Trip trip = readTripAndAdd(); System.out.println("Trip added: #" + trip.getId()); }
            case 3 -> {
                int id = readInt("Trip ID: "); service.trip(id); printTripReferences();
                service.updateTrip(id, readInt("Rider ID: "), readInt("Route ID: "),
                    readInt("Origin station ID: "), readInt("Destination station ID: "),
                    readDate(), readFare());
                System.out.println("Trip updated.");
            }
            case 4 -> { service.deleteTrip(readInt("Trip ID: ")); System.out.println("Trip deleted."); }
            default -> { }
        }
    }
    private Trip readTripAndAdd() {
        return service.addTrip(readInt("Rider ID: "), readInt("Route ID: "),
            readInt("Origin station ID: "), readInt("Destination station ID: "), readDate(), readFare());
    }
    private void printTripReferences() {
        System.out.println("Riders:"); printAll(service.riders());
        System.out.println("Routes:"); printAll(service.routes());
        System.out.println("Stations:"); printAll(service.stations());
    }
    private void searchTrips() {
        System.out.println("1. By route  2. By station");
        int type = readInt("Choose: ");
        if (type == 1) { printAll(service.routes()); printTrips(service.searchTripsByRoute(readInt("Route ID: "))); }
        else if (type == 2) { printAll(service.stations()); printTrips(service.searchTripsByStation(readInt("Station ID: "))); }
        else System.out.println("Invalid search option.");
    }
    private void showAnalytics() {
        System.out.println("\n--- REVENUE BY ROUTE (sample fare values) ---");
        analytics.revenueByRoute().forEach((id, total) ->
            System.out.printf("%-20s $%s%n", service.route(id).getName(), total.setScale(2)));
        System.out.println("\n--- POPULAR STATIONS (origin + destination appearances) ---");
        analytics.stationPopularity().entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
            .forEach(e -> System.out.printf("%-20s %d%n", service.station(e.getKey()).getName(), e.getValue()));
        System.out.println("\n--- TRAVEL PERIODS ---");
        System.out.printf("Peak trips: %d | Off-peak trips: %d%n", analytics.peakTrips(), analytics.offPeakTrips());
        System.out.println("Peak = Mon-Fri 07:00-09:00 or 16:00-18:00 (local timestamps).");
    }
    private void printTrips(Collection<Trip> trips) {
        if (trips.isEmpty()) { System.out.println("No trips found."); return; }
        for (Trip t : trips) {
            System.out.printf("#%d | %s | %s | %s -> %s | %s | $%s%n", t.getId(),
                service.rider(t.getRiderId()).getName(), service.route(t.getRouteId()).getName(),
                service.station(t.getOriginStationId()).getName(), service.station(t.getDestinationStationId()).getName(),
                t.getDeparture().format(DATE_FORMAT), t.getFare().setScale(2));
        }
    }
    private void printAll(Collection<?> entries) {
        if (entries.isEmpty()) System.out.println("No records found.");
        else entries.forEach(System.out::println);
    }
    private int crudChoice(String name) {
        System.out.println("\n--- " + name + " ---");
        System.out.println("1. View all  2. Add  3. Update  4. Delete  0. Back");
        return readInt("Choose: ");
    }
    private String readText(String label) {
        System.out.print(label);
        if (!input.hasNextLine()) { System.out.println("\nInput ended."); System.exit(0); }
        return input.nextLine().trim();
    }
    private int readInt(String label) {
        while (true) {
            try { return Integer.parseInt(readText(label)); }
            catch (NumberFormatException ex) { System.out.println("Please enter a whole number."); }
        }
    }
    private LocalDateTime readDate() {
        while (true) {
            try { return LocalDateTime.parse(readText("Departure (yyyy-MM-dd HH:mm): "), DATE_FORMAT); }
            catch (DateTimeParseException ex) { System.out.println("Use format yyyy-MM-dd HH:mm, e.g. 2026-11-04 08:30."); }
        }
    }
    private BigDecimal readFare() {
        while (true) {
            try {
                BigDecimal value = new BigDecimal(readText("Fare (USD): "));
                if (value.signum() >= 0 && value.scale() <= 2) return value;
            } catch (NumberFormatException ex) { /* retry */ }
            System.out.println("Enter a nonnegative amount with at most two decimal places.");
        }
    }
}
