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

    /* public this is going to be a function that executes a predefined SQL query

                    // This is a basic statement to test connecting to the database. I have no idea why capital is returning a number
                Statement stmt = a.con.createStatement();
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

// The top N populated cities in a region where N is provided by the user.
//The top N populated cities in a country where N is provided by the user.
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
    */
    public static void main(String[] args) {

        App a = new App();

        a.connect();
        if (a.con != null) {
            try {

                // This is a basic statement to test connecting to the database. I have no idea why capital is returning a number
                Statement stmt = a.con.createStatement();
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

                System.out.println("\nTHIS IS THE END OF THE FIRST QUERY\n");

                Statement stmt2 = a.con.createStatement();
                String strSelect2 = "SELECT name, countrycode, district, population "
                        + "FROM city "
                        + "LIMIT 10";
                ResultSet rset2 = stmt2.executeQuery(strSelect2);

                while (rset2.next()) {
                    City city = new City();
                    city.name = rset2.getString("name");
                    // city.countrycode = rset2.getInt("countrycode");
                    // ", Country = " + city.countrycode +
                    city.district = rset2.getString("district");
                    city.population = rset2.getInt("population");
                    System.out.println("Name = " + city.name + ", District = " + city.district + ", Population = " + city.population);
                }

                System.out.println("\nTHIS IS THE END OF THE SECOND QUERY\n");
                // this is a test to try joining city to country

                Statement stmt3 = a.con.createStatement();

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
                System.out.println("\nTHIS IS THE END OF THE THIRD QUERY\n");
                a.disconnect();

            } catch (SQLException e) {
                System.out.println("SQL error occurred: " + e.getMessage());
            }

        }
    }
}