package com.ctaanalytics.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Trip {
    private final int id;
    private final int riderId;
    private final int routeId;
    private final int originStationId;
    private final int destinationStationId;
    private final LocalDateTime departure;
    private final BigDecimal fare;

    public Trip(int id, int riderId, int routeId, int originStationId, int destinationStationId,
                LocalDateTime departure, BigDecimal fare) {
        if (originStationId == destinationStationId) throw new IllegalArgumentException("Origin and destination must differ.");
        if (departure == null) throw new IllegalArgumentException("Departure is required.");
        if (fare == null || fare.signum() < 0 || fare.scale() > 2) throw new IllegalArgumentException("Fare must be nonnegative with at most two decimals.");
        this.id = id;
        this.riderId = riderId;
        this.routeId = routeId;
        this.originStationId = originStationId;
        this.destinationStationId = destinationStationId;
        this.departure = departure;
        this.fare = fare;
    }
    public int getId() { return id; }
    public int getRiderId() { return riderId; }
    public int getRouteId() { return routeId; }
    public int getOriginStationId() { return originStationId; }
    public int getDestinationStationId() { return destinationStationId; }
    public LocalDateTime getDeparture() { return departure; }
    public BigDecimal getFare() { return fare; }
}
