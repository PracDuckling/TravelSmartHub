package org.example;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

// Strategy Interface for Sorting
interface FlightSortingStrategy {
    void sort(List<Flight> flights);
}

// Concrete Strategy: Sort by Fare
class SortByFare implements FlightSortingStrategy {
    @Override
    public void sort(List<Flight> flights) {
        flights.sort(Comparator.comparingDouble(f -> f.fare));
    }
}

// Concrete Strategy: Sort by Duration
class SortByDuration implements FlightSortingStrategy {
    @Override
    public void sort(List<Flight> flights) {
        flights.sort(Comparator.comparingInt(f -> f.duration));
    }
}

// Concrete Strategy: Sort by Fare and Duration
class SortByFareAndDuration implements FlightSortingStrategy {
    @Override
    public void sort(List<Flight> flights) {
        flights.sort(Comparator.comparingDouble((Flight f) -> f.fare)
                .thenComparingInt(f -> f.duration));
    }
}

// Context Class to Use Sorting Strategies
class FlightSorter {
    private FlightSortingStrategy strategy;

    public void setStrategy(FlightSortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sortFlights(List<Flight> flights) {
        if (strategy != null) {
            strategy.sort(flights);
        }
    }
}

// Main FlightSearch Class
public class FlightSearch {

    public static Map<String, String> loadCityMapping(String cityFile) {
        Map<String, String> cityMapping = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(cityFile))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String abbreviation = fields[0].trim();
                String cityName = fields[1].trim().toLowerCase(); // Use lowercase for case-insensitivity
                cityMapping.put(cityName, abbreviation);
            }
        } catch (IOException e) {
            System.out.println("Error reading city mapping file: " + cityFile);
        }
        return cityMapping;
    }

    public static List<Flight> loadFlightData(String flightFile) {
        List<Flight> flightData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(flightFile))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String flightNumber = fields[0].trim();
                String sourceCity = fields[1].trim();
                String destinationCity = fields[2].trim();
                double fare = Double.parseDouble(fields[3].trim());
                int duration = Integer.parseInt(fields[4].trim());
                String airline = fields[5].trim();

                flightData.add(new Flight(flightNumber, sourceCity, destinationCity, fare, duration, airline));
            }
        } catch (IOException e) {
            System.out.println("Error reading flight data file: " + flightFile);
            System.out.println(e.getMessage());
        }
        return flightData;
    }

    public static List<Flight> searchFlights(List<Flight> flightData, String source, String destination) {
        return flightData.stream()
                .filter(f -> f.sourceCity.equals(source) && f.destinationCity.equals(destination))
                .collect(Collectors.toList());
    }

    public static void displayFlights(List<Flight> flights) {
        if (flights.isEmpty()) {
            System.out.println("No flights available for the selected route.");
            return;
        }
        System.out.printf("%-15s %-15s %-20s %-10s %-10s %-10s\n", "Flight Number", "Source", "Destination", "Fare", "Duration", "Airline");
        System.out.println("--------------------------------------------------------------------------------------");
        flights.forEach(System.out::println);
    }

    public static void getDetails() {
        String flightFile = "src/main/resources/DATA.csv";
        String cityFile = "src/main/resources/CITIES.xlsx.csv";

        List<Flight> flightData = loadFlightData(flightFile);
        Map<String, String> cityMapping = loadCityMapping(cityFile);

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter Source City: ");
        String sourceCityInput = scanner.nextLine().trim().toLowerCase();
        System.out.print("Enter Destination City: ");
        String destinationCityInput = scanner.nextLine().trim().toLowerCase();

        try {
            if (sourceCityInput.equals(destinationCityInput)) {
                System.out.println("Source and destination cannot be the same. Please try again.");
                return;
            }

            String sourceCity = cityMapping.get(sourceCityInput);
            String destinationCity = cityMapping.get(destinationCityInput);

            if (sourceCity == null || destinationCity == null) {
                System.out.println("Invalid city names. Please try again.");
                return;
            }

            List<Flight> flights = searchFlights(flightData, sourceCity, destinationCity);

            System.out.print("Sort Criteria:\n1. Fare\n2. Duration\n3. Fare and Duration\nPlease Select the Sort Criteria: ");
            int sortCriteria = scanner.nextInt();

            // Use FlightSorter with the Strategy Pattern
            FlightSorter sorter = new FlightSorter();
            switch (sortCriteria) {
                case 1:
                    sorter.setStrategy(new SortByFare());
                    break;
                case 2:
                    sorter.setStrategy(new SortByDuration());
                    break;
                case 3:
                    sorter.setStrategy(new SortByFareAndDuration());
                    break;
                default:
                    System.out.println("Invalid sort criteria. Defaulting to Fare.");
                    sorter.setStrategy(new SortByFare());
            }

            sorter.sortFlights(flights);

            System.out.println("\nAvailable Flights:");
            displayFlights(flights);

            System.out.print("\nWhich flight would you like to book (Enter the number): ");
            int bookedFlight = scanner.nextInt();

            System.out.print("How many tickets would you like to book: ");
            int totalTickets = scanner.nextInt();

            if (bookedFlight > 0 && bookedFlight <= flights.size()) {
                Flight selectedFlight = flights.get(bookedFlight - 1);
                double totalCost = selectedFlight.fare * totalTickets;
                String flightInfo = String.format(
                        "Flight Number: %s\nSource City: %s\nDestination City: %s\nFare: %.2f\nDuration: %d\nAirline: %s\nTotal Tickets: %d\nTotal Cost: %.2f",
                        selectedFlight.flightNumber, selectedFlight.sourceCity, selectedFlight.destinationCity,
                        selectedFlight.fare, selectedFlight.duration, selectedFlight.airline, totalTickets, totalCost);

                try (FileWriter writer = new FileWriter("ticket.txt")) {
                    writer.write("Ticket Details\n");
                    writer.write("====================\n");
                    writer.write(flightInfo);
                    System.out.println("\nTicket generated successfully!");
                } catch (IOException e) {
                    System.out.println("Error while generating the ticket: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid flight selection.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args) {
        getDetails();
    }
}

