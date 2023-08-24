package com.mybnb.mybnb.classes.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class HostService {
    private Connection connection;

    public HostService(Connection connection) {
        this.connection = connection;
    }

    // Create Renter
    public void insertHost(Host user) {
        String insertSQL = """
                INSERT INTO host (sin, email, password, first_name, last_name, date_of_birth, address_line_one, 
                address_line_two, city, state, country, postal_code, occupation) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, user.getSin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getDateOfBirth().toString());
            preparedStatement.setString(7, user.getAddressLineOne());
            preparedStatement.setString(8, user.getAddressLineTwo());
            preparedStatement.setString(9, user.getCity());
            preparedStatement.setString(10, user.getState());
            preparedStatement.setString(11, user.getCountry());
            preparedStatement.setString(12, user.getPostalCode());
            preparedStatement.setString(13, user.getOccupation());

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read host
    public User getHostByEmail(String email) {
        String selectSQL = "SELECT * FROM host WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractHostFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateHost(Host updatedHost) {
        String updateSQL = "UPDATE host SET sin = ?, email = ?, password = ?, first_name = ?, last_name = ?, date_of_birth = ?, address_line_one = ?, address_line_two = ?, city = ?, state = ?, country = ?, postal_code = ?, occupation = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, updatedHost.getSin());
            preparedStatement.setString(2, updatedHost.getEmail());
            preparedStatement.setString(3, updatedHost.getPassword());
            preparedStatement.setString(4, updatedHost.getFirstName());
            preparedStatement.setString(5, updatedHost.getLastName());
            preparedStatement.setDate(6, new java.sql.Date(updatedHost.getDateOfBirth().getTime()));
            preparedStatement.setString(7, updatedHost.getAddressLineOne());
            preparedStatement.setString(8, updatedHost.getAddressLineTwo());
            preparedStatement.setString(9, updatedHost.getCity());
            preparedStatement.setString(10, updatedHost.getState());
            preparedStatement.setString(11, updatedHost.getCountry());
            preparedStatement.setString(12, updatedHost.getPostalCode());
            preparedStatement.setString(13, updatedHost.getOccupation());
            preparedStatement.setInt(14, updatedHost.getId()); // Set username for WHERE clause

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHost(Host user) {
        String deleteSQL = "DELETE FROM host WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, user.getId());

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0; // Return true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Renter extractHostFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        int sin = resultSet.getInt("sin");
        String firstname = resultSet.getString("first_name");
        String lastname = resultSet.getString("last_name");
        Date dateOfBirth = resultSet.getDate("date_of_birth");
        String addressLineOne = resultSet.getString("address_line_one");
        String addressLineTwo = resultSet.getString("address_line_two");
        String city = resultSet.getString("city");
        String state = resultSet.getString("state");
        String country = resultSet.getString("country");
        String postalCode = resultSet.getString("postal_code");
        String occupation = resultSet.getString("occupation");

        // Create and return a Renter object based on the retrieved data
        return new Renter(id, email, password,
                sin, firstname, lastname, dateOfBirth, addressLineOne, addressLineTwo, city, state, country, postalCode, occupation);
    }
}



