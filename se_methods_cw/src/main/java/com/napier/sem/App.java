package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

public class App {
    private Connection con = null;
    private Scanner scanner = new Scanner(System.in);

    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Connection to the database
        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true", "root", "example");
                System.out.println("Successfully connected");
                // Wait a bit
                Thread.sleep(1000);
                // Exit for loop
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public void displayMainMenu() {
        System.out.println("\n========================================");
        System.out.println("   WORLD POPULATION DATABASE SYSTEM");
        System.out.println("========================================");
        System.out.println("1.  Country Queries");
        System.out.println("2.  City Queries");
        System.out.println("3.  Population Queries");
        System.out.println("4.  Language Reports");
        System.out.println("5.  Detailed Reports");
        System.out.println("0.  Exit");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
    }

    public void displayCountryMenu() {
        System.out.println("\n--- COUNTRY QUERIES ---");
        System.out.println("1. All countries in the world (largest to smallest population)");
        System.out.println("2. All countries in a continent (largest to smallest population)");
        System.out.println("3. All countries in a region (largest to smallest population)");
        System.out.println("4. Top N populated countries in the world");
        System.out.println("5. Top N populated countries in a continent");
        System.out.println("6. Top N populated countries in a region");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
    }

    public void displayCityMenu() {
        System.out.println("\n--- CITY QUERIES ---");
        System.out.println("1. All cities in the world (largest to smallest population)");
        System.out.println("2. All cities in a continent (largest to smallest population)");
        System.out.println("3. All cities in a region (largest to smallest population)");
        System.out.println("4. All cities in a country (largest to smallest population)");
        System.out.println("5. All cities in a district (largest to smallest population)");
        System.out.println("6. Top N populated cities in the world");
        System.out.println("7. Top N populated cities in a continent");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
    }

    public void displayPopulationMenu() {
        System.out.println("\n--- POPULATION QUERIES ---");
        System.out.println("1. Population of the world");
        System.out.println("2. Population of a continent");
        System.out.println("3. Population of a region");
        System.out.println("4. Population of a country");
        System.out.println("5. Population of a district");
        System.out.println("6. Population of a city");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
    }

    public void displayReportMenu() {
        System.out.println("\n--- DETAILED REPORTS ---");
        System.out.println("1. Country Report (Code, Name, Continent, Region, Population, Capital)");
        System.out.println("2. City Report (Name, Country, District, Population)");
        System.out.println("3. Capital City Report (Name, Country, Population)");
        System.out.println("4. Population Report - Continent (with city/non-city breakdown)");
        System.out.println("5. Population Report - Region (with city/non-city breakdown)");
        System.out.println("6. Population Report - Country (with city/non-city breakdown)");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
    }

    // ===============================================
    // COUNTRY QUERIES
    // ===============================================

    public void allCountriesWorld() {
        try {
            System.out.println("\n=== All countries in the world (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM country ORDER BY population DESC LIMIT 10";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void allCountriesContinent() {
        try {
            System.out.print("Enter continent name (e.g., Asia, Europe, Africa): ");
            String continent = scanner.nextLine();
            System.out.println("\n=== All countries in " + continent + " (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM country WHERE continent = '" + continent + "' ORDER BY population DESC";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void allCountriesRegion() {
        try {
            System.out.print("Enter region name (e.g., Caribbean, Western Europe): ");
            String region = scanner.nextLine();
            System.out.println("\n=== All countries in " + region + " (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM country WHERE region = '" + region + "' ORDER BY population DESC";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void topNCountriesWorld() {
        try {
            System.out.print("Enter number of countries (N): ");
            int n = Integer.parseInt(scanner.nextLine());
            System.out.println("\n=== Top " + n + " populated countries in the world ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM country ORDER BY population DESC LIMIT " + n;
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }
    }

    public void topNCountriesContinent() {
        try {
            System.out.print("Enter continent name: ");
            String continent = scanner.nextLine();
            System.out.print("Enter number of countries (N): ");
            int n = Integer.parseInt(scanner.nextLine());
            System.out.println("\n=== Top " + n + " populated countries in " + continent + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM country WHERE continent = '" + continent + "' ORDER BY population DESC LIMIT " + n;
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }
    }

    public void topNCountriesRegion() {
        try {
            System.out.print("Enter region name: ");
            String region = scanner.nextLine();
            System.out.print("Enter number of countries (N): ");
            int n = Integer.parseInt(scanner.nextLine());
            System.out.println("\n=== Top " + n + " populated countries in " + region + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM country WHERE region = '" + region + "' ORDER BY population DESC LIMIT " + n;
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }
    }

    // ===============================================
    // CITY QUERIES
    // ===============================================

    public void allCitiesWorld() {
        try {
            System.out.println("\n=== All cities in the world (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM city ORDER BY population DESC LIMIT 20";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void allCitiesContinent() {
        try {
            System.out.print("Enter continent name: ");
            String continent = scanner.nextLine();
            System.out.println("\n=== All cities in " + continent + " (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT city.name, city.population FROM city JOIN country ON city.countrycode = country.code WHERE country.continent = '" + continent + "' ORDER BY city.population DESC LIMIT 20";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void allCitiesRegion() {
        try {
            System.out.print("Enter region name: ");
            String region = scanner.nextLine();
            System.out.println("\n=== All cities in " + region + " (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT city.name, city.population FROM city JOIN country ON city.countrycode = country.code WHERE country.region = '" + region + "' ORDER BY city.population DESC LIMIT 20";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void allCitiesCountry() {
        try {
            System.out.print("Enter country code (e.g., USA, GBR, CHN): ");
            String countryCode = scanner.nextLine();
            System.out.println("\n=== All cities in " + countryCode + " (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM city WHERE countrycode = '" + countryCode + "' ORDER BY population DESC";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void allCitiesDistrict() {
        try {
            System.out.print("Enter district name (e.g., California, England): ");
            String district = scanner.nextLine();
            System.out.println("\n=== All cities in " + district + " (largest to smallest) ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM city WHERE district = '" + district + "' ORDER BY population DESC";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void topNCitiesWorld() {
        try {
            System.out.print("Enter number of cities (N): ");
            int n = Integer.parseInt(scanner.nextLine());
            System.out.println("\n=== Top " + n + " populated cities in the world ===");
            Statement stmt = con.createStatement();
            String query = "SELECT name, population FROM city ORDER BY population DESC LIMIT " + n;
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }
    }

    public void topNCitiesContinent() {
        try {
            System.out.print("Enter continent name: ");
            String continent = scanner.nextLine();
            System.out.print("Enter number of cities (N): ");
            int n = Integer.parseInt(scanner.nextLine());
            System.out.println("\n=== Top " + n + " populated cities in " + continent + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT city.name, city.population FROM city JOIN country ON city.countrycode = country.code WHERE country.continent = '" + continent + "' ORDER BY city.population DESC LIMIT " + n;
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Name = " + rset.getString("name") + ", Population = " + rset.getInt("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }
    }

    // ===============================================
    // POPULATION QUERIES
    // ===============================================

    public void populationWorld() {
        try {
            System.out.println("\n=== Population of the world ===");
            Statement stmt = con.createStatement();
            String query = "SELECT SUM(population) AS total_population FROM country";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                System.out.println("World Population = " + rset.getLong("total_population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationContinent() {
        try {
            System.out.print("Enter continent name: ");
            String continent = scanner.nextLine();
            System.out.println("\n=== Population of " + continent + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT SUM(population) AS total_population FROM country WHERE continent = '" + continent + "'";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                System.out.println("Continent Population = " + rset.getLong("total_population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationRegion() {
        try {
            System.out.print("Enter region name: ");
            String region = scanner.nextLine();
            System.out.println("\n=== Population of " + region + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT SUM(population) AS total_population FROM country WHERE region = '" + region + "'";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                System.out.println("Region Population = " + rset.getLong("total_population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationCountry() {
        try {
            System.out.print("Enter country code (e.g., USA): ");
            String countryCode = scanner.nextLine();
            System.out.println("\n=== Population of " + countryCode + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT population FROM country WHERE code = '" + countryCode + "'";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                System.out.println("Country Population = " + rset.getLong("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationDistrict() {
        try {
            System.out.print("Enter district name: ");
            String district = scanner.nextLine();
            System.out.println("\n=== Population of " + district + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT SUM(population) AS total_population FROM city WHERE district = '" + district + "'";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                System.out.println("District Population = " + rset.getLong("total_population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationCity() {
        try {
            System.out.print("Enter city name: ");
            String cityName = scanner.nextLine();
            System.out.println("\n=== Population of " + cityName + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT population FROM city WHERE name = '" + cityName + "'";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                System.out.println("City Population = " + rset.getLong("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    // ===============================================
    // LANGUAGE REPORTS
    // ===============================================

    public void languageReport() {
        try {
            System.out.println("\n=== Language speakers (greatest to smallest with world percentage) ===");
            
            // Get world population
            Statement stmtWorld = con.createStatement();
            String queryWorld = "SELECT SUM(population) AS total_population FROM country";
            ResultSet rsetWorld = stmtWorld.executeQuery(queryWorld);
            long worldPopulation = 0;
            if (rsetWorld.next()) {
                worldPopulation = rsetWorld.getLong("total_population");
            }
            
            // Get language speakers
            Statement stmt = con.createStatement();
            String query = "SELECT cl.language, SUM(c.population * cl.percentage / 100) AS speakers "
                    + "FROM countrylanguage cl "
                    + "JOIN country c ON cl.countrycode = c.code "
                    + "WHERE cl.language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') "
                    + "GROUP BY cl.language "
                    + "ORDER BY speakers DESC";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                String language = rset.getString("language");
                long speakers = rset.getLong("speakers");
                double percentage = (double) speakers / worldPopulation * 100;
                System.out.println("Language = " + language + ", Speakers = " + speakers + ", World % = " + String.format("%.2f", percentage) + "%");
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    // ===============================================
    // DETAILED REPORTS
    // ===============================================

    public void countryReport() {
        try {
            System.out.println("\n=== Country Report ===");
            Statement stmt = con.createStatement();
            String query = "SELECT c.code, c.name, c.continent, c.region, c.population, city.name AS capital "
                    + "FROM country c "
                    + "LEFT JOIN city ON c.capital = city.id "
                    + "ORDER BY c.population DESC LIMIT 20";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Code = " + rset.getString("code") + 
                                   ", Name = " + rset.getString("name") + 
                                   ", Continent = " + rset.getString("continent") + 
                                   ", Region = " + rset.getString("region") + 
                                   ", Population = " + rset.getLong("population") + 
                                   ", Capital = " + rset.getString("capital"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void cityReport() {
        try {
            System.out.println("\n=== City Report ===");
            Statement stmt = con.createStatement();
            String query = "SELECT city.name, country.name AS country, city.district, city.population "
                    + "FROM city "
                    + "JOIN country ON city.countrycode = country.code "
                    + "ORDER BY city.population DESC LIMIT 20";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("City = " + rset.getString("name") + 
                                   ", Country = " + rset.getString("country") + 
                                   ", District = " + rset.getString("district") + 
                                   ", Population = " + rset.getLong("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void capitalCityReport() {
        try {
            System.out.println("\n=== Capital City Report ===");
            Statement stmt = con.createStatement();
            String query = "SELECT city.name, country.name AS country, city.population "
                    + "FROM city "
                    + "JOIN country ON city.id = country.capital "
                    + "ORDER BY city.population DESC LIMIT 20";
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                System.out.println("Capital = " + rset.getString("name") + 
                                   ", Country = " + rset.getString("country") + 
                                   ", Population = " + rset.getLong("population"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationReportContinent() {
        try {
            System.out.print("Enter continent name: ");
            String continent = scanner.nextLine();
            System.out.println("\n=== Population Report - " + continent + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT c.continent AS name, SUM(c.population) AS total_population, "
                    + "SUM(city_pop.city_population) AS city_population "
                    + "FROM country c "
                    + "LEFT JOIN (SELECT countrycode, SUM(population) AS city_population FROM city GROUP BY countrycode) city_pop "
                    + "ON c.code = city_pop.countrycode "
                    + "WHERE c.continent = '" + continent + "' "
                    + "GROUP BY c.continent";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                long totalPop = rset.getLong("total_population");
                long cityPop = rset.getLong("city_population");
                long nonCityPop = totalPop - cityPop;
                double cityPercent = (double) cityPop / totalPop * 100;
                double nonCityPercent = (double) nonCityPop / totalPop * 100;
                System.out.println("Continent = " + rset.getString("name"));
                System.out.println("Total Population = " + totalPop);
                System.out.println("City Population = " + cityPop + " (" + String.format("%.2f", cityPercent) + "%)");
                System.out.println("Non-City Population = " + nonCityPop + " (" + String.format("%.2f", nonCityPercent) + "%)");
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationReportRegion() {
        try {
            System.out.print("Enter region name: ");
            String region = scanner.nextLine();
            System.out.println("\n=== Population Report - " + region + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT c.region AS name, SUM(c.population) AS total_population, "
                    + "SUM(city_pop.city_population) AS city_population "
                    + "FROM country c "
                    + "LEFT JOIN (SELECT countrycode, SUM(population) AS city_population FROM city GROUP BY countrycode) city_pop "
                    + "ON c.code = city_pop.countrycode "
                    + "WHERE c.region = '" + region + "' "
                    + "GROUP BY c.region";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                long totalPop = rset.getLong("total_population");
                long cityPop = rset.getLong("city_population");
                long nonCityPop = totalPop - cityPop;
                double cityPercent = (double) cityPop / totalPop * 100;
                double nonCityPercent = (double) nonCityPop / totalPop * 100;
                System.out.println("Region = " + rset.getString("name"));
                System.out.println("Total Population = " + totalPop);
                System.out.println("City Population = " + cityPop + " (" + String.format("%.2f", cityPercent) + "%)");
                System.out.println("Non-City Population = " + nonCityPop + " (" + String.format("%.2f", nonCityPercent) + "%)");
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public void populationReportCountry() {
        try {
            System.out.print("Enter country code: ");
            String countryCode = scanner.nextLine();
            System.out.println("\n=== Population Report - " + countryCode + " ===");
            Statement stmt = con.createStatement();
            String query = "SELECT c.name AS name, c.population AS total_population, "
                    + "COALESCE(city_pop.city_population, 0) AS city_population "
                    + "FROM country c "
                    + "LEFT JOIN (SELECT countrycode, SUM(population) AS city_population FROM city GROUP BY countrycode) city_pop "
                    + "ON c.code = city_pop.countrycode "
                    + "WHERE c.code = '" + countryCode + "'";
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                long totalPop = rset.getLong("total_population");
                long cityPop = rset.getLong("city_population");
                long nonCityPop = totalPop - cityPop;
                double cityPercent = (double) cityPop / totalPop * 100;
                double nonCityPercent = (double) nonCityPop / totalPop * 100;
                System.out.println("Country = " + rset.getString("name"));
                System.out.println("Total Population = " + totalPop);
                System.out.println("City Population = " + cityPop + " (" + String.format("%.2f", cityPercent) + "%)");
                System.out.println("Non-City Population = " + nonCityPop + " (" + String.format("%.2f", nonCityPercent) + "%)");
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    // ===============================================
    // MENU HANDLERS
    // ===============================================

    public void handleCountryMenu() {
        boolean back = false;
        while (!back) {
            displayCountryMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": allCountriesWorld(); break;
                case "2": allCountriesContinent(); break;
                case "3": allCountriesRegion(); break;
                case "4": topNCountriesWorld(); break;
                case "5": topNCountriesContinent(); break;
                case "6": topNCountriesRegion(); break;
                case "0": back = true; break;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public void handleCityMenu() {
        boolean back = false;
        while (!back) {
            displayCityMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": allCitiesWorld(); break;
                case "2": allCitiesContinent(); break;
                case "3": allCitiesRegion(); break;
                case "4": allCitiesCountry(); break;
                case "5": allCitiesDistrict(); break;
                case "6": topNCitiesWorld(); break;
                case "7": topNCitiesContinent(); break;
                case "0": back = true; break;
                default: 
                    System.out.println("please, use an correct number");
            }
        }
    }
public void handlePopulationMenu() {
        boolean back = false;
        while (!back) {
            displayPopulationMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": populationWorld(); break;
                case "2": populationContinent(); break;
                case "3": populationRegion(); break;
                case "4": populationCountry(); break;
                case "5": populationDistrict(); break;
                case "6": populationCity(); break;
                case "0": back = true; break;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public void handleReportMenu() {
        boolean back = false;
        while (!back) {
            displayReportMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": countryReport(); break;
                case "2": cityReport(); break;
                case "3": capitalCityReport(); break;
                case "4": populationReportContinent(); break;
                case "5": populationReportRegion(); break;
                case "6": populationReportCountry(); break;
                case "0": back = true; break;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": handleCountryMenu(); break;
                case "2": handleCityMenu(); break;
                case "3": handlePopulationMenu(); break;
                case "4": languageReport(); break;
                case "5": handleReportMenu(); break;
                case "0": 
                    exit = true;
                    System.out.println("Exiting... Goodbye!");
                    break;
                default: 
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        App a = new App();
        a.connect();
        
        if (a.con != null) {
            a.run();
            a.disconnect();
        } else {
            System.out.println("Failed to connect to database. Exiting...");
        }
    }
}