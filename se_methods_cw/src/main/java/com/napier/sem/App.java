package com.napier.sem;

import java.sql.*;

public class App
{
    public static void main(String[] args)
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Connection to the database
        Connection con = null;
        int retries = 100;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
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
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }

        if (con != null)
        {
            try
            {
                // This is a basic statement to test connecting to the database. I have no idea why capital is returning a number
                Statement stmt = con.createStatement();
                String strSelect = "SELECT capital, name "
                        + "FROM country";
                ResultSet rset = stmt.executeQuery(strSelect);

                while(rset.next())
                {
                    Country country = new Country();
                    country.capital = rset.getString("capital");
                    country.name = rset.getString("name");
                    System.out.println("Name = " + country.name + ", Capital = " + country.capital);
                }

                System.out.println("THIS IS THE END OF THE FIRST QUERY");

                Statement stmt2 = con.createStatement();
                String strSelect2 = "SELECT name, countrycode, district, population "
                        + "FROM city";
                ResultSet rset2 = stmt2.executeQuery(strSelect2);

                while(rset2.next())
                {
                    City city = new City();
                    city.name = rset2.getString("name");
                   // city.countrycode = rset2.getInt("countrycode");
                    // ", Country = " + city.countrycode +
                    city.district = rset2.getString("district");
                    city.population = rset2.getInt("population");
                    System.out.println("Name = " + city.name + ", District = " + city.district  + ", Population = " + city.population);
                }

                System.out.println("THIS IS THE END OF THE SECOND QUERY");


                // Close connection
                con.close();

            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }
}