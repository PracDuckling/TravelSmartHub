package org.example;

public class Flight {
    public String flightNumber;
    public String sourceCity;
    public String destinationCity;
    public double fare;
    public int duration;
    public String airline;

    public Flight(String flightNumber, String sourceCity, String destinationCity, double fare, int duration, String airline) {
        this.flightNumber = flightNumber;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.fare = fare;
        this.duration = duration;
        this.airline = airline;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-20s %-10.2f %-10d %-10s", flightNumber, sourceCity, destinationCity, fare, duration, airline);
    }
}
