import com.napier.sem.App;
import com.napier.sem.PopulationReport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    private App app;
    private Connection con;

    @BeforeEach
    public void setUp() {
        app = new App();
        app.connect();
        con = app.getConnection();
    }

    @AfterEach
    public void tearDown() {
        app.disconnect();
    }

    @Test
    void testDatabaseConnectionNotNull() {
        assertNotNull(con, "Connection should not be null after connect()");
    }

    @Test
    void testPopulationByContinentDataNotEmpty() {
        ArrayList<PopulationReport> reports = app.getPopulationByContinentData();

        assertNotNull(reports, "Reports list should not be null");
        assertFalse(reports.isEmpty(), "Reports list for continents should not be empty");

        for (PopulationReport report : reports) {
            assertNotNull(report, "Report should not be null");
            assertNotNull(report.name, "Continent name should not be null");
            assertTrue(report.totalPopulation >= 0, "Total population should be >= 0");
            assertEquals(
                    report.totalPopulation,
                    report.cityPopulation + report.nonCityPopulation,
                    "Total population should equal city + non-city population"
            );
        }
    }

    @Test
    void testPopulationByRegionDataNotEmpty() {
        ArrayList<PopulationReport> reports = app.getPopulationByRegionData();

        assertNotNull(reports, "Reports list should not be null");
        assertFalse(reports.isEmpty(), "Reports list for regions should not be empty");

        for (PopulationReport report : reports) {
            assertNotNull(report, "Report should not be null");
            assertNotNull(report.name, "Region name should not be null");
            assertTrue(report.totalPopulation >= 0, "Total population should be >= 0");
            assertEquals(
                    report.totalPopulation,
                    report.cityPopulation + report.nonCityPopulation,
                    "Total population should equal city + non-city population"
            );
        }
    }

    @Test
    void testPopulationByCountryDataNotEmpty() {
        ArrayList<PopulationReport> reports = app.getPopulationByCountryData();

        assertNotNull(reports, "Reports list should not be null");
        assertFalse(reports.isEmpty(), "Reports list for countries should not be empty");

        for (PopulationReport report : reports) {
            assertNotNull(report, "Report should not be null");
            assertNotNull(report.name, "Country name should not be null");
            assertTrue(report.totalPopulation >= 0, "Total population should be >= 0");
            assertEquals(
                    report.totalPopulation,
                    report.cityPopulation + report.nonCityPopulation,
                    "Total population should equal city + non-city population"
            );
        }
    }

    @Test
    void testContinentEndpointHtmlContainsTable() {
        // This calls the Spring @RequestMapping method directly, without MockMvc
        String html = app.getPopulationByContinent();

        assertNotNull(html, "HTML response should not be null");
        assertTrue(html.contains("<table>"), "HTML should contain a table");
        assertTrue(html.contains("Population by Continent"),
                "HTML should contain the report title");
    }

    @Test
    void testHomePageContainsLinks() {
        String html = app.home();

        assertNotNull(html, "Home HTML should not be null");
        assertTrue(html.contains("/population/continent"), "Home page should link to continent report");
        assertTrue(html.contains("/population/region"), "Home page should link to region report");
        assertTrue(html.contains("/population/country"), "Home page should link to country report");
    }
}
