import com.napier.sem.App;
import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;


class MyTest
{
   private App app;
   private Connection con;

   @BeforeEach
    public void start_up(){
       app = new App();
       app.connect();
       con = app.getConnection();
   }

   @AfterEach
    public void shut_down(){
       app.disconnect();
   }

   // this test checks that the function top_pop_region_user_input correctly handles a valid input
   @Test
    public void top_pop_region_valid_input(){
       int valid_limit = 4;
           app.top_pop_country_user_input(valid_limit);
   }

    // this test checks that the function top_pop_region_user_input correctly handles an invalid input
   @Test
    public void top_pop_region_invalid_input(){
       int invalid_limit = -1;
       app.top_pop_country_user_input(invalid_limit);
   }

    // tests valid input in the top_pop_country_user_input function
    @Test
    public void top_pop_country_valid_input(){
        int valid_limit = 5;
        app.top_pop_country_user_input(valid_limit);

    }

    // tests invalid input in the top_pop_country_user_input function
    @Test
    public void top_pop_country_invalid_input(){
        int invalid_limit = -2;
        app.top_pop_country_user_input(invalid_limit);
    }

    // same again but for all_capital_world_user_input function
    @Test
    public void all_capital_world_valid_input(){
        int valid_limit = 5;
        app.all_capital_world_user_input(valid_limit);
    }

    // and an invalid one
    @Test
    public void all_capital_world_invalid_input(){
        int invalid_limit = -23;
        app.all_capital_world_user_input(invalid_limit);
    }
    // and the rest, following the same pattern
   @Test
    public void top_pop_district_valid_input(){
       int valid_limit = 5;
       app.top_pop_district_user_input(valid_limit);
   }

    @Test
    public void top_pop_district_invalid_input(){
        int invalid_limit = 0;
        app.top_pop_district_user_input(invalid_limit);
    }

    @Test
    public void all_capital_world_by_continent_valid_input(){
        int valid_limit = 2;
        app.all_capital_world_by_continent_user_input(valid_limit);
    }

    @Test
    public void all_capital_world_by_continent_invalid_input(){
        int valid_limit = -2;
        app.all_capital_world_by_continent_user_input(valid_limit);
    }

    @Test
    public void all_capital_world_by_region_valid_input(){
        int valid_limit = 2;
        app.all_capital_world_by_region_user_input(valid_limit);
    }

    @Test
    public void all_capital_world_by_region_invalid_input(){
        int invalid_limit = -21213413;
        app.all_capital_world_by_region_user_input(invalid_limit);
    }

}
