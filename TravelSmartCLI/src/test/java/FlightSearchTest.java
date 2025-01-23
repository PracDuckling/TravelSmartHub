import org.example.Flight;
import org.example.FlightSearch;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FlightSearchTest {

    @Test
//    check if the cities are loading correctly or not
    public void testLoadCityMapping() {
        String cityFile = "src/main/resources/CITIES.xlsx.csv";
        Map<String, String> cityMapping = FlightSearch.loadCityMapping(cityFile);

        assertNotNull(cityMapping);
        assertEquals("DEL", cityMapping.get("delhi"));
        assertEquals("PNQ", cityMapping.get("pune"));

        System.out.println("Test 1 Working");
    }

    @Test
//    check if the Flight data is loading correctly or not
    public void testLoadFlightData() {
        String flightFile = "src/main/resources/DATA.csv";
        List<Flight> flightData = FlightSearch.loadFlightData(flightFile);

        assertNotNull(flightData);
        assertFalse(flightData.isEmpty());
        assertEquals("1", flightData.getFirst().flightNumber);
        System.out.println("Test 2 working");
    }


    @Test
    public void testSearchFlights() {
        //input example flights
        List<Flight> flightData = Arrays.asList(
                new Flight("AA123", "NYC", "LAX", 300.0, 360, "American Airlines"),
                new Flight("DL456", "NYC", "LAX", 350.0, 370, "Delta Airlines")
        );

        List<Flight> result = FlightSearch.searchFlights(flightData, "NYC", "LAX");

        assertNotNull(result);
        assertEquals(2, result.size());
        System.out.println("Test 3 Working");
    }

    @Test
    public void testDisplayFlights() {
        List<Flight> flights = Arrays.asList(
                new Flight("AA123", "NYC", "LAX", 300.0, 360, "American Airlines"),
                new Flight("DL456", "NYC", "LAX", 350.0, 370, "Delta Airlines")
        );

        FlightSearch.displayFlights(flights);
        System.out.println("Test 4 working ");
    }
}