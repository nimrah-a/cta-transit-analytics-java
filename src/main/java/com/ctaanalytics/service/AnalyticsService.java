package com.ctaanalytics.service;

import com.ctaanalytics.model.Trip;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

/** Peak periods: weekdays 07:00-09:00 and 16:00-18:00, start inclusive/end exclusive. */
public class AnalyticsService {
    private final TransitService transit;
    public AnalyticsService(TransitService transit) { this.transit = Objects.requireNonNull(transit); }

    public Map<Integer, BigDecimal> revenueByRoute() {
        Map<Integer, BigDecimal> revenue = new LinkedHashMap<>();
        transit.routes().forEach(r -> revenue.put(r.getId(), BigDecimal.ZERO));
        for (Trip trip : transit.trips())
            revenue.merge(trip.getRouteId(), trip.getFare(), BigDecimal::add);
        return revenue;
    }
    /** Counts station appearances at both the origin and the destination of each trip. */
    public Map<Integer, Long> stationPopularity() {
        Map<Integer, Long> counts = new LinkedHashMap<>();
        transit.stations().forEach(s -> counts.put(s.getId(), 0L));
        for (Trip trip : transit.trips()) {
            counts.merge(trip.getOriginStationId(), 1L, Long::sum);
            counts.merge(trip.getDestinationStationId(), 1L, Long::sum);
        }
        return counts;
    }
    public boolean isPeak(Trip trip) {
        int day = trip.getDeparture().getDayOfWeek().getValue();
        if (day > 5) return false;
        LocalTime time = trip.getDeparture().toLocalTime();
        return inRange(time, LocalTime.of(7,0), LocalTime.of(9,0)) ||
               inRange(time, LocalTime.of(16,0), LocalTime.of(18,0));
    }
    private boolean inRange(LocalTime value, LocalTime start, LocalTime end) {
        return !value.isBefore(start) && value.isBefore(end);
    }
    public long peakTrips() { return transit.trips().stream().filter(this::isPeak).count(); }
    public long offPeakTrips() { return transit.trips().size() - peakTrips(); }
}
