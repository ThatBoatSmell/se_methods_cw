package com.napier.sem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class App {
    private Connection con = null;

    public static void main(String[] args) {
        // Start Spring Boot application
        SpringApplication.run(App.class, args);
    }

    /**
     * Home page endpoint
     */
    @RequestMapping("/")
    public String home() {
        return "<html>" +
                "<head><title>Population Reports</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }" +
                "h1 { color: #333; }" +
                "a { display: inline-block; margin: 10px; padding: 15px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }" +
                "a:hover { background-color: #0056b3; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>Population Reports System</h1>" +
                "<p>Select a report to view:</p>" +
                "<a href='/population/continent'>Population by Continent</a>" +
                "<a href='/population/region'>Population by Region</a>" +
                "<a href='/population/country'>Population by Country</a>" +
                "</body></html>";
    }

    /**
     * Population by Continent endpoint
     */
    @RequestMapping(value = "/population/continent", method = RequestMethod.GET)
    public String getPopulationByContinent() {
        connect();
        ArrayList<PopulationReport> reports = getPopulationByContinentData();
        disconnect();
        return generateHTMLReport(reports, "Population by Continent", "/");
    }

    /**
     * Population by Region endpoint
     */
    @RequestMapping(value = "/population/region", method = RequestMethod.GET)
    public String getPopulationByRegion() {
        connect();
        ArrayList<PopulationReport> reports = getPopulationByRegionData();
        disconnect();
        return generateHTMLReport(reports, "Population by Region", "/");
    }

    /**
     * Population by Country endpoint
     */
    @RequestMapping(value = "/population/country", method = RequestMethod.GET)
    public String getPopulationByCountry() {
        connect();
        ArrayList<PopulationReport> reports = getPopulationByCountryData();
        disconnect();
        return generateHTMLReport(reports, "Population by Country", "/");
    }

    /**
     * Generate HTML table from population reports
     */
    private String generateHTMLReport(ArrayList<PopulationReport> reports, String title, String backLink) {
        if (reports == null || reports.isEmpty()) {
            return "<html><body><h1>No data available</h1><a href='" + backLink + "'>Back to Home</a></body></html>";
        }

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>").append(title).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        html.append("h1 { color: #333; text-align: center; }");
        html.append("table { border-collapse: collapse; width: 100%; margin: 20px 0; background-color: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        html.append("th { background-color: #007bff; color: white; font-weight: bold; }");
        html.append("tr:nth-child(even) { background-color: #f9f9f9; }");
        html.append("tr:hover { background-color: #f1f1f1; }");
        html.append(".back-link { display: inline-block; margin: 20px 0; padding: 10px 20px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px; }");
        html.append(".back-link:hover { background-color: #218838; }");
        html.append("</style></head><body>");

        html.append("<h1>").append(title).append("</h1>");
        html.append("<a class='back-link' href='").append(backLink).append("'>← Back to Home</a>");

        html.append("<table>");
        html.append("<thead><tr>");
        html.append("<th>Name</th>");
        html.append("<th>Total Population</th>");
        html.append("<th>City Population</th>");
        html.append("<th>City %</th>");
        html.append("<th>Non-City Population</th>");
        html.append("<th>Non-City %</th>");
        html.append("</tr></thead>");
        html.append("<tbody>");

        for (PopulationReport report : reports) {
            if (report == null) continue;

            html.append("<tr>");
            html.append("<td>").append(report.name).append("</td>");
            html.append("<td>").append(String.format("%,d", report.totalPopulation)).append("</td>");
            html.append("<td>").append(String.format("%,d", report.cityPopulation)).append("</td>");

            if (Double.isNaN(report.cityPercentage)) {
                html.append("<td>NaN%</td>");
            } else {
                html.append("<td>").append(String.format("%.2f%%", report.cityPercentage)).append("</td>");
            }

            html.append("<td>").append(String.format("%,d", report.nonCityPopulation)).append("</td>");

            if (Double.isNaN(report.nonCityPercentage)) {
                html.append("<td>NaN%</td>");
            } else {
                html.append("<td>").append(String.format("%.2f%%", report.nonCityPercentage)).append("</td>");
            }

            html.append("</tr>");
        }

        html.append("</tbody></table>");
        html.append("<a class='back-link' href='").append(backLink).append("'>← Back to Home</a>");
        html.append("</body></html>");

        return html.toString();
    }

    // ========== DATABASE CONNECTION METHODS ==========

    /**
     * Connect to database with flexible host/port configuration
     * Tries Docker configuration first (db:3306), then falls back to localhost:33060
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Try Docker configuration first (for production/Docker environment)
        String dockerHost = "db";
        int dockerPort = 3306;

        // Try localhost configuration (for local testing)
        String localHost = "localhost";
        int localPort = 33060;

        // Check environment variable to determine which to use
        String dbHost = System.getenv("DB_HOST");
        String dbPortStr = System.getenv("DB_PORT");

        if (dbHost != null && dbPortStr != null) {
            // Use environment variables if set
            try {
                int dbPort = Integer.parseInt(dbPortStr);
                if (tryConnect(dbHost, dbPort)) {
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid DB_PORT environment variable");
            }
        }

        // Try Docker first
        System.out.println("Attempting Docker database connection (db:3306)...");
        if (tryConnect(dockerHost, dockerPort)) {
            return;
        }

        // Fall back to localhost
        System.out.println("Docker connection failed, trying localhost (localhost:33060)...");
        if (tryConnect(localHost, localPort)) {
            return;
        }

        System.out.println("Failed to connect to database with all configurations");
    }

    /**
     * Try to connect to database with given host and port
     */
    private boolean tryConnect(String host, int port) {
        int retries = 10;
        for (int i = 0; i < retries; i++) {
            try {
                Thread.sleep(1000);
                String url = String.format("jdbc:mysql://%s:%d/world?useSSL=false&allowPublicKeyRetrieval=true", host, port);
                con = DriverManager.getConnection(url, "root", "example");
                System.out.println("Successfully connected to " + host + ":" + port);
                return true;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to " + host + ":" + port + " attempt " + (i + 1));
                if (i == retries - 1) {
                    System.out.println("Final error: " + sqle.getMessage());
                }
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
                return false;
            }
        }
        return false;
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public Connection getConnection() {
        return con;
    }

    // ========== DATA RETRIEVAL METHODS ==========

    public ArrayList<PopulationReport> getPopulationByContinentData() {
        ArrayList<PopulationReport> reports = new ArrayList<>();

        if (con != null) {
            try {
                Statement stmt = con.createStatement();

                String query = "SELECT continent, " +
                        "SUM(total_pop) AS total_population, " +
                        "SUM(city_pop) AS city_population " +
                        "FROM ( " +
                        "    SELECT country.continent, " +
                        "    country.population AS total_pop, " +
                        "    COALESCE(SUM(city.population), 0) AS city_pop " +
                        "    FROM country " +
                        "    LEFT JOIN city ON country.code = city.countryCode " +
                        "    GROUP BY country.code, country.continent, country.population " +
                        ") AS subquery " +
                        "GROUP BY continent " +
                        "ORDER BY total_population DESC";

                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    PopulationReport report = new PopulationReport();
                    report.name = rs.getString("continent");
                    report.totalPopulation = rs.getLong("total_population");
                    report.cityPopulation = rs.getLong("city_population");
                    report.nonCityPopulation = report.totalPopulation - report.cityPopulation;

                    if (report.totalPopulation > 0) {
                        report.cityPercentage = (report.cityPopulation * 100.0) / report.totalPopulation;
                        report.nonCityPercentage = (report.nonCityPopulation * 100.0) / report.totalPopulation;
                    } else {
                        report.cityPercentage = Double.NaN;
                        report.nonCityPercentage = Double.NaN;
                    }

                    reports.add(report);
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }
        return reports;
    }

    public ArrayList<PopulationReport> getPopulationByRegionData() {
        ArrayList<PopulationReport> reports = new ArrayList<>();

        if (con != null) {
            try {
                Statement stmt = con.createStatement();

                String query = "SELECT region, " +
                        "SUM(total_pop) AS total_population, " +
                        "SUM(city_pop) AS city_population " +
                        "FROM ( " +
                        "    SELECT country.region, " +
                        "    country.population AS total_pop, " +
                        "    COALESCE(SUM(city.population), 0) AS city_pop " +
                        "    FROM country " +
                        "    LEFT JOIN city ON country.code = city.countryCode " +
                        "    GROUP BY country.code, country.region, country.population " +
                        ") AS subquery " +
                        "GROUP BY region " +
                        "ORDER BY total_population DESC";

                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    PopulationReport report = new PopulationReport();
                    report.name = rs.getString("region");
                    report.totalPopulation = rs.getLong("total_population");
                    report.cityPopulation = rs.getLong("city_population");
                    report.nonCityPopulation = report.totalPopulation - report.cityPopulation;

                    if (report.totalPopulation > 0) {
                        report.cityPercentage = (report.cityPopulation * 100.0) / report.totalPopulation;
                        report.nonCityPercentage = (report.nonCityPopulation * 100.0) / report.totalPopulation;
                    } else {
                        report.cityPercentage = Double.NaN;
                        report.nonCityPercentage = Double.NaN;
                    }

                    reports.add(report);
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }
        return reports;
    }

    public ArrayList<PopulationReport> getPopulationByCountryData() {
        ArrayList<PopulationReport> reports = new ArrayList<>();

        if (con != null) {
            try {
                Statement stmt = con.createStatement();

                String query = "SELECT country.name, " +
                        "country.population AS total_population, " +
                        "COALESCE(SUM(city.population), 0) AS city_population " +
                        "FROM country " +
                        "LEFT JOIN city ON country.code = city.countryCode " +
                        "GROUP BY country.code, country.name, country.population " +
                        "ORDER BY total_population DESC";

                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    PopulationReport report = new PopulationReport();
                    report.name = rs.getString("name");
                    report.totalPopulation = rs.getLong("total_population");
                    report.cityPopulation = rs.getLong("city_population");
                    report.nonCityPopulation = report.totalPopulation - report.cityPopulation;

                    if (report.totalPopulation > 0) {
                        report.cityPercentage = (report.cityPopulation * 100.0) / report.totalPopulation;
                        report.nonCityPercentage = (report.nonCityPopulation * 100.0) / report.totalPopulation;
                    } else {
                        report.cityPercentage = Double.NaN;
                        report.nonCityPercentage = Double.NaN;
                    }

                    reports.add(report);
                }
            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }
        return reports;
    }
}