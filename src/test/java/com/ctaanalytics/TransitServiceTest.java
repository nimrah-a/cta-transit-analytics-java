package com.ctaanalytics;

import com.ctaanalytics.service.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Dependency-free regression tests. Run with: java -ea -cp out com.ctaanalytics.TransitServiceTest */
public final class TransitServiceTest {
    private TransitServiceTest() {}
    public static void main(String[] args) {
        TransitService s = SampleData.create();
        AnalyticsService a = new AnalyticsService(s);
        assert s.riders().size() == 3;
        assert s.routes().size() == 3;
        assert s.stations().size() == 5;
        assert s.trips().size() == 5;
        assert s.searchTripsByRoute(1).size() == 3;
        assert s.searchTripsByStation(2).size() == 3;
        assert a.revenueByRoute().get(1).compareTo(new BigDecimal("7.50")) == 0;
        assert a.revenueByRoute().get(2).compareTo(new BigDecimal("3.00")) == 0;
        assert a.stationPopularity().get(2) == 3L;
        assert a.peakTrips() == 2;
        assert a.offPeakTrips() == 3;
        int rider = s.addRider("Taylor Green", "taylor@example.com").getId();
        s.updateRider(rider, "Taylor Smith", "smith@example.com");
        assert s.rider(rider).getName().equals("Taylor Smith");
        int route = s.addRoute("Demo Bus", "Bus").getId();
        s.updateRoute(route, "Demo Express", "Bus");
        assert s.route(route).getName().equals("Demo Express");
        int station = s.addStation("Demo Stop", "Loop").getId();
        s.updateStation(station, "Updated Stop", "Loop");
        assert s.station(station).getName().equals("Updated Stop");
        int trip = s.addTrip(rider, route, station, 1, LocalDateTime.of(2026,11,9,8,0), new BigDecimal("2.00")).getId();
        assert s.searchTripsByRoute(route).size() == 1;
        try { s.deleteRider(rider); throw new AssertionError("Expected integrity failure"); }
        catch (IllegalArgumentException expected) { assert expected.getMessage().contains("referenced"); }
        s.updateTrip(trip, rider, route, station, 2, LocalDateTime.of(2026,11,9,12,0), new BigDecimal("1.50"));
        assert s.trip(trip).getFare().compareTo(new BigDecimal("1.50")) == 0;
        assert s.searchTripsByStation(2).size() == 4;
        s.deleteTrip(trip); s.deleteRider(rider); s.deleteRoute(route); s.deleteStation(station);
        assert s.riders().size() == 3;
        boolean invalid = false;
        try { s.addTrip(999,1,1,2, LocalDateTime.now(), new BigDecimal("2.50")); }
        catch (IllegalArgumentException expected) { invalid = true; }
        assert invalid;
        System.out.println("All transit service tests passed.");
    }
}
