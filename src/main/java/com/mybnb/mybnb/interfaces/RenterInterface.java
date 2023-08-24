package com.mybnb.mybnb.interfaces;

import com.mybnb.mybnb.classes.booking.Booking;
import com.mybnb.mybnb.classes.booking.BookingService;
import com.mybnb.mybnb.classes.listing.Listing;
import com.mybnb.mybnb.classes.listing.ListingService;
import com.mybnb.mybnb.classes.review.Review;
import com.mybnb.mybnb.classes.review.ReviewService;
import com.mybnb.mybnb.classes.user.Host;
import com.mybnb.mybnb.classes.user.Renter;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class RenterInterface {

    private static Connection currConnection;
    private static Renter currRenter;

    public static void run(Connection connection, Scanner scanner, Renter renter) {

        currConnection = connection;
        currRenter = renter;

        while (true) {
            System.out.println("Renter Menu:");
            System.out.println("1. Search Listings");
            System.out.println("2. Get Bookings");
            System.out.println("3. Create Booking");
            System.out.println("4. Leave Review for Listing");
            System.out.println("5. Leave Review for Host");
            System.out.println("6. Go Back");
            System.out.print("Please choose an option: ");

            int renterChoice = scanner.nextInt();
            scanner.nextLine();

            switch (renterChoice) {
                case 1:
                    SearchInterface.run(connection, scanner, renter);
                    break;
                case 2:
                    getBookings();
                    break;
                case 3:
                    createBooking(scanner);
                    break;
                case 4:
                    leaveReviewAboutListing(scanner);
                    break;
                case 5:
                    leaveReviewAboutHost(scanner);
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    private static void getBookings() {
        BookingService bookingService = new BookingService(currConnection);
        ArrayList<Booking> bookings = bookingService.getAllBookingsForRenter(currRenter.getId());
        ArrayList<TableFormat> tableFormatBookings = new ArrayList<>(bookings);

        if (bookings.isEmpty()) {
            System.out.println("You have no bookings.");
            System.out.println();
        } else {
            TableOutput.formatTable(tableFormatBookings);
            System.out.println();
        }
    }

    private static void createBooking(Scanner scanner) {
        BookingService bookingService = new BookingService(currConnection);

        System.out.println("Creating a Booking:");

        System.out.print("Enter Listing ID: ");
        int listingId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.println("Enter Start Date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        LocalDateTime startDate = LocalDateTime.parse(startDateStr);

        System.out.println("Enter End Date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);

        Booking booking = new Booking(currRenter.getId(), listingId, "inprogress", null,
                LocalDateTime.now(), startDate, endDate, LocalDateTime.now());

        if (bookingService.createBooking(booking)) {
            System.out.println("Created listing");

            String[][] data = {
                    booking.getRowValues()
            };
            TableOutput.outputTable(booking.getHeaders(), data);
        } else {
            System.out.println("Something went wrong");
        }


        System.out.println("Booking created:");

    }

    private static void leaveReviewAboutListing(Scanner scanner) {

        System.out.println("Creating a review about a listing:");

        System.out.print("Which booking is this for (id)?: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine();

        BookingService bookingService = new BookingService(currConnection);
        ReviewService reviewService = new ReviewService(currConnection);
        Booking booking = bookingService.getBookingById(bookingId);

        if (booking == null) {
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

        Review review = new Review(bookingId, currRenter.getId(), booking.getListingId(), comment, rating);
        reviewService.createReviewOfRenter(review);

        System.out.println("Review Created");
        System.out.println();
    }

    private static void leaveReviewAboutHost(Scanner scanner) {

        System.out.println("Creating a review about a host:");

        System.out.print("Which booking is this for (id)?: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine();

        BookingService bookingService = new BookingService(currConnection);
        ReviewService reviewService = new ReviewService(currConnection);
        ListingService listingService = new ListingService(currConnection);
        Booking booking = bookingService.getBookingById(bookingId);

        if (booking == null) {
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

        Listing currListing = listingService.getListingById(booking.getListingId());
        Review review = new Review(bookingId, currRenter.getId(), currListing.getHostId(), comment, rating);
        reviewService.createReviewOfRenter(review);

        System.out.println("Review Created");
        System.out.println();
    }
}
