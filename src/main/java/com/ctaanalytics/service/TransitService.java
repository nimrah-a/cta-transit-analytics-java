package com.ctaanalytics.service;

import com.ctaanalytics.model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/** In-memory CRUD repository with referential-integrity checks. */
public class TransitService {
    private final Map<Integer, Rider> riders = new LinkedHashMap<>();
    private final Map<Integer, Route> routes = new LinkedHashMap<>();
    private final Map<Integer, Station> stations = new LinkedHashMap<>();
    private final Map<Integer, Trip> trips = new LinkedHashMap<>();
    private int riderSequence = 1, routeSequence = 1, stationSequence = 1, tripSequence = 1;

    public Collection<Rider> riders() { return Collections.unmodifiableCollection(riders.values()); }
    public Collection<Route> routes() { return Collections.unmodifiableCollection(routes.values()); }
    public Collection<Station> stations() { return Collections.unmodifiableCollection(stations.values()); }
    public Collection<Trip> trips() { return Collections.unmodifiableCollection(trips.values()); }

    public Rider rider(int id) { return mustFind(riders, id, "Rider"); }
    public Route route(int id) { return mustFind(routes, id, "Route"); }
    public Station station(int id) { return mustFind(stations, id, "Station"); }
    public Trip trip(int id) { return mustFind(trips, id, "Trip"); }

    public Rider addRider(String name, String email) {
        Rider value = new Rider(riderSequence, name, email);
        riders.put(riderSequence++, value);
        return value;
    }
    public Route addRoute(String name, String mode) {
        Route value = new Route(routeSequence, name, mode);
        routes.put(routeSequence++, value);
        return value;
    }
    public Station addStation(String name, String neighborhood) {
        Station value = new Station(stationSequence, name, neighborhood);
        stations.put(stationSequence++, value);
        return value;
    }
    public Trip addTrip(int riderId, int routeId, int originId, int destinationId, LocalDateTime departure, BigDecimal fare) {
        rider(riderId); route(routeId); station(originId); station(destinationId);
        Trip value = new Trip(tripSequence, riderId, routeId, originId, destinationId, departure, fare);
        trips.put(tripSequence++, value);
        return value;
    }
    public void updateRider(int id, String name, String email) {
        Rider previous = rider(id);
        Rider replacement = new Rider(id, name, email); // validate both fields first
        previous.setName(replacement.getName()); previous.setEmail(replacement.getEmail());
    }
    public void updateRoute(int id, String name, String mode) {
        Route previous = route(id);
        Route replacement = new Route(id, name, mode);
        previous.setName(replacement.getName()); previous.setMode(replacement.getMode());
    }
    public void updateStation(int id, String name, String neighborhood) {
        Station previous = station(id);
        Station replacement = new Station(id, name, neighborhood);
        previous.setName(replacement.getName()); previous.setNeighborhood(replacement.getNeighborhood());
    }
    public void updateTrip(int id, int riderId, int routeId, int originId, int destinationId, LocalDateTime departure, BigDecimal fare) {
        trip(id); rider(riderId); route(routeId); station(originId); station(destinationId);
        trips.put(id, new Trip(id, riderId, routeId, originId, destinationId, departure, fare));
    }
    public void deleteRider(int id) {
        rider(id);
        if (trips.values().stream().anyMatch(t -> t.getRiderId() == id)) throw new IllegalArgumentException("Cannot delete rider referenced by trips. Delete those trips first.");
        riders.remove(id);
    }
    public void deleteRoute(int id) {
        route(id);
        if (trips.values().stream().anyMatch(t -> t.getRouteId() == id)) throw new IllegalArgumentException("Cannot delete route referenced by trips. Delete those trips first.");
        routes.remove(id);
    }
    public void deleteStation(int id) {
        station(id);
        if (trips.values().stream().anyMatch(t -> t.getOriginStationId() == id || t.getDestinationStationId() == id))
            throw new IllegalArgumentException("Cannot delete station referenced by trips. Delete those trips first.");
        stations.remove(id);
    }
    public void deleteTrip(int id) { trip(id); trips.remove(id); }

    public List<Trip> searchTripsByRoute(int routeId) {
        route(routeId);
        return trips.values().stream().filter(t -> t.getRouteId() == routeId).toList();
    }
    public List<Trip> searchTripsByStation(int stationId) {
        station(stationId);
        return trips.values().stream().filter(t -> t.getOriginStationId() == stationId || t.getDestinationStationId() == stationId).toList();
    }
    private static <T> T mustFind(Map<Integer, T> values, int id, String type) {
        T result = values.get(id);
        if (result == null) throw new IllegalArgumentException(type + " #" + id + " not found.");
        return result;
    }
}
