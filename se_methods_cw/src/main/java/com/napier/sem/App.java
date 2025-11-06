package com.napier.sem;
/* REFERENCE FOR REPORTS
A country report requires the following columns:

Code.
Name.
Continent.
Region.
Population.
Capital.

A city report requires the following columns:

Name.
Country.
District.
Population.

A capital city report requires the following columns:

Name.
Country.
Population.

For the population reports, the following information is requested:

The name of the continent/region/country.
The total population of the continent/region/country.
The total population of the continent/region/country living in cities (including a %).
The total population of the continent/region/country not living in cities (including a %).
 */
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
                // Wait a bit for db to start
                Thread.sleep(1000);
                // Connect to database
                // this may need to be changed back to db:3306 instead of localhost:33060 !!!
                con = DriverManager.getConnection("jdbc:mysql://localhost:33060/world?useSSL=false&allowPublicKeyRetrieval=true", "root", "example");
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

    public void test() {
        if (con != null) {
            try {

                // This is a basic statement to test connecting to the database.
                Statement stmt = con.createStatement();
                String strSelect = "SELECT capital, name "
                        + "FROM country "
                        + "LIMIT 10";
                ResultSet rset = stmt.executeQuery(strSelect);

                while (rset.next()) {
                    Country country = new Country();
                    country.capital = rset.getString("capital");
                    country.name = rset.getString("name");
                    System.out.println("Name = " + country.name + ", Capital = " + country.capital);
                }
            }
            catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }
    }

    public void test2() {
        if (con != null) {
            try {
                Statement stmt2 = con.createStatement();
                String strSelect2 = "SELECT name, countrycode, district, population "
                        + "FROM city "
                        + "LIMIT 10";
                ResultSet rset2 = stmt2.executeQuery(strSelect2);

                while (rset2.next()) {
                    City city = new City();
                    city.name = rset2.getString("name");
                    // city.countryCode = rset2.getInt("countryCode");
                    // ", Country = " + city.countryCode +
                    city.district = rset2.getString("district");
                    city.population = rset2.getInt("population");
                    System.out.println("Name = " + city.name + ", District = " + city.district + ", Population = " + city.population);
                }
            }
            catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }
    }

    public void test3() {
        if (con != null) {
            try {
                Statement stmt3 = con.createStatement();

                String strSelect3 = "Select country.name, city.name AS capital "
                        + "FROM country "
                        + "INNER JOIN city ON country.capital = city.id "
                        + "LIMIT 10";

                ResultSet rset3 = stmt3.executeQuery(strSelect3);

                while (rset3.next()) {
                    Country country3 = new Country();
                    country3.capital = rset3.getString("capital");
                    country3.name = rset3.getString("name");
                    System.out.println("Name = " + country3.name + ", Capital = " + country3.capital);
                }
            }
            catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }
    }

// The top N populated cities in a region where N is provided by the user.

    public void top_pop_region_user_input(int limit){
        if (con != null){
            try{
                Statement top_pop_region = con.createStatement();
                // First part of statement is straight forward - select relevant info based to display at end
                String strSelect_top_pop_region = "SELECT country1.name, city1.name as city_name, country1.region, city1.district AS district, city1.population AS population "
                        + "FROM country AS country1 "
                        // join on the city table, as it contains relevant population info
                        + "JOIN city AS city1 ON country1.code = city1.countryCode "
                        // we then do a subquery - counting how many other cities in the same region have a larger population
                        // if it's less than the limit set by the user, then it has the correct outcome for that region
                        + "WHERE ( "
                            + "SELECT COUNT(*) FROM country AS country2 "
                            + "JOIN city AS city2 ON country2.code = city2.countryCode "
                            + "WHERE country2.region = country1.region AND city2.population > city1.population) "
                        + "< " + limit
                        + " ORDER BY country1.region, city1.population DESC";

                ResultSet results_top_pop_region = top_pop_region .executeQuery(strSelect_top_pop_region);

                // null string for formatting results
                String lastRegion = null;

                while (results_top_pop_region.next()) {
                    Country country = new Country();
                    City city = new City();
                    city.name = results_top_pop_region.getString("city_name");
                    city.district = results_top_pop_region.getString("district");
                    country.name = results_top_pop_region.getString("name");
                    country.region = results_top_pop_region.getString("region");
                    country.population = results_top_pop_region.getInt("population");

                    // if lastRegion is null (like at the start) or is not equal to the current value of region
                    // this formatting can be safely removed, doesn't affect anything important
                    if (lastRegion == null || !lastRegion.equals(country.region)) {
                        // print new line and a tag showing name of new region
                        System.out.println("\n... Region: " + country.region + " ...");
                        // then set lastRegion to current region
                        lastRegion = country.region;
                    }

                    System.out.println("Country : " + country.name + " | City : " + city.name + " | District : " + city.district +
                            " | Region : " + country.region + " | Population : " + country.population);
                }
            } // end of try
            catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }// end of if
    } // end of function

//The top N populated cities in a country where N is provided by the user.
public void top_pop_country_user_input(int limit){
    if (con != null){
        try{
            Statement top_pop_country = con.createStatement();
            String strSelect_top_pop_country = "SELECT country1.name, city1.name AS city_name, country1.region, city1.district AS district, city1.population AS population "
                    + "FROM country AS country1 "
                    + "JOIN city AS city1 ON country1.code = city1.countryCode "
                    + "WHERE ( "
                        + "SELECT COUNT(*) FROM city AS city2 "
                        + "WHERE city2.countryCode = country1.code AND city2.population > city1.population) "
                    + "< " + limit
                    + " ORDER BY country1.region, city1.population DESC";

            ResultSet results_top_pop_country= top_pop_country .executeQuery(strSelect_top_pop_country);

            String lastCountry = null;

            while (results_top_pop_country.next()) {
                Country country = new Country();
                City city = new City();
                city.name = results_top_pop_country.getString("city_name");
                city.district = results_top_pop_country.getString("district");
                country.name = results_top_pop_country.getString("name");
                country.region = results_top_pop_country.getString("region");
                country.population = results_top_pop_country.getInt("population");

                if (lastCountry == null || !lastCountry.equals(country.name)) {
                    // print new line and a tag showing name of new region
                    System.out.println("\n... Country: " + country.name + " ...");
                    // then set lastRegion to current region
                    lastCountry = country.name;
                }

                System.out.println("Country = " + country.name + " | City = " + city.name + " | District = " + city.district +
                        " | Region = " + country.region + " | Population = " + country.population);
            }
        } // end of try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }// end of if
} // end of function
//The top N populated cities in a district where N is provided by the user.
//All the capital cities in the world organised by largest population to smallest.
//All the capital cities in a continent organised by largest population to smallest.
//All the capital cities in a region organised by largest to smallest.
//The top N populated capital cities in the world where N is provided by the user.
//The top N populated capital cities in a continent where N is provided by the user.
//The top N populated capital cities in a region where N is provided by the user.
//The population of people, people living in cities, and people not living in cities in each continent.
//The population of people, people living in cities, and people not living in cities in each region.
//The population of people, people living in cities, and people not living in cities in each country.

    public static void main(String[] args) {

        App a = new App();

        a.connect();
        if (a.con != null) {

                a.top_pop_region_user_input(3);
                a.top_pop_country_user_input(3);

                a.disconnect();

        }
    }
}