package com.mybnb.mybnb.classes.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewService {
    private Connection connection; // Your database connection

    // Constructor to initialize the connection
    public ReviewService(Connection connection) {
        this.connection = connection;
    }

    // Create a new review of renter
    public void createReviewOfRenter(Review review) {
        // Check if the reviewer (host) has a completed booking with the renter
        String checkBookingSQL = "SELECT id FROM booking WHERE listing_id IN " +
                "(SELECT id FROM listing WHERE host_id = ?) AND renter_id = ? AND status = 'completed'";

        try (PreparedStatement checkBookingStatement = connection.prepareStatement(checkBookingSQL)) {
            checkBookingStatement.setInt(1, review.getReviewerId()); // Host ID
            checkBookingStatement.setInt(2, review.getReviewedId()); // Renter ID

            try (ResultSet resultSet = checkBookingStatement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Host doesn't have a completed booking with this renter.");
                    return; // Exit the function if no completed booking found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return; // Exit the function on error
        }

        // If a completed booking exists, proceed to insert the review
        String insertSQL = """
                INSERT INTO review_of_renter (booking_id, reviewer_id, renter_id, created_at, comment, rating)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, review.getBookingId());
            preparedStatement.setInt(2, review.getReviewerId());
            preparedStatement.setInt(3, review.getReviewedId());
            preparedStatement.setObject(4, review.getCreatedAt()); // Use setObject to handle LocalDateTime
            preparedStatement.setString(5, review.getComment());
            preparedStatement.setInt(6, review.getRating());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createReviewOfHost(Review review) {
        // Check if the reviewer (renter) has a completed booking with the host
        String checkBookingSQL = "SELECT id FROM booking WHERE renter_id = ? AND listing_id IN " +
                "(SELECT id FROM listing WHERE host_id = ?) AND status = 'completed'";

        try (PreparedStatement checkBookingStatement = connection.prepareStatement(checkBookingSQL)) {
            checkBookingStatement.setInt(1, review.getReviewerId()); // Renter ID
            checkBookingStatement.setInt(2, review.getReviewedId()); // Host ID

            try (ResultSet resultSet = checkBookingStatement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Renter doesn't have a completed booking with this host.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String insertSQL = """
                INSERT INTO review_of_host (booking_id, reviewer_id, host_id, created_at, comment, rating)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, review.getBookingId());
            preparedStatement.setInt(2, review.getReviewerId());
            preparedStatement.setInt(3, review.getReviewedId());
            preparedStatement.setObject(4, review.getCreatedAt()); // Use setObject to handle LocalDateTime
            preparedStatement.setString(5, review.getComment());
            preparedStatement.setInt(6, review.getRating());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create a new review of listing
    public void createReviewOfListing(Review review) {
        // Check if the renter has a completed booking for the listing
        String checkBookingSQL = "SELECT id FROM booking WHERE renter_id = ? AND listing_id = ? AND status = 'completed'";

        try (PreparedStatement checkBookingStatement = connection.prepareStatement(checkBookingSQL)) {
            checkBookingStatement.setInt(1, review.getReviewerId()); // Renter ID
            checkBookingStatement.setInt(2, review.getReviewedId()); // Listing ID

            try (ResultSet resultSet = checkBookingStatement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Renter doesn't have a completed booking for this listing.");
                    return; // Exit the function if no completed booking found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return; // Exit the function on error
        }

        // If a completed booking exists, proceed to insert the review
        String insertSQL = """
                INSERT INTO review_of_listing (booking_id, reviewer_id, listing_id, created_at, comment, rating)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, review.getBookingId());
            preparedStatement.setInt(2, review.getReviewerId());
            preparedStatement.setInt(3, review.getReviewedId());
            preparedStatement.setObject(4, review.getCreatedAt()); // Use setObject to handle LocalDateTime
            preparedStatement.setString(5, review.getComment());
            preparedStatement.setInt(6, review.getRating());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Retrieve all reviews for a specific host by host ID
    public List<Review> getReviewsForHost(int hostId) {
        List<Review> reviews = new ArrayList<>();
        String selectSQL = "SELECT * FROM review_of_host WHERE host_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, hostId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Review review = extractReviewFromResultSet(resultSet);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    // Retrieve all reviews for a specific renter by renter ID
    public List<Review> getReviewsForRenter(int renterId) {
        List<Review> reviews = new ArrayList<>();
        String selectSQL = "SELECT * FROM review_of_renter WHERE renter_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, renterId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Review review = extractReviewFromResultSet(resultSet);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    // Retrieve all reviews for a specific listing by listing ID
    public List<Review> getReviewsForListing(int listingId) {
        List<Review> reviews = new ArrayList<>();
        String selectSQL = "SELECT * FROM review_of_listing WHERE listing_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, listingId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Review review = extractReviewFromResultSet(resultSet);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    // Extract review data from a ResultSet
    private Review extractReviewFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int bookingId = resultSet.getInt("booking_id");
        int reviewerId = resultSet.getInt("reviewer_id");
        int reviewedId = resultSet.getInt("host_id"); // For host reviews, adjust based on table
        Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
        String comment = resultSet.getString("comment");
        int rating = resultSet.getInt("rating");

        return new Review(id, bookingId, reviewerId, reviewedId, comment, rating, createdAt);
    }

    // Other methods and helper functions
}
