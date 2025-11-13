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
                // this may need to be changed back to db:3306 instead of localhost:33060 !!! - changed back to db:3306 for github actions
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

    // Getter for the private con field - might be required for testing
    public Connection getConnection() {
        return con;
    }

// The top N populated cities in a region where N is provided by the user.

    public void top_pop_region_user_input(int limit){
        if (limit <= 0) {
            System.out.println("Invalid limit - must be greater than 0");
            return;
        }

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

                ResultSet results_top_pop_region = top_pop_region.executeQuery(strSelect_top_pop_region);

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

                    System.out.println("Country : " + country.name + " || City : " + city.name + " || District : " + city.district +
                            " || Region : " + country.region + " || Population : " + country.population);
                }
                System.out.println("\n... End of Query ...");
            } // end of try
            catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }
        }// end of if
    } // end of function

//The top N populated cities in a country where N is provided by the user.
public void top_pop_country_user_input(int limit){
    if (limit <= 0) {
        System.out.println("Invalid limit - must be greater than 0");
        return;
    }
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
                    System.out.println("\n... Country: " + country.name + " ...");
                    lastCountry = country.name;
                }

                System.out.println("Country = " + country.name + " || City = " + city.name + " || District = " + city.district +
                        " || Region = " + country.region + " || Population = " + country.population);
            }
            System.out.println("\n... End of Query ...");
        } // end of try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }// end of if
} // end of function

//The top N populated cities in a district where N is provided by the user.
public void top_pop_district_user_input(int limit){
    if (limit <= 0) {
        System.out.println("Invalid limit - must be greater than 0");
        return;
    }
    if (con != null){
        try{
            Statement top_pop_district = con.createStatement();

            String strSelect_top_pop_district = "SELECT country1.name, city1.name as city_name, country1.region, city1.district AS district, city1.population AS population "
                    + "FROM country AS country1 "
                    + "JOIN city AS city1 ON country1.code = city1.countryCode "
                    + "WHERE ( "
                    + "SELECT COUNT(*) FROM city AS city2 "
                    + "WHERE city2.district = city1.district AND city2.population > city1.population) "
                    + "< " + limit
                    + " ORDER BY city1.district, city1.population DESC";

            ResultSet results_top_pop_district = top_pop_district.executeQuery(strSelect_top_pop_district);

            // null string for formatting results
            String lastDistrict = null;

            while (results_top_pop_district.next()) {
                Country country = new Country();
                City city = new City();
                city.name = results_top_pop_district.getString("city_name");
                city.district = results_top_pop_district.getString("district");
                country.name = results_top_pop_district.getString("name");
                country.region = results_top_pop_district.getString("region");
                country.population = results_top_pop_district.getInt("population");

                if (lastDistrict == null || !lastDistrict.equals(city.district)) {
                    System.out.println("\n... District: " + city.district + " ...");
                    lastDistrict = city.district;
                }

                System.out.println("Country : " + country.name + " || City : " + city.name + " || District : " + city.district +
                        " || Region : " + country.region + " || Population : " + country.population);
            }
            System.out.println("\n... End of Query ...");
        } // end of try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }
    }// end of if
} // end of function

//All the capital cities in the world organised by largest population to smallest.
    public void all_capital_world() {
        if (con != null) {
            try {
                Statement all_capital_world = con.createStatement();

                String strSelect_all_capital_world = "SELECT country1.name, city1.name as city_name, country1.region, city1.district AS district, city1.population AS population "
                        + "FROM country AS country1 "
                        + "JOIN city AS city1 ON country1.capital = city1.id "
                        + "ORDER BY population DESC ";

                ResultSet results_all_capital_world = all_capital_world.executeQuery(strSelect_all_capital_world);
                while (results_all_capital_world.next()) {
                    Country country = new Country();
                    City city = new City();
                    city.name = results_all_capital_world.getString("city_name");
                    city.district = results_all_capital_world.getString("district");
                    country.name = results_all_capital_world.getString("name");
                    country.region = results_all_capital_world.getString("region");
                    country.population = results_all_capital_world.getInt("population");

                    System.out.println("Country : " + country.name + " || City : " + city.name + " || District : " + city.district +
                            " || Region : " + country.region + " || Population : " + country.population);
                } // end of while

            } // end try
            catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }// end catch
        }// end if
    }// end func

//All the capital cities in a continent organised by largest population to smallest.
public void all_capital_world_by_continent() {
    if (con != null) {
        try {
            Statement all_capital_world_by_continent = con.createStatement();

            String strSelect_all_capital_world_by_continent = "SELECT country1.name, city1.name as city_name, country1.continent, country1.region, city1.district AS district, city1.population AS population "
                    + "FROM country AS country1 "
                    + "JOIN city AS city1 ON country1.capital = city1.id "
                    + "ORDER BY country1.continent, population DESC ";

            ResultSet results_all_capital_world_by_continent = all_capital_world_by_continent.executeQuery(strSelect_all_capital_world_by_continent);

            // null string for formatting results
            String lastContinent = null;

            while (results_all_capital_world_by_continent.next()) {
                Country country = new Country();
                City city = new City();
                city.name = results_all_capital_world_by_continent.getString("city_name");
                city.district = results_all_capital_world_by_continent.getString("district");
                country.continent = results_all_capital_world_by_continent.getString("continent");
                country.name = results_all_capital_world_by_continent.getString("name");
                country.region = results_all_capital_world_by_continent.getString("region");
                country.population = results_all_capital_world_by_continent.getInt("population");

                if (lastContinent == null || !lastContinent.equals(country.continent)) {
                    System.out.println("\n... Continent: " + country.continent + " ...");
                    lastContinent = country.continent;
                }

                // note: not printing continent as it's a little redundant
                System.out.println("Country : " + country.name + " || City : " + city.name +  " || District : " + city.district +
                        " || Region : " + country.region + " || Population : " + country.population);
            } // end of while

        } // end try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }// end catch
    }// end if
}// end func


//All the capital cities in a region organised by largest to smallest.
public void all_capital_world_by_region() {
    if (con != null) {
        try {
            Statement all_capital_world_by_region = con.createStatement();

            String strSelect_all_capital_world_by_region = "SELECT country1.name, city1.name as city_name, country1.region, city1.district AS district, city1.population AS population "
                    + "FROM country AS country1 "
                    + "JOIN city AS city1 ON country1.capital = city1.id "
                    + "ORDER BY country1.region, population DESC ";

            ResultSet results_all_capital_world_by_region = all_capital_world_by_region.executeQuery(strSelect_all_capital_world_by_region);

            // null string for formatting results
            String lastRegion = null;

            while (results_all_capital_world_by_region.next()) {
                Country country = new Country();
                City city = new City();
                city.name = results_all_capital_world_by_region.getString("city_name");
                city.district = results_all_capital_world_by_region.getString("district");
                //country.continent = results_all_capital_world_by_region.getString("continent");
                country.name = results_all_capital_world_by_region.getString("name");
                country.region = results_all_capital_world_by_region.getString("region");
                country.population = results_all_capital_world_by_region.getInt("population");

                if (lastRegion == null || !lastRegion.equals(country.region)) {
                    System.out.println("\n... Region: " + country.region + " ...");
                    lastRegion = country.region;
                }

                System.out.println("Country : " + country.name + " || City : " + city.name +  " || District : " + city.district +
                        " || Region : " + country.region + " || Population : " + country.population);
            } // end of while

        } // end try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }// end catch
    }// end if
}// end func

//The top N populated capital cities in the world where N is provided by the user.
public void all_capital_world_user_input(int limit) {
    if (limit <= 0) {
        System.out.println("Invalid limit - must be greater than 0");
        return;
    }
    if (con != null) {
        try {
            Statement all_capital_world_user_input = con.createStatement();

            String strSelect_all_capital_world_user_input = "SELECT country1.name, city1.name as city_name, country1.region, city1.district AS district, city1.population AS population "
                    + "FROM country AS country1 "
                    + "JOIN city AS city1 ON country1.capital = city1.id "
                    + "ORDER BY population DESC "
                    + "LIMIT " + limit;

            ResultSet results_all_capital_world_user_input = all_capital_world_user_input.executeQuery(strSelect_all_capital_world_user_input);
            while (results_all_capital_world_user_input.next()) {
                Country country = new Country();
                City city = new City();
                city.name = results_all_capital_world_user_input.getString("city_name");
                city.district = results_all_capital_world_user_input.getString("district");
                country.name = results_all_capital_world_user_input.getString("name");
                country.region = results_all_capital_world_user_input.getString("region");
                country.population = results_all_capital_world_user_input.getInt("population");

                System.out.println("Country : " + country.name + " || City : " + city.name + " || District : " + city.district +
                        " || Region : " + country.region + " || Population : " + country.population);
            } // end of while

        } // end try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }// end catch
    }// end if
}// end func

//The top N populated capital cities in a continent where N is provided by the user.
public void all_capital_world_by_continent_user_input(int limit) {
    if (con != null) {
        try {
            Statement all_capital_world_by_continent_user_input = con.createStatement();

            String strSelect_all_capital_world_by_continent_user_input = "SELECT country1.name, city1.name as city_name, country1.continent, country1.region, city1.district AS district, city1.population AS population "
                    + "FROM country AS country1 "
                    + "JOIN city AS city1 ON country1.capital = city1.id "
                    + "WHERE ( "
                    + "SELECT COUNT(*) FROM country AS country2 "
                    + "JOIN city AS city2 ON country2.capital = city2.id "
                    + "WHERE country2.continent = country1.continent AND city2.population > city1.population) "
                    + "< " + limit
                    + " ORDER BY country1.continent, city1.population DESC ";


            ResultSet results_all_capital_world_by_continent_user_input = all_capital_world_by_continent_user_input.executeQuery(strSelect_all_capital_world_by_continent_user_input);

            // null string for formatting results
            String lastContinent = null;

            while (results_all_capital_world_by_continent_user_input.next()) {
                Country country = new Country();
                City city = new City();
                city.name = results_all_capital_world_by_continent_user_input.getString("city_name");
                city.district = results_all_capital_world_by_continent_user_input.getString("district");
                country.continent = results_all_capital_world_by_continent_user_input.getString("continent");
                country.name = results_all_capital_world_by_continent_user_input.getString("name");
                country.region = results_all_capital_world_by_continent_user_input.getString("region");
                country.population = results_all_capital_world_by_continent_user_input.getInt("population");

                if (lastContinent == null || !lastContinent.equals(country.continent)) {
                    System.out.println("\n... Continent: " + country.continent + " ...");
                    lastContinent = country.continent;
                }

                // note: not printing continent as it's a little redundant
                System.out.println("Country : " + country.name + " || City : " + city.name +  " || District : " + city.district +
                        " || Region : " + country.region + " || Population : " + country.population);
            } // end of while

        } // end try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }// end catch
    }// end if
}// end func

//The top N populated capital cities in a region where N is provided by the user.
public void all_capital_world_by_region_user_input(int limit) {
    if (limit <= 0) {
        System.out.println("Invalid limit - must be greater than 0");
        return;
    }
    if (con != null) {
        try {
            Statement all_capital_world_by_region_user_input = con.createStatement();

            String strSelect_all_capital_world_by_region_user_input = "SELECT country1.name, city1.name as city_name, country1.continent, country1.region, city1.district AS district, city1.population AS population "
                    + "FROM country AS country1 "
                    + "JOIN city AS city1 ON country1.capital = city1.id "
                    + "WHERE ( "
                    + "SELECT COUNT(*) FROM country AS country2 "
                    + "JOIN city AS city2 ON country2.capital = city2.id "
                    + "WHERE country2.region = country1.region AND city2.population > city1.population) "
                    + "< " + limit
                    + " ORDER BY country1.region, city1.population DESC ";


            ResultSet results_all_capital_world_by_region_user_input = all_capital_world_by_region_user_input.executeQuery(strSelect_all_capital_world_by_region_user_input);

            // null string for formatting results
            String lastRegion = null;

            while (results_all_capital_world_by_region_user_input.next()) {
                Country country = new Country();
                City city = new City();
                city.name = results_all_capital_world_by_region_user_input.getString("city_name");
                city.district = results_all_capital_world_by_region_user_input.getString("district");
                country.name = results_all_capital_world_by_region_user_input.getString("name");
                country.region = results_all_capital_world_by_region_user_input.getString("region");
                country.population = results_all_capital_world_by_region_user_input.getInt("population");

                if (lastRegion == null || !lastRegion.equals(country.region)) {
                    System.out.println("\n... Region: " + country.region + " ...");
                    lastRegion = country.region;
                }

                // note: not printing continent as it's a little redundant
                System.out.println("Country : " + country.name + " || City : " + city.name +  " || District : " + city.district +
                        " || Region : " + country.region + " || Population : " + country.population);
            } // end of while

        } // end try
        catch (SQLException e) {
            System.out.println("SQL error occurred: " + e.getMessage());
        }// end catch
    }// end if
}// end func

//The population of people, people living in cities, and people not living in cities in each continent.
//The population of people, people living in cities, and people not living in cities in each region.
//The population of people, people living in cities, and people not living in cities in each country.

    public static void main(String[] args) {

        App a = new App();

        a.connect();
        if (a.con != null) {

            System.out.println("\nRunning The top N populated cities in a region where N is provided by the user.\n");
            a.top_pop_region_user_input(3);
            System.out.println("\nThe top N populated cities in a country where N is provided by the user.\n");
            a.top_pop_country_user_input(3);
            System.out.println("\nThe top N populated cities in a district where N is provided by the user.\n");
            a.top_pop_district_user_input(3);
            System.out.println("\nAll the capital cities in the world organised by largest population to smallest.\n");
            a.all_capital_world();
            System.out.println("\nAll the capital cities in a continent organised by largest population to smallest.\n");
            a.all_capital_world_by_continent();
            System.out.println("\nAll the capital cities in a continent organised by largest population to smallest.\n");
            a.all_capital_world_by_region();
            System.out.println("\nThe top N populated capital cities in the world where N is provided by the user.\n");
            a.all_capital_world_user_input(5);
            System.out.println("\nThe top N populated capital cities in a continent where N is provided by the user.\n");
            a.all_capital_world_by_continent_user_input(4);
            System.out.println("\nThe top N populated capital cities in a region where N is provided by the user.\n");
            a.all_capital_world_by_region_user_input(6);
            System.out.println("\n\nAll queries finished\n\n");
            a.disconnect();

        }
    }
}