package com.mybnb.mybnb.interfaces;

import com.mybnb.mybnb.classes.listing.Listing;
import com.mybnb.mybnb.classes.user.Renter;
import com.mybnb.mybnb.classes.listing.SearchListings;
import org.apache.commons.cli.*;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SearchInterface {

    private static Connection currConnection;
    private static Renter currRenter;

    private static Set<String> allPossibleAmenities = new HashSet<>(Arrays.asList(
            "wifi", "kitchen", "washer", "dryer", "air_conditioning", "heating", "dedicated_workspace",
            "hair_dryer", "tv", "iron", "pool", "free_parking", "crib", "bbq_grill", "indoor_fireplace",
            "hot_tub", "ev_charger", "gym", "breakfast", "smoking_allowed", "beachfront", "waterfront",
            "smoke_alarm", "carbon_monoxide_alarm"
    ));
    public static void run(Connection connection, Scanner scanner, Renter renter) {

        currConnection = connection;
        currRenter = renter;

        while (true) {
            System.out.println("Search Menu:");
            System.out.println("1. Search via geo coordinates (lat, long)");
            System.out.println("2. Search via postal code");
            System.out.println("3. Search via exact address");
            System.out.println("4. Go Back");

            System.out.print("Please choose an option: ");

            int renterChoice = scanner.nextInt();
            scanner.nextLine();

            switch (renterChoice) {
                case 1:
                    searchByGeoCoordinates(scanner);
                    break;
                case 2:
                    searchPostalCode(scanner);
                    break;
                case 3:
                    // Implement leave review functionality
                    break;
                case 4:
                    System.out.println("Returning to prev menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    private static void searchByGeoCoordinates(Scanner scanner){

        SearchListings searchListings = new SearchListings(currConnection);

        System.out.println("Search Listings by Coordinates:");

        // Get search location details
        System.out.print("Enter search latitude: ");
        double searchLat = scanner.nextDouble();
        System.out.print("Enter search longitude: ");
        double searchLng = scanner.nextDouble();
        System.out.println();
        scanner.nextLine();

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        Options amenitiesOptions = new Options();
        amenitiesOptions.addOption(Option.builder("a")
                .hasArgs()
                .desc("List of amenities")
                .build());

        options.addOption("md", "max-distance", true, "Maximum distance");
        options.addOption("s", "sort-by", true, "Sort by (price or distance)");
        options.addOption("a", "ascending", true, "Sort ascending (true or false)");
        options.addOption("sd", "start-date", true, "Start date (yyyy-MM-dd)");
        options.addOption("ed", "end-date", true, "End date (yyyy-MM-dd)");
        options.addOption("minp", "min-price", true, "Minimum price");
        options.addOption("maxp", "max-price", true, "Maximum price");

        try {
            System.out.println("Optional filters");
            System.out.println("Max distance: -md,\nSort by: -s price OR -s distance \nAscending: " +
                    "-a true OR -a false");
            System.out.println("Dates (need start and end if using): -sd yyyy-MM-dd -ed yyyy-MM-dd\n" +
                    "Price (minimum / maximum): -minp _ AND / OR -maxp");
            CommandLine cmd = parser.parse(options, scanner.nextLine().split(" "));
            Double maxDistance = cmd.hasOption("md") ? Double.parseDouble(cmd.getOptionValue("md")) : 0.0;
            String sortBy = cmd.hasOption("s") ? cmd.getOptionValue("s") : null;
            Boolean sortByAscending = cmd.hasOption("a") ? Boolean.parseBoolean(cmd.getOptionValue("a")) : false;
            String startDateStr = cmd.hasOption("sd") ? cmd.getOptionValue("sd") : null;
            String endDateStr = cmd.hasOption("ed") ? cmd.getOptionValue("ed") : null;
            Integer minPrice = cmd.hasOption("minp") ? Integer.parseInt(cmd.getOptionValue("minp")) : 0;
            Integer maxPrice = cmd.hasOption("maxp") ? Integer.parseInt(cmd.getOptionValue("maxp")) : 0;

            // Date range
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;


            try {
                if(startDateStr != null){
                    startDate = dateFormat.parse(startDateStr);
                }
                if(endDateStr != null){
                    endDate = dateFormat.parse(endDateStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Amenities
            System.out.println("Step 4: List any amenities (comma-separated): ");
            String amenitiesInput = scanner.nextLine();

            List<String> amenities = Arrays.asList(amenitiesInput.split(","));

            amenities = amenities.stream()
                    .map(String::trim)
                    .collect(Collectors.toList());

            amenities = filterAmenities(amenities);

            boolean sortByPrice = false;
            boolean sortByDistance = false;
            if(sortBy != null) {
                if (sortBy.equals("price")) {
                    sortByPrice = true;
                } else if (sortBy.equals("distance")) {
                    sortByDistance = true;
                }
            }

            ArrayList<Listing> listings = searchListings.searchByLocationWithDistanceAndRanking(
                    searchLat, searchLng, maxDistance, sortByDistance, sortByPrice, sortByAscending,
                    startDate, endDate, minPrice, maxPrice, amenities);
            ArrayList<TableFormat> tableFormatListings = new ArrayList<>(listings);

            if (listings.isEmpty()) {
                System.out.println("Could not find any listings.");
                System.out.println();
            } else {
                TableOutput.formatTable(tableFormatListings);
                System.out.println();
            }

        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            System.out.println();
        }
    }

    private static void searchPostalCode(Scanner scanner){
        SearchListings searchListings = new SearchListings(currConnection);

        System.out.println("Search Listings by Coordinates:");

        // Get search location details
        System.out.print("Enter Postal Code: ");
        String postalCode = scanner.nextLine();
        scanner.nextLine();

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption("md", "max-distance", true, "Maximum distance");
        options.addOption("s", "sort-by", true, "Sort by (price or distance)");
        options.addOption("a", "ascending", true, "Sort ascending (true or false)");
        options.addOption("sd", "start-date", true, "Start date (yyyy-MM-dd)");
        options.addOption("ed", "end-date", true, "End date (yyyy-MM-dd)");
        options.addOption("minp", "min-price", true, "Minimum price");
        options.addOption("maxp", "max-price", true, "Maximum price");

        try {
            System.out.println("Optional filters");
            System.out.println("Max distance: -md,\nSort by: -s price OR -s distance \nAscending: " +
                    "-a true OR -a false");
            System.out.println("Dates (need start and end if using): -sd yyyy-MM-dd -ed yyyy-MM-dd\n" +
                    "Price (minimum / maximum): -minp _ AND / OR -maxp");
            CommandLine cmd = parser.parse(options, scanner.nextLine().split(" "));
            Double maxDistance = cmd.hasOption("md") ? Double.parseDouble(cmd.getOptionValue("md")) : 0.0;
            String sortBy = cmd.hasOption("s") ? cmd.getOptionValue("s") : null;
            Boolean sortByAscending = cmd.hasOption("a") ? Boolean.parseBoolean(cmd.getOptionValue("a")) : false;
            String startDateStr = cmd.hasOption("sd") ? cmd.getOptionValue("sd") : null;
            String endDateStr = cmd.hasOption("ed") ? cmd.getOptionValue("ed") : null;
            Integer minPrice = cmd.hasOption("minp") ? Integer.parseInt(cmd.getOptionValue("minp")) : 0;
            Integer maxPrice = cmd.hasOption("maxp") ? Integer.parseInt(cmd.getOptionValue("maxp")) : 0;

            // Date range
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;


            try {
                if(startDateStr != null){
                    startDate = dateFormat.parse(startDateStr);
                }
                if(endDateStr != null){
                    endDate = dateFormat.parse(endDateStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Amenities
            System.out.println("Step 4: List any amenities (comma-separated): ");
            String amenitiesInput = scanner.nextLine();

            List<String> amenities = Arrays.asList(amenitiesInput.split(","));

            amenities = amenities.stream()
                    .map(String::trim)
                    .collect(Collectors.toList());

            amenities = filterAmenities(amenities);

            boolean sortByPrice = false;
            boolean sortByDistance = false;
            if(sortBy != null) {
                if (sortBy.equals("price")) {
                    sortByPrice = true;
                } else if (sortBy.equals("distance")) {
                    sortByDistance = true;
                }
            }

            ArrayList<Listing> listings = searchListings.searchBySimilarPostalCode(
                    postalCode, sortByDistance, sortByPrice, sortByAscending,
                    startDate, endDate, minPrice, maxPrice, amenities);

            ArrayList<TableFormat> tableFormatListings = new ArrayList<>(listings);

            if (listings.isEmpty()) {
                System.out.println("Could not find any listings.");
                System.out.println();
            } else {
                TableOutput.formatTable(tableFormatListings);
                System.out.println();
            }

        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            System.out.println();
        }
    }

    private static void searchByExactAddress(Scanner scanner){

        SearchListings searchListings = new SearchListings(currConnection);

        System.out.println("Search Listings Exact Address:");

        // Get search location details
        System.out.print("Enter Addr1: ");
        String add1 = scanner.nextLine();
        System.out.print("Enter Addr1: ");
        String add2 = scanner.nextLine();
        System.out.print("Enter City: ");
        String city = scanner.nextLine();
        System.out.print("Enter State: ");
        String state = scanner.nextLine();
        System.out.print("Enter Country: ");
        String country = scanner.nextLine();
        scanner.nextLine();

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption("md", "max-distance", true, "Maximum distance");
        options.addOption("s", "sort-by", true, "Sort by (price or distance)");
        options.addOption("a", "ascending", true, "Sort ascending (true or false)");
        options.addOption("sd", "start-date", true, "Start date (yyyy-MM-dd)");
        options.addOption("ed", "end-date", true, "End date (yyyy-MM-dd)");
        options.addOption("minp", "min-price", true, "Minimum price");
        options.addOption("maxp", "max-price", true, "Maximum price");

        try {
            System.out.println("Optional filters");
            System.out.println("Max distance: -md,\nSort by: -s price OR -s distance \nAscending: " +
                    "-a true OR -a false");
            System.out.println("Dates (need start and end if using): -sd yyyy-MM-dd -ed yyyy-MM-dd\n" +
                    "Price (minimum / maximum): -minp _ AND / OR -maxp");
            CommandLine cmd = parser.parse(options, scanner.nextLine().split(" "));
            Double maxDistance = cmd.hasOption("md") ? Double.parseDouble(cmd.getOptionValue("md")) : 0.0;
            String sortBy = cmd.hasOption("s") ? cmd.getOptionValue("s") : null;
            Boolean sortByAscending = cmd.hasOption("a") ? Boolean.parseBoolean(cmd.getOptionValue("a")) : false;
            String startDateStr = cmd.hasOption("sd") ? cmd.getOptionValue("sd") : null;
            String endDateStr = cmd.hasOption("ed") ? cmd.getOptionValue("ed") : null;
            Integer minPrice = cmd.hasOption("minp") ? Integer.parseInt(cmd.getOptionValue("minp")) : 0;
            Integer maxPrice = cmd.hasOption("maxp") ? Integer.parseInt(cmd.getOptionValue("maxp")) : 0;

            // Date range
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;


            try {
                if(startDateStr != null){
                    startDate = dateFormat.parse(startDateStr);
                }
                if(endDateStr != null){
                    endDate = dateFormat.parse(endDateStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Amenities
            System.out.println("Step 4: List any amenities (comma-separated): ");
            String amenitiesInput = scanner.nextLine();

            List<String> amenities = Arrays.asList(amenitiesInput.split(","));

            amenities = amenities.stream()
                    .map(String::trim)
                    .collect(Collectors.toList());

            amenities = filterAmenities(amenities);

            boolean sortByPrice = false;
            boolean sortByDistance = false;
            if(sortBy != null) {
                if (sortBy.equals("price")) {
                    sortByPrice = true;
                } else if (sortBy.equals("distance")) {
                    sortByDistance = true;
                }
            }

            ArrayList<Listing> listings = searchListings.searchByAddressParameters(
                    add1, add2, city, state, country, sortByDistance, sortByPrice, sortByAscending,
                    startDate, endDate, minPrice, maxPrice, amenities);

            ArrayList<TableFormat> tableFormatListings = new ArrayList<>(listings);

            if (listings.isEmpty()) {
                System.out.println("Could not find any listings.");
                System.out.println();
            } else {
                TableOutput.formatTable(tableFormatListings);
                System.out.println();
            }

        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            System.out.println();
        }
    }

    public static List<String> filterAmenities(List<String> amenities) {
        List<String> filteredAmenities = new ArrayList<>();
        for (String amenity : amenities) {
            if (allPossibleAmenities.contains(amenity)) {
                filteredAmenities.add(amenity);
            }
        }
        return filteredAmenities;
    }

}
