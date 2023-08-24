package com.mybnb.mybnb.classes.booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingService {
    private Connection connection;

    public BookingService(Connection connection) {
        this.connection = connection;
    }

    // Create a new booking
    public boolean createBooking(Booking booking) {
        String insertSQL = """
                INSERT INTO booking (renter_id, listing_id, status, created_at, start_date, end_date) 
                VALUES (?, ?, ?, ?, ?, ?)""";
        try {
            // Check if the booking dates are within available dates
            if (areDatesAvailable(booking.getListingId(), booking.getStartDate(), booking.getEndDate())) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                    preparedStatement.setInt(1, booking.getRenterId());
                    preparedStatement.setInt(2, booking.getListingId());
                    preparedStatement.setString(3, booking.getStatus());
                    preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                    preparedStatement.setDate(5, new java.sql.Date(booking.getStartDate().getTime()));
                    preparedStatement.setDate(6, new java.sql.Date(booking.getEndDate().getTime()));
                    preparedStatement.executeUpdate();

                    int rowsInserted = preparedStatement.executeUpdate();
                    return rowsInserted > 0;
                }
            } else {
                System.out.println("Booking dates are not available.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve a booking by ID
    public Booking getBookingById(int id) {
        String selectSQL = "SELECT * FROM booking WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractBookingFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update a booking
    public void updateBooking(Booking booking) {
        String updateSQL = "UPDATE booking SET renter_id = ?, listing_id = ?, status = ?, start_date = ?, end_date = ? " +
                "WHERE id = ?";

        try {
            // Check if the booking dates are within available dates
            if (areDatesAvailable(booking.getListingId(), booking.getStartDate(), booking.getEndDate())) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                    preparedStatement.setInt(1, booking.getRenterId());
                    preparedStatement.setInt(2, booking.getListingId());
                    preparedStatement.setString(3, booking.getStatus());
                    preparedStatement.setDate(4, new java.sql.Date(booking.getStartDate().getTime()));
                    preparedStatement.setDate(5, new java.sql.Date(booking.getEndDate().getTime()));
                    preparedStatement.setInt(6, booking.getId());
                    preparedStatement.executeUpdate();
                }
            } else {
                System.out.println("Booking dates are not available.");
                // Handle the case where booking dates are not available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelBooking(int bookingId, String cancelledBy) {
        String updateBookingSQL = "UPDATE booking " +
                "SET status = 'cancelled', cancelled_by = ? " +
                "WHERE id = ? AND status = 'inprogress'";

        String updateAvailabilitySQL = "UPDATE available_dates " +
                "SET available = 1 " +
                "WHERE listing_id = ? AND date BETWEEN ? AND ?";

        try {
            connection.setAutoCommit(false); // Start a transaction

            Booking currBooking = getBookingById(bookingId);

            // Update booking status and cancelled_by
            try (PreparedStatement bookingPreparedStatement = connection.prepareStatement(updateBookingSQL)) {
                bookingPreparedStatement.setString(1, cancelledBy);
                bookingPreparedStatement.setInt(2, bookingId);

                int rowsAffected = bookingPreparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Booking could not be cancelled.");
                }
            }

            // Update availability status in available_dates table
            LocalDate startDate = currBooking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = currBooking.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Integer listingId = currBooking.getListingId();

            try (PreparedStatement availabilityPreparedStatement = connection.prepareStatement(updateAvailabilitySQL)) {
                availabilityPreparedStatement.setInt(1, listingId);
                availabilityPreparedStatement.setDate(2, java.sql.Date.valueOf(startDate));
                availabilityPreparedStatement.setDate(3, java.sql.Date.valueOf(endDate));

                availabilityPreparedStatement.executeUpdate();
            }

            connection.commit(); // Commit the transaction
            System.out.println("Booking has been cancelled successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // Rollback the transaction in case of error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Delete a booking by ID
    public void deleteBooking(int id) {
        String deleteSQL = "DELETE FROM booking WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Booking> getAllBookingsForRenter(int userId) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM booking WHERE renter_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = extractBookingFromResultSet(resultSet);
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public ArrayList<Booking> getAllBookingsByListingId(int listingId) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String selectSQL = "SELECT * FROM booking WHERE listing_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, listingId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = extractBookingFromResultSet(resultSet);
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // Extract a Booking object from a ResultSet
    private Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getInt("id"));
        booking.setRenterId(resultSet.getInt("renter_id"));
        booking.setListingId(resultSet.getInt("listing_id"));
        booking.setStatus(resultSet.getString("status"));
        booking.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        booking.setStartDate(resultSet.getDate("start_date").toLocalDate());
        booking.setEndDate(resultSet.getDate("end_date").toLocalDate());
        booking.setLastUpdated(resultSet.getTimestamp("last_updated").toLocalDateTime());
        return booking;
    }

    private long calculateDaysBetweenDates(Date startDate, Date endDate) {
        long millisecondsPerDay = 24 * 60 * 60 * 1000; // Number of milliseconds in a day

        long timeDifference = endDate.getTime() - startDate.getTime();
        return timeDifference / millisecondsPerDay;
    }

    private boolean areDatesAvailable(int listingId, Date startDate, Date endDate) {
        String selectSQL = "SELECT COUNT(*) FROM available_dates " +
                "WHERE listing_id = ? AND date BETWEEN ? AND ? AND available = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, listingId);
            preparedStatement.setDate(2, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);

                // Calculate the expected number of days between startDate and endDate
                Instant startInstant = startDate.toInstant();
                Instant endInstant = endDate.toInstant();

                LocalDateTime startLocal = startInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime endLocal = endInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                long daysBetween = ChronoUnit.DAYS.between(startLocal.toLocalDate(), endLocal.toLocalDate());

                return count == daysBetween + 1; // Dates are available if the count matches the expected days
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the exception as needed
        }
    }
}
