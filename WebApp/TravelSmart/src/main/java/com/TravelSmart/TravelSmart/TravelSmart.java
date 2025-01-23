package com.TravelSmart.TravelSmart;
import com.TravelSmart.TravelSmart.util.SendAirportList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class TravelSmart {
    List<Flight> flights;
    FlightSearch flightSearch = FlightSearch.getFlightSearch();


   @RequestMapping(path = "/flight", method = RequestMethod.GET)
    public ResponseEntity<Object> getFlightDetails(@RequestParam String src, @RequestParam String dest){
       try{

           flights = flightSearch.searchFlights(src, dest);
           return ResponseEntity.status(200).body(flights);
       }catch (Exception e){
           System.out.println(e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
       }
   }


}
