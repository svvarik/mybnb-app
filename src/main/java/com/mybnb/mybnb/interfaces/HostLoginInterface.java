package com.mybnb.mybnb.interfaces;

import com.mybnb.mybnb.classes.user.Host;
import com.mybnb.mybnb.classes.user.Renter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class HostLoginInterface {

    public static void run(Connection connection, Scanner scanner) {
        System.out.println("Host Login");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        Host currHost = validateHostLogin(connection, email, password);
        if (currHost != null) {
            HostInterface.run(connection, scanner, currHost);
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static Host validateHostLogin(Connection connection, String email, String password) {
        String query = "SELECT * FROM host WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Integer sin = resultSet.getInt("sin");
                    Date dateOfBirth = resultSet.getDate("date_of_birth");
                    String addressLineOne = resultSet.getString("address_line_one");
                    String addressLineTwo = resultSet.getString("address_line_two");
                    String city = resultSet.getString("city");
                    String state = resultSet.getString("state");
                    String country = resultSet.getString("country");
                    String postalCode = resultSet.getString("postal_code");
                    String occupation = resultSet.getString("occupation");

                    return new Host(id, email, password, sin, firstName, lastName, dateOfBirth, addressLineOne,
                            addressLineTwo, city, state, country, postalCode, occupation);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
