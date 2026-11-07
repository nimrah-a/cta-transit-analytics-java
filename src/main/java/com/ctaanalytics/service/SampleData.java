package com.ctaanalytics.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Fictional riders/trips for demonstration, not official CTA data or fare information. */
public final class SampleData {
    private SampleData() {}
    public static TransitService create() {
        TransitService service = new TransitService();
        int alex = service.addRider("Alex Morgan", "alex@example.com").getId();
        int sam = service.addRider("Sam Rivera", "sam@example.com").getId();
        int jordan = service.addRider("Jordan Lee", "jordan@example.com").getId();
        int red = service.addRoute("Red Line", "Rail").getId();
        int blue = service.addRoute("Blue Line", "Rail").getId();
        int brown = service.addRoute("Brown Line", "Rail").getId();
        int fullerton = service.addStation("Fullerton", "Lincoln Park").getId();
        int lake = service.addStation("Lake", "Loop").getId();
        int ohare = service.addStation("O'Hare", "O'Hare").getId();
        int clarkLake = service.addStation("Clark/Lake", "Loop").getId();
        int belmont = service.addStation("Belmont", "Lakeview").getId();
        service.addTrip(alex, red, fullerton, lake, LocalDateTime.of(2026, 11, 2, 8, 15), new BigDecimal("2.50"));
        service.addTrip(sam, blue, ohare, clarkLake, LocalDateTime.of(2026, 11, 2, 13, 30), new BigDecimal("3.00"));
        service.addTrip(jordan, red, belmont, lake, LocalDateTime.of(2026, 11, 3, 17, 10), new BigDecimal("2.50"));
        service.addTrip(alex, brown, belmont, clarkLake, LocalDateTime.of(2026, 11, 7, 11, 0), new BigDecimal("2.50"));
        service.addTrip(sam, red, lake, fullerton, LocalDateTime.of(2026, 11, 4, 18, 15), new BigDecimal("2.50"));
        return service;
    }
}
