package com.TravelSmart.TravelSmart;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;


public class FlightSearch {
    private FlightSearch(){}

    static List<Flight> flightData;

    public static FlightSearch getFlightSearch(){
        FlightSearch flightSearch = new FlightSearch();
        flightSearch.loadFlightData();
        return flightSearch;
    }

    // Load flight data from CSV file into a list using threads
    private void loadFlightData() {
        String file = "data/DATA.csv";
        List<Flight> flightData = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(file);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            // Skip header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String flightNumber = fields[0];
                String source = fields[1];
                String destinationCity = fields[2];
                double fare = Double.parseDouble(fields[3]);
                int duration = Integer.parseInt(fields[4]);
                String airline = fields[5];
                flightData.add(new Flight(flightNumber, source, destinationCity, fare, duration, airline));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + file);
        }
        FlightSearch.flightData = flightData;
    }

    // Search for flights based on source and destination cities using streams
    public List<Flight> searchFlights(String source, String destinationCity) {
        return flightData.stream()
                .filter(flight -> flight.source.equalsIgnoreCase(source) && flight.destination.equalsIgnoreCase(destinationCity))
                .toList();
    }

    // Sort the flights based on the criteria (Fare, Duration, or both) using lambdas
    public List<Flight> sortFlights(List<Flight> flights, int sortBy) {
        Comparator<Flight> comparator;
        switch (sortBy) {
            case 1:
                comparator = Comparator.comparingDouble(f -> f.fare);
                break;
            case 2:
                comparator = Comparator.comparingInt(f -> f.duration);
                break;
            case 3:
                comparator = Comparator.comparingDouble((Flight f) -> f.fare)
                        .thenComparingInt(f -> f.duration);
                break;
            default:
                System.out.println("Invalid sort criteria. Showing unsorted results.");
                return flights;
        }
        return flights.stream().sorted(comparator).collect(Collectors.toList());
    }



    public List<String> getSourceAirports(){
        List<String> source = new ArrayList<>();
        flightData.forEach(flight -> source.add(flight.source));
        return source;
    }

    public List<String> getDestinationAirports(){
        List<String> destination = new ArrayList<>();
        flightData.forEach(flight -> destination.add(flight.destination));
        return destination;
    }
}

// Flight class definition
class Flight {
    String flightNumber;
    String source;
    String destination;
    double fare;
    int duration;
    String airline;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Flight(String flightNumber, String source, String destination, double fare, int duration, String airline) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.fare = fare;
        this.duration = duration;
        this.airline = airline;


    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-20s %-10.2f %-10d %-10s", flightNumber, source, destination, fare, duration, airline);
    }
}
