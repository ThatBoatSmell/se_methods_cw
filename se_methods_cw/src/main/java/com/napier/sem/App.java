package com.napier.sem;

import java.sql.*;

public class App {
    private Connection con = null;

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
                // this may need to be changed back to db:3306 instead of localhost:33060 !!!
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

    public static void main(String[] args) {

        App a = new App();

        a.connect();
        if (a.con != null) {
            try {

                // All the countries in the world organised by largest population to smallest.
                System.out.println("All the countries in the world organised by largest population to smallest.");
                Statement stmtLtoS = a.con.createStatement();
                String strSelectLtoS = "SELECT name, population "
                        + "FROM country "
                        + "ORDER BY population DESC";
                ResultSet rsetLtoS = stmtLtoS.executeQuery(strSelectLtoS);
                while (rsetLtoS.next()) {
                    Country country1 = new Country();
                    country1.population = rsetLtoS.getInt("population");
                    country1.name = rsetLtoS.getString("name");
                    System.out.println("Name = " + country1.name + ", Population = " + country1.population);
                }


                System.out.println("\n\n\n\nAll the countries in a continent organised by largest population to smallest.");
                Statement stmtStoL = a.con.createStatement();
                String strSelectStoL = "SELECT name, population "
                        + "FROM country "
                        + "ORDER BY population ASC";
                ResultSet rsetStoL = stmtStoL.executeQuery(strSelectStoL);
                while (rsetStoL.next()) {
                    Country country1 = new Country();
                    country1.population = rsetStoL.getInt("population");
                    country1.name = rsetStoL.getString("name");
                    System.out.println("Name = " + country1.name + ", Population = " + country1.population);
                }


                // All the countries in a continent organised by largest population to smallest.
                System.out.println("\n\n\n\nAll the countries in a continent organised by largest population to smallest.");
                Statement stmtContinent = a.con.createStatement();
                String strSelectContinent = "SELECT name, population "
                        + "FROM country "
                        + "WHERE continent = 'Asia' "
                        + "ORDER BY population DESC";
                ResultSet rsetContinent = stmtContinent.executeQuery(strSelectContinent);
                while (rsetContinent.next()) {
                    Country country1 = new Country();
                    country1.population = rsetContinent.getInt("population");
                    country1.name = rsetContinent.getString("name");
                    System.out.println("Name = " + country1.name + ", Population = " + country1.population);
                }

// All the countries in a region organised by largest population to smallest.
                System.out.println("\n\n\n\nAll the countries in a region organised by largest population to smallest.");
                Statement stmtRegion = a.con.createStatement();
                String strSelectRegion = "SELECT name, population "
                        + "FROM country "
                        + "WHERE region = 'Caribbean' "
                        + "ORDER BY population DESC";
                ResultSet rsetRegion = stmtRegion.executeQuery(strSelectRegion);
                while (rsetRegion.next()) {
                    Country country2 = new Country();
                    country2.population = rsetRegion.getInt("population");
                    country2.name = rsetRegion.getString("name");
                    System.out.println("Name = " + country2.name + ", Population = " + country2.population);
                }

// The top N populated countries in the world where N is provided by the user.
                System.out.println("\n\n\n\nThe top N populated countries in the world.");
                int nWorld = 5; // N provided by user
                Statement stmtTopN = a.con.createStatement();
                String strSelectTopN = "SELECT name, population "
                        + "FROM country "
                        + "ORDER BY population DESC "
                        + "LIMIT " + nWorld;
                ResultSet rsetTopN = stmtTopN.executeQuery(strSelectTopN);
                while (rsetTopN.next()) {
                    Country country3 = new Country();
                    country3.population = rsetTopN.getInt("population");
                    country3.name = rsetTopN.getString("name");
                    System.out.println("Name = " + country3.name + ", Population = " + country3.population);
                }

// The top N populated countries in a continent where N is provided by the user.
                System.out.println("\n\n\n\nThe top N populated countries in a continent.");
                int nContinent = 5; // N provided by user
                Statement stmtTopNContinent = a.con.createStatement();
                String strSelectTopNContinent = "SELECT name, population "
                        + "FROM country "
                        + "WHERE continent = 'Europe' "
                        + "ORDER BY population DESC "
                        + "LIMIT " + nContinent;
                ResultSet rsetTopNContinent = stmtTopNContinent.executeQuery(strSelectTopNContinent);
                while (rsetTopNContinent.next()) {
                    Country country4 = new Country();
                    country4.population = rsetTopNContinent.getInt("population");
                    country4.name = rsetTopNContinent.getString("name");
                    System.out.println("Name = " + country4.name + ", Population = " + country4.population);
                }

// The top N populated countries in a region where N is provided by the user.
                System.out.println("\n\n\n\nThe top N populated countries in a region.");
                int nRegion = 5; // N provided by user
                Statement stmtTopNRegion = a.con.createStatement();
                String strSelectTopNRegion = "SELECT name, population "
                        + "FROM country "
                        + "WHERE region = 'Western Europe' "
                        + "ORDER BY population DESC "
                        + "LIMIT " + nRegion;
                ResultSet rsetTopNRegion = stmtTopNRegion.executeQuery(strSelectTopNRegion);
                while (rsetTopNRegion.next()) {
                    Country country5 = new Country();
                    country5.population = rsetTopNRegion.getInt("population");
                    country5.name = rsetTopNRegion.getString("name");
                    System.out.println("Name = " + country5.name + ", Population = " + country5.population);
                }

// All the cities in the world organised by largest population to smallest.
                System.out.println("\n\n\n\nAll the cities in the world organised by largest population to smallest.");
                Statement stmtCities = a.con.createStatement();
                String strSelectCities = "SELECT name, population "
                        + "FROM city "
                        + "ORDER BY population DESC";
                ResultSet rsetCities = stmtCities.executeQuery(strSelectCities);
                while (rsetCities.next()) {
                    String cityName = rsetCities.getString("name");
                    int cityPop = rsetCities.getInt("population");
                    System.out.println("Name = " + cityName + ", Population = " + cityPop);
                }

// All the cities in a continent organised by largest population to smallest.
                System.out.println("\n\n\n\nAll the cities in a continent organised by largest population to smallest.");
                Statement stmtCitiesContinent = a.con.createStatement();
                String strSelectCitiesContinent = "SELECT city.name, city.population "
                        + "FROM city "
                        + "JOIN country ON city.countrycode = country.code "
                        + "WHERE country.continent = 'Asia' "
                        + "ORDER BY city.population DESC";
                ResultSet rsetCitiesContinent = stmtCitiesContinent.executeQuery(strSelectCitiesContinent);
                while (rsetCitiesContinent.next()) {
                    String cityName = rsetCitiesContinent.getString("name");
                    int cityPop = rsetCitiesContinent.getInt("population");
                    System.out.println("Name = " + cityName + ", Population = " + cityPop);
                }

// All the cities in a region organised by largest population to smallest.
                System.out.println("\n\n\n\nAll the cities in a region organised by largest population to smallest.");
                Statement stmtCitiesRegion = a.con.createStatement();
                String strSelectCitiesRegion = "SELECT city.name, city.population "
                        + "FROM city "
                        + "JOIN country ON city.countrycode = country.code "
                        + "WHERE country.region = 'Caribbean' "
                        + "ORDER BY city.population DESC";
                ResultSet rsetCitiesRegion = stmtCitiesRegion.executeQuery(strSelectCitiesRegion);
                while (rsetCitiesRegion.next()) {
                    String cityName = rsetCitiesRegion.getString("name");
                    int cityPop = rsetCitiesRegion.getInt("population");
                    System.out.println("Name = " + cityName + ", Population = " + cityPop);
                }

// All the cities in a country organised by largest population to smallest.
                System.out.println("\n\n\n\nAll the cities in a country organised by largest population to smallest.");
                Statement stmtCitiesCountry = a.con.createStatement();
                String strSelectCitiesCountry = "SELECT name, population "
                        + "FROM city "
                        + "WHERE countrycode = 'USA' "
                        + "ORDER BY population DESC";
                ResultSet rsetCitiesCountry = stmtCitiesCountry.executeQuery(strSelectCitiesCountry);
                while (rsetCitiesCountry.next()) {
                    String cityName = rsetCitiesCountry.getString("name");
                    int cityPop = rsetCitiesCountry.getInt("population");
                    System.out.println("Name = " + cityName + ", Population = " + cityPop);
                }

// All the cities in a district organised by largest population to smallest.
                System.out.println("\n\n\n\nAll the cities in a district organised by largest population to smallest.");
                Statement stmtCitiesDistrict = a.con.createStatement();
                String strSelectCitiesDistrict = "SELECT name, population "
                        + "FROM city "
                        + "WHERE district = 'California' "
                        + "ORDER BY population DESC";
                ResultSet rsetCitiesDistrict = stmtCitiesDistrict.executeQuery(strSelectCitiesDistrict);
                while (rsetCitiesDistrict.next()) {
                    String cityName = rsetCitiesDistrict.getString("name");
                    int cityPop = rsetCitiesDistrict.getInt("population");
                    System.out.println("Name = " + cityName + ", Population = " + cityPop);
                }

// The top N populated cities in the world where N is provided by the user.
                System.out.println("\n\n\n\nThe top N populated cities in the world.");
                int nCitiesWorld = 10; // N provided by user
                Statement stmtTopNCities = a.con.createStatement();
                String strSelectTopNCities = "SELECT name, population "
                        + "FROM city "
                        + "ORDER BY population DESC "
                        + "LIMIT " + nCitiesWorld;
                ResultSet rsetTopNCities = stmtTopNCities.executeQuery(strSelectTopNCities);
                while (rsetTopNCities.next()) {
                    String cityName = rsetTopNCities.getString("name");
                    int cityPop = rsetTopNCities.getInt("population");
                    System.out.println("Name = " + cityName + ", Population = " + cityPop);
                }

// The top N populated cities in a continent where N is provided by the user.
                System.out.println("\n\n\n\nThe top N populated cities in a continent.");
                int nCitiesContinent = 10; // N provided by user
                Statement stmtTopNCitiesContinent = a.con.createStatement();
                String strSelectTopNCitiesContinent = "SELECT city.name, city.population "
                        + "FROM city "
                        + "JOIN country ON city.countrycode = country.code "
                        + "WHERE country.continent = 'Europe' "
                        + "ORDER BY city.population DESC "
                        + "LIMIT " + nCitiesContinent;
                ResultSet rsetTopNCitiesContinent = stmtTopNCitiesContinent.executeQuery(strSelectTopNCitiesContinent);
                while (rsetTopNCitiesContinent.next()) {
                    String cityName = rsetTopNCitiesContinent.getString("name");
                    int cityPop = rsetTopNCitiesContinent.getInt("population");
                    System.out.println("Name = " + cityName + ", Population = " + cityPop);
                }



// ===============================================
// POPULATION QUERIES
// ===============================================

// The population of the world
                System.out.println("\n\n\n\nThe population of the world.");
                Statement stmtWorldPop = a.con.createStatement();
                String strSelectWorldPop = "SELECT SUM(population) AS total_population FROM country";
                ResultSet rsetWorldPop = stmtWorldPop.executeQuery(strSelectWorldPop);
                if (rsetWorldPop.next()) {
                    long worldPop = rsetWorldPop.getLong("total_population");
                    System.out.println("World Population = " + worldPop);
                }

// The population of a continent
                System.out.println("\n\n\n\nThe population of a continent.");
                Statement stmtContinentPop = a.con.createStatement();
                String strSelectContinentPop = "SELECT SUM(population) AS total_population "
                        + "FROM country "
                        + "WHERE continent = 'Asia'";
                ResultSet rsetContinentPop = stmtContinentPop.executeQuery(strSelectContinentPop);
                if (rsetContinentPop.next()) {
                    long continentPop = rsetContinentPop.getLong("total_population");
                    System.out.println("Continent Population (Asia) = " + continentPop);
                }

// The population of a region
                System.out.println("\n\n\n\nThe population of a region.");
                Statement stmtRegionPop = a.con.createStatement();
                String strSelectRegionPop = "SELECT SUM(population) AS total_population "
                        + "FROM country "
                        + "WHERE region = 'Caribbean'";
                ResultSet rsetRegionPop = stmtRegionPop.executeQuery(strSelectRegionPop);
                if (rsetRegionPop.next()) {
                    long regionPop = rsetRegionPop.getLong("total_population");
                    System.out.println("Region Population (Caribbean) = " + regionPop);
                }

// The population of a country
                System.out.println("\n\n\n\nThe population of a country.");
                Statement stmtCountryPop = a.con.createStatement();
                String strSelectCountryPop = "SELECT population "
                        + "FROM country "
                        + "WHERE code = 'USA'";
                ResultSet rsetCountryPop = stmtCountryPop.executeQuery(strSelectCountryPop);
                if (rsetCountryPop.next()) {
                    long countryPop = rsetCountryPop.getLong("population");
                    System.out.println("Country Population (USA) = " + countryPop);
                }

// The population of a district
                System.out.println("\n\n\n\nThe population of a district.");
                Statement stmtDistrictPop = a.con.createStatement();
                String strSelectDistrictPop = "SELECT SUM(population) AS total_population "
                        + "FROM city "
                        + "WHERE district = 'California'";
                ResultSet rsetDistrictPop = stmtDistrictPop.executeQuery(strSelectDistrictPop);
                if (rsetDistrictPop.next()) {
                    long districtPop = rsetDistrictPop.getLong("total_population");
                    System.out.println("District Population (California) = " + districtPop);
                }

// The population of a city
                System.out.println("\n\n\n\nThe population of a city.");
                Statement stmtCityPop = a.con.createStatement();
                String strSelectCityPop = "SELECT population "
                        + "FROM city "
                        + "WHERE name = 'London'";
                ResultSet rsetCityPop = stmtCityPop.executeQuery(strSelectCityPop);
                if (rsetCityPop.next()) {
                    long cityPop = rsetCityPop.getLong("population");
                    System.out.println("City Population (London) = " + cityPop);
                }

// ===============================================
// LANGUAGE SPEAKERS REPORT
// ===============================================

                System.out.println("\n\n\n\nLanguage speakers from greatest to smallest with world population percentage.");

// First get world population for percentage calculation
                Statement stmtWorldPopForLang = a.con.createStatement();
                String strWorldPopForLang = "SELECT SUM(population) AS total_population FROM country";
                ResultSet rsetWorldPopForLang = stmtWorldPopForLang.executeQuery(strWorldPopForLang);
                long worldPopulation = 0;
                if (rsetWorldPopForLang.next()) {
                    worldPopulation = rsetWorldPopForLang.getLong("total_population");
                }

// Get language speakers
                Statement stmtLanguages = a.con.createStatement();
                String strSelectLanguages = "SELECT cl.language, SUM(c.population * cl.percentage / 100) AS speakers "
                        + "FROM countrylanguage cl "
                        + "JOIN country c ON cl.countrycode = c.code "
                        + "WHERE cl.language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') "
                        + "GROUP BY cl.language "
                        + "ORDER BY speakers DESC";
                ResultSet rsetLanguages = stmtLanguages.executeQuery(strSelectLanguages);
                while (rsetLanguages.next()) {
                    String language = rsetLanguages.getString("language");
                    long speakers = rsetLanguages.getLong("speakers");
                    double percentage = (double) speakers / worldPopulation * 100;
                    System.out.println("Language = " + language + ", Speakers = " + speakers + ", World % = " + String.format("%.2f", percentage) + "%");
                }

// ===============================================
// COUNTRY REPORT
// ===============================================

                System.out.println("\n\n\n\nCountry Report (Code, Name, Continent, Region, Population, Capital).");
                Statement stmtCountryReport = a.con.createStatement();
                String strSelectCountryReport = "SELECT c.code, c.name, c.continent, c.region, c.population, city.name AS capital "
                        + "FROM country c "
                        + "LEFT JOIN city ON c.capital = city.id "
                        + "ORDER BY c.population DESC "
                        + "LIMIT 20";
                ResultSet rsetCountryReport = stmtCountryReport.executeQuery(strSelectCountryReport);
                while (rsetCountryReport.next()) {
                    String code = rsetCountryReport.getString("code");
                    String name = rsetCountryReport.getString("name");
                    String continent = rsetCountryReport.getString("continent");
                    String region = rsetCountryReport.getString("region");
                    long population = rsetCountryReport.getLong("population");
                    String capital = rsetCountryReport.getString("capital");
                    System.out.println("Code = " + code + ", Name = " + name + ", Continent = " + continent +
                            ", Region = " + region + ", Population = " + population + ", Capital = " + capital);
                }

// ===============================================
// CITY REPORT
// ===============================================

                System.out.println("\n\n\n\nCity Report (Name, Country, District, Population).");
                Statement stmtCityReport = a.con.createStatement();
                String strSelectCityReport = "SELECT city.name, country.name AS country, city.district, city.population "
                        + "FROM city "
                        + "JOIN country ON city.countrycode = country.code "
                        + "ORDER BY city.population DESC "
                        + "LIMIT 20";
                ResultSet rsetCityReport = stmtCityReport.executeQuery(strSelectCityReport);
                while (rsetCityReport.next()) {
                    String cityName = rsetCityReport.getString("name");
                    String countryName = rsetCityReport.getString("country");
                    String district = rsetCityReport.getString("district");
                    long population = rsetCityReport.getLong("population");
                    System.out.println("City = " + cityName + ", Country = " + countryName +
                            ", District = " + district + ", Population = " + population);
                }

// ===============================================
// CAPITAL CITY REPORT
// ===============================================

                System.out.println("\n\n\n\nCapital City Report (Name, Country, Population).");
                Statement stmtCapitalReport = a.con.createStatement();
                String strSelectCapitalReport = "SELECT city.name, country.name AS country, city.population "
                        + "FROM city "
                        + "JOIN country ON city.id = country.capital "
                        + "ORDER BY city.population DESC "
                        + "LIMIT 20";
                ResultSet rsetCapitalReport = stmtCapitalReport.executeQuery(strSelectCapitalReport);
                while (rsetCapitalReport.next()) {
                    String capitalName = rsetCapitalReport.getString("name");
                    String countryName = rsetCapitalReport.getString("country");
                    long population = rsetCapitalReport.getLong("population");
                    System.out.println("Capital = " + capitalName + ", Country = " + countryName +
                            ", Population = " + population);
                }

// ===============================================
// POPULATION REPORTS (with city/non-city breakdown)
// ===============================================

// Population report for a continent
                System.out.println("\n\n\n\nPopulation Report - Continent.");
                Statement stmtPopReportContinent = a.con.createStatement();
                String strPopReportContinent = "SELECT "
                        + "c.continent AS name, "
                        + "SUM(c.population) AS total_population, "
                        + "SUM(city_pop.city_population) AS city_population "
                        + "FROM country c "
                        + "LEFT JOIN (SELECT countrycode, SUM(population) AS city_population FROM city GROUP BY countrycode) city_pop "
                        + "ON c.code = city_pop.countrycode "
                        + "WHERE c.continent = 'Europe' "
                        + "GROUP BY c.continent";
                ResultSet rsetPopReportContinent = stmtPopReportContinent.executeQuery(strPopReportContinent);
                if (rsetPopReportContinent.next()) {
                    String name = rsetPopReportContinent.getString("name");
                    long totalPop = rsetPopReportContinent.getLong("total_population");
                    long cityPop = rsetPopReportContinent.getLong("city_population");
                    long nonCityPop = totalPop - cityPop;
                    double cityPercent = (double) cityPop / totalPop * 100;
                    double nonCityPercent = (double) nonCityPop / totalPop * 100;
                    System.out.println("Continent = " + name);
                    System.out.println("Total Population = " + totalPop);
                    System.out.println("City Population = " + cityPop + " (" + String.format("%.2f", cityPercent) + "%)");
                    System.out.println("Non-City Population = " + nonCityPop + " (" + String.format("%.2f", nonCityPercent) + "%)");
                }

// Population report for a region
                System.out.println("\n\n\n\nPopulation Report - Region.");
                Statement stmtPopReportRegion = a.con.createStatement();
                String strPopReportRegion = "SELECT "
                        + "c.region AS name, "
                        + "SUM(c.population) AS total_population, "
                        + "SUM(city_pop.city_population) AS city_population "
                        + "FROM country c "
                        + "LEFT JOIN (SELECT countrycode, SUM(population) AS city_population FROM city GROUP BY countrycode) city_pop "
                        + "ON c.code = city_pop.countrycode "
                        + "WHERE c.region = 'Western Europe' "
                        + "GROUP BY c.region";
                ResultSet rsetPopReportRegion = stmtPopReportRegion.executeQuery(strPopReportRegion);
                if (rsetPopReportRegion.next()) {
                    String name = rsetPopReportRegion.getString("name");
                    long totalPop = rsetPopReportRegion.getLong("total_population");
                    long cityPop = rsetPopReportRegion.getLong("city_population");
                    long nonCityPop = totalPop - cityPop;
                    double cityPercent = (double) cityPop / totalPop * 100;
                    double nonCityPercent = (double) nonCityPop / totalPop * 100;
                    System.out.println("Region = " + name);
                    System.out.println("Total Population = " + totalPop);
                    System.out.println("City Population = " + cityPop + " (" + String.format("%.2f", cityPercent) + "%)");
                    System.out.println("Non-City Population = " + nonCityPop + " (" + String.format("%.2f", nonCityPercent) + "%)");
                }

// Population report for a country
                System.out.println("\n\n\n\nPopulation Report - Country.");
                Statement stmtPopReportCountry = a.con.createStatement();
                String strPopReportCountry = "SELECT "
                        + "c.name AS name, "
                        + "c.population AS total_population, "
                        + "COALESCE(city_pop.city_population, 0) AS city_population "
                        + "FROM country c "
                        + "LEFT JOIN (SELECT countrycode, SUM(population) AS city_population FROM city GROUP BY countrycode) city_pop "
                        + "ON c.code = city_pop.countrycode "
                        + "WHERE c.code = 'USA'";
                ResultSet rsetPopReportCountry = stmtPopReportCountry.executeQuery(strPopReportCountry);
                if (rsetPopReportCountry.next()) {
                    String name = rsetPopReportCountry.getString("name");
                    long totalPop = rsetPopReportCountry.getLong("total_population");
                    long nonCityPop = rsetPopReportCountry.getLong("city_population");
                    long CityPop = totalPop - nonCityPop;
                    double cityPercent = (double) CityPop / totalPop * 100;
                    double nonCityPercent = (double) nonCityPop / totalPop * 100;
                    System.out.println("Country = " + name);
                    System.out.println("Total Population = " + totalPop);
                    System.out.println("City Population = " + CityPop + " (" + String.format("%.2f", cityPercent) + "%)");
                    System.out.println("Non-City Population = " + nonCityPop + " (" + String.format("%.2f", nonCityPercent) + "%)");
                }


                a.disconnect();



            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }



    }
}


/// All the countries in a continent organised by largest population to smallest.
/// All the countries in a region organised by largest population to smallest.
/// The top N populated countries in the world where N is provided by the user.
/// The top N populated countries in a continent where N is provided by the user.
/// The top N populated countries in a region where N is provided by the user.
/// All the cities in the world organised by largest population to smallest.
/// All the cities in a continent organised by largest population to smallest.
/// All the cities in a region organised by largest population to smallest.
/// All the cities in a country organised by largest population to smallest.
/// All the cities in a district organised by largest population to smallest.
/// The top N populated cities in the world where N is provided by the user.
/// The top N populated cities in a continent where N is provided by the user.