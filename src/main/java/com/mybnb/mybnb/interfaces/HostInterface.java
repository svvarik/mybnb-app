package com.mybnb.mybnb.interfaces;

import com.mybnb.mybnb.classes.booking.Booking;
import com.mybnb.mybnb.classes.booking.BookingService;
import com.mybnb.mybnb.classes.listing.Listing;
import com.mybnb.mybnb.classes.listing.ListingService;
import com.mybnb.mybnb.classes.listing.PropertyType;
import com.mybnb.mybnb.classes.review.Review;
import com.mybnb.mybnb.classes.review.ReviewService;
import com.mybnb.mybnb.classes.user.Host;
import com.mybnb.mybnb.classes.user.HostService;

import org.apache.commons.cli.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HostInterface {

    private static Connection currConnection;
    private static Host currHost;

    public static void run(Connection connection, Scanner scanner, Host host) {

        currConnection = connection;
        currHost = host;

        while (true) {
            System.out.println("Welcome back: " + host.getFirstName() + "!");
            System.out.println("1. Get Listings");
            System.out.println("2. Create Listing");
            System.out.println("3. Update Listing");
            System.out.println("4. Get Bookings");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Leave Review");
            System.out.println("7. Back to Main Menu");
            System.out.print("Please choose an option: ");

            int hostChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (hostChoice) {
                case 1:
                    getListings();
                    break;
                case 2:
                    createListing(scanner);
                    break;
                case 3:
                    getListings();
                    System.out.println("Type id of listing you want to update or type 0 to go back: ");
                    int listingid = scanner.nextInt();
                    scanner.nextLine();
                    if(listingid != 0){
                        updateListing(scanner, listingid);
                    }
                    break;
                case 4:
                    getBookings(scanner);
                    break;
                case 5:
                    cancelBooking(scanner);
                    break;
                case 6:
                    leaveReview(scanner);
                    break; // Go back to the main menu
                case 7:
                    return; // Go back to the main menu
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }


    private static void getListings() {
        ListingService listingService = new ListingService(currConnection);
        ArrayList<Listing> listings = listingService.getAllListingsByHost(currHost.getId());
        ArrayList<TableFormat> tableFormatListings = new ArrayList<>(listings);

        if (listings.isEmpty()) {
            System.out.println("You have no listings.");
            System.out.println();
        } else {
            TableOutput.formatTable(tableFormatListings);
        }
    }

    private static void createListing(Scanner scanner) {
        ListingService listingService = new ListingService(currConnection);

        CommandLineParser parser = new DefaultParser();

        Options stepOneOptions = new Options();
        stepOneOptions.addOption("t", true, "Listing title");
        stepOneOptions.addOption("p", true, "Base price");
        stepOneOptions.addOption("type", true, "Property type");

        Options stepTwoOptions = new Options();
        stepTwoOptions.addOption("lo", true, "Longitude");
        stepTwoOptions.addOption("la", true, "Latitude");

        Options stepThreeOptions = new Options();
        stepThreeOptions.addOption("al1", true, "Address line 1");
        stepThreeOptions.addOption("al2", true, "Address line 2");
        stepThreeOptions.addOption("pc", true, "Postal code");
        stepThreeOptions.addOption("ci", true, "City");
        stepThreeOptions.addOption("st", true, "State");
        stepThreeOptions.addOption("co", true, "Country");

        Options stepFourOptions = new Options();
        stepFourOptions.addOption(Option.builder("a")
                .hasArgs()
                .desc("List of amenities")
                .build());


        try {
            // Step 1: Set listing title, base price, property type
            System.out.println("Step 1: Set listing title, base price, and property type");
            CommandLine cmd = parser.parse(stepOneOptions, scanner.nextLine().split(" "));
            String title = cmd.getOptionValue("t");
            int basePrice = Integer.parseInt(cmd.getOptionValue("p"));
            String type = cmd.getOptionValue("type");
            PropertyType propertyType = PropertyType.valueOf(type);

            System.out.println("Listing title: " + title + " , " + "Base price: $" +
                    basePrice + " , " + "Property type: " + propertyType);
            System.out.println();

            // Step 2: Geo coordinates: longitude, latitude
            System.out.println("Step 2: Enter geo coordinates (longitude, latitude)");
            cmd = parser.parse(stepTwoOptions, scanner.nextLine().split(" "));
            double longitude = Double.parseDouble(cmd.getOptionValue("lo"));
            double latitude = Double.parseDouble(cmd.getOptionValue("la"));

            System.out.println("Longitude: " + longitude + ", " + "Latitude: " + latitude);
            System.out.println();

            // Step 3: Address: line1, line2, postal_code, city, state, country
            System.out.println("Step 3: Enter address details");
            cmd = parser.parse(stepThreeOptions, scanner.nextLine().split(" "));
            String addressLine1 = cmd.getOptionValue("al1");
            String addressLine2 = cmd.getOptionValue("al2");
            String postalCode = cmd.getOptionValue("pc");
            String city = cmd.getOptionValue("ci");
            String state = cmd.getOptionValue("st");
            String country = cmd.getOptionValue("co");

            System.out.println("Address line 1: " + addressLine1 + " , " + "Address line 2: " + addressLine2);
            System.out.println("Postal code: " + postalCode + " , " + "City: " + city);
            System.out.println("State: " + state + " , " + "Country: " + country);
            System.out.println();

            // Step 4: List any amenities
            System.out.println("Step 4: List any amenities (comma-separated)");
            cmd = parser.parse(stepFourOptions, scanner.nextLine().split(","));
            String[] amenities = cmd.getOptionValues("a");

            System.out.println("Amenities: " + String.join(", ", amenities));
            System.out.println();

            Listing newListing = listingService.createListingFromAmenities(title, currHost.getId(), basePrice,
                    propertyType, longitude, latitude, addressLine1, addressLine2, city, state,
                    country, postalCode, new ArrayList<>(Arrays.asList(amenities)));

            if (listingService.insertListing(newListing)) {
                System.out.println("Created listing");

                String[][] data = {
                        newListing.getRowValues()
                };
                TableOutput.outputTable(newListing.getHeaders(), data);
            } else {
                System.out.println("Something went wrong");
            }

        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
//            HelpFormatter formatter = new HelpFormatter();
//            formatter.printHelp("ListingCreator", );
        } catch (Exception e) {
            System.err.println("Oops, there was an error: " + e.getMessage());
        }
    }

    private static void updateListing(Scanner scanner, Integer listingId) {
        ListingService listingService = new ListingService(currConnection);

        CommandLineParser parser = new DefaultParser();

        Options stepOneOptions = new Options();
        stepOneOptions.addOption("t", true, "Listing title");
        stepOneOptions.addOption("p", true, "Base price");
        stepOneOptions.addOption("type", true, "Property type");

        Options stepTwoOptions = new Options();
        stepTwoOptions.addOption("lo", true, "Longitude");
        stepTwoOptions.addOption("la", true, "Latitude");

        Options stepThreeOptions = new Options();
        stepThreeOptions.addOption("al1", true, "Address line 1");
        stepThreeOptions.addOption("al2", true, "Address line 2");
        stepThreeOptions.addOption("pc", true, "Postal code");
        stepThreeOptions.addOption("ci", true, "City");
        stepThreeOptions.addOption("st", true, "State");
        stepThreeOptions.addOption("co", true, "Country");

        Options stepFourOptions = new Options();
        stepFourOptions.addOption(Option.builder("a")
                .hasArgs()
                .desc("List of amenities")
                .build());


        try {
            Listing currListing = listingService.getListingById(listingId);

            // Step 1: Set listing title, base price, property type
            System.out.println("Step 1: Update listing title (t), base price (p), and property type (type)");
            CommandLine cmd = parser.parse(stepOneOptions, scanner.nextLine().split(" "));
            String title = cmd.getOptionValue("t");
            int basePrice = Integer.parseInt(cmd.getOptionValue("p"));
            String type = cmd.getOptionValue("type");

            // Step 2: Geo coordinates: longitude, latitude
            System.out.println("Step 2: Update geo coordinates (longitude (lo)), latitude (la))");
            cmd = parser.parse(stepTwoOptions, scanner.nextLine().split(" "));
            double longitude = Double.parseDouble(cmd.getOptionValue("lo"));
            double latitude = Double.parseDouble(cmd.getOptionValue("la"));

            // Step 3: Address: line1, line2, postal_code, city, state, country
            System.out.println("Step 3: Update address line 1 (al1), line 2(al2), postal_code (pc), City (ci), " +
                    "State (st), Country (c)");
            cmd = parser.parse(stepThreeOptions, scanner.nextLine().split(" "));
            String addressLine1 = cmd.getOptionValue("al1");
            String addressLine2 = cmd.getOptionValue("al2");
            String postalCode = cmd.getOptionValue("pc");
            String city = cmd.getOptionValue("ci");
            String state = cmd.getOptionValue("st");
            String country = cmd.getOptionValue("co");

            System.out.println("Address line 1: " + addressLine1 + " , " + "Address line 2: " + addressLine2);
            System.out.println("Postal code: " + postalCode + " , " + "City: " + city);
            System.out.println("State: " + state + " , " + "Country: " + country);
            System.out.println();

            // Step 4: List any amenities
            System.out.println("Step 4: Update any amenities (comma-separated)");
            cmd = parser.parse(stepFourOptions, scanner.nextLine().split(","));
            String[] amenities = cmd.getOptionValues("a");

            System.out.println("Amenities: " + String.join(", ", amenities));
            System.out.println();

            if (isNotNullOrEmpty(title)) {
                currListing.setTitle(title);
            }
            if (basePrice != 0){
                currListing.setBasePrice(basePrice);
            }
            if (isNotNullOrEmpty(type)){
                try {
                    PropertyType propertyType = PropertyType.valueOf(type);
                    currListing.setPropertyType(propertyType);
                } catch (IllegalArgumentException e) {
                }
            }
            if (longitude != 0) {
                currListing.setLongitude(longitude);
            }
            if (latitude != 0) {
                currListing.setLatitude(latitude);
            }
            if (isNotNullOrEmpty(addressLine1)){
                currListing.setAddressLineOne(addressLine1);
            }
            if (isNotNullOrEmpty(addressLine2)){
                currListing.setAddressLineTwo(addressLine2);
            }
            if (isNotNullOrEmpty(city)) {
                currListing.setCity(city);
            }
            if (isNotNullOrEmpty(state)) {
                currListing.setState(state);
            }
            if (isNotNullOrEmpty(country)) {
                currListing.setCountry(country);
            }
            if (isNotNullOrEmpty(postalCode)){
                currListing.setPostalCode(postalCode);
            }

            if (listingService.updateListing(currListing)) {
                System.out.println("Updated listing");

                String[][] data = {
                        currListing.getRowValues()
                };
                TableOutput.outputTable(currListing.getHeaders(), data);
                System.out.println();
            } else {
                System.out.println("Something went wrong");
                System.out.println();
            }

        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
//            HelpFormatter formatter = new HelpFormatter();
//            formatter.printHelp("ListingCreator", );
        } catch (Exception e) {
            System.err.println("Oops, there was an error: " + e.getMessage());
        }
    }

    private static void getBookings (Scanner scanner) {
        getListings();

        System.out.print("Input listing id to view bookings for that listing: ");
        int listingid = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        BookingService bookingService = new BookingService(currConnection);

        ArrayList<Booking> bookings = bookingService.getAllBookingsByListingId(listingid);
        ArrayList<TableFormat> tableFormatBookings = new ArrayList<>(bookings);

        if (bookings.isEmpty()) {
            System.out.println("You have no bookings.");
            System.out.println();
        } else {
            TableOutput.formatTable(tableFormatBookings);
            System.out.println();
        }
    }

    private static void cancelBooking(Scanner scanner) {

        BookingService bookingService = new BookingService(currConnection);

        System.out.print("Input booking id of booking you want to cancel: ");
        int bookingid = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        bookingService.cancelBooking(bookingid, "host");

    }

    private static void leaveReview(Scanner scanner) {

        System.out.println("Creating a review:");

        System.out.print("Which booking is this for (id)?: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine();

        BookingService bookingService = new BookingService(currConnection);
        ReviewService reviewService = new ReviewService(currConnection);
        Booking booking = bookingService.getBookingById(bookingId);

        if(booking == null) {
            System.out.println("This booking does not exist");
            System.out.println();
            return;
        }

        System.out.print("Enter your comment: ");
        String comment = scanner.nextLine();

        System.out.print("Enter your rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Validate rating
        while (rating < 1 || rating > 5) {
            System.out.println("Invalid rating. Please enter a rating between 1 and 5.");
            rating = scanner.nextInt();
            scanner.nextLine();
        }

        Review review = new Review(bookingId, currHost.getId(), booking.getRenterId(), comment, rating);
        reviewService.createReviewOfRenter(review);

    }

    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

}
