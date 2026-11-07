# CTA Transit Analytics System

A **Java 17+ console application** for managing riders, routes, stations, and transit trips. The project demonstrates object-oriented programming, CRUD workflows, collection-backed repositories, input validation, referential integrity, and transit analytics.

> **Portfolio/educational project:** Not affiliated with or endorsed by the Chicago Transit Authority (CTA). The bundled riders, trips, and fare values are fictional examples; this is not live CTA data and fares are not official rates.

## Features

- **Riders:** Add, list, edit, and delete rider profiles.
- **Routes:** Manage route names and transportation modes.
- **Stations:** Manage station names and neighborhoods.
- **Trips:** Create, list, edit, and delete trips with a rider, route, origin, destination, departure, and fare.
- **Search:** Find trips by route or by origin/destination station.
- **Analytics:** Sum fare revenue by route, rank stations by appearances, and compare peak versus off-peak trips.
- **Validation:** Reject invalid IDs, blank fields, invalid dates/fares, and deletion of records referenced by trips.
- **Sample records:** Preloaded so you can explore the app immediately.

## Technologies

Java 17+, IntelliJ IDEA (optional), OOP, `LinkedHashMap` collections for efficient ID lookups, Java Collections and Streams, `Scanner`, `BigDecimal`, and `java.time`.

## Project structure

```text
cta-transit-analytics-system/
├── src/
│   ├── main/java/com/ctaanalytics/
│   │   ├── Main.java
│   │   ├── model/              Rider, Route, Station, Trip
│   │   ├── service/            TransitService, AnalyticsService, SampleData
│   │   └── ui/                 ConsoleApp
│   └── test/java/com/ctaanalytics/
│       └── TransitServiceTest.java
├── .github/workflows/         GitHub Actions build + tests
├── .gitignore
├── LICENSE
└── README.md
```

## Run in IntelliJ IDEA

1. Install **JDK 17 or newer** (JDK 21 is fine).
2. Open IntelliJ IDEA and select **File → Open**; choose the extracted project folder.
3. Set **Project SDK** to Java 17+ if prompted.
4. Open `src/main/java/com/ctaanalytics/Main.java` and click **Run** next to `main()`.
5. Enter a menu number and follow the prompts.

## Run from the terminal

At the project root, on macOS or Linux:

```bash
mkdir -p out
javac -d out $(find src/main/java -name '*.java')
java -cp out com.ctaanalytics.Main
```

On Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object FullName | Set-Content sources.txt
javac -d out '@sources.txt'
java -cp out com.ctaanalytics.Main
```

### Example menu

```text
CTA Transit Analytics System | Demo data loaded

=== MAIN MENU ===
1. Riders   2. Routes   3. Stations   4. Trips
5. Search trips   6. Analytics   0. Exit
Choose: 6

--- REVENUE BY ROUTE (sample fare values) ---
Red Line             $7.50
Blue Line            $3.00
Brown Line           $2.50
```

## Run tests

The project includes dependency-free regression tests for CRUD, trip searches, fare aggregation, station popularity, peak classification, and invalid-record protection.

```bash
mkdir -p out
javac -d out $(find src/main/java src/test/java -name '*.java')
java -ea -cp out com.ctaanalytics.TransitServiceTest
```

> On Windows, include both `src/main/java` and `src/test/java` when generating `sources.txt` with the PowerShell approach above.

## How analytics work

- **Route revenue:** Sum the stored `BigDecimal` fare on each trip, grouped by route ID.
- **Popular stations:** Count each trip's origin and destination once each, grouped by station ID; sort by descending count.
- **Peak travel:** Monday–Friday **07:00–09:00** and **16:00–18:00**; the starting time is included, ending time excluded. All other trips are off-peak. These are **project-defined analysis windows**, not official CTA classifications.

## Design notes & limitations

- All data lives **in memory**. Changes are reset when you exit/restart the program; there is no database or persistent storage.
- The program models a trip and its station references but **does not validate whether both stations are served by a specific CTA route**. It is a learning-oriented model, not a journey planner.
- Deleting a rider, route, or station referenced by a trip is blocked; delete dependent trips first.
- Trip fares are stored per trip and are not recalculated from official fare policy.
- Dates/times are entered as local timestamps (`yyyy-MM-dd HH:mm`) without time-zone handling.

## Potential next steps

CSV/JSON persistence, SQLite/JDBC storage, real CTA API integration, test automation with JUnit, and a JavaFX dashboard.

## License

MIT License. See [LICENSE](LICENSE).
