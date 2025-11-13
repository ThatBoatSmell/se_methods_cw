import com.napier.sem.App;
import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    private App app;
    private Connection con;

    @BeforeEach
    @Test
    public void start_up(){
        app = new App();
        app.connect();
        con = app.getConnection();
    }

    @AfterEach
    public void shut_down(){
        app.disconnect();
    }

    @Test
    void test_all_capital_world() {
        app.all_capital_world();
    }
}
