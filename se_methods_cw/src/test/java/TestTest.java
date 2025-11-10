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
       int limit = 4;
       try {
           app.top_pop_country_user_input(limit);
       } catch (Exception e) {
           fail("The method threw an exception: " + e.getMessage());
       }
   }
    // this test checks that the function top_pop_region_user_input correctly handles an invalid input
   @Test
    public void top_pop_region_invalid_input(){
       int limit = -1;
       try {
           app.top_pop_country_user_input(limit);
       } catch (Exception e) {
           fail("The method threw an exception: " + e.getMessage());
       }
   }
}