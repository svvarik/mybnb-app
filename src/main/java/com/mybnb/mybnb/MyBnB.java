package com.mybnb.mybnb;

import com.mybnb.mybnb.interfaces.HostInterface;
import com.mybnb.mybnb.interfaces.HostLoginInterface;
import com.mybnb.mybnb.interfaces.RenterLoginInterface;
import com.mybnb.mybnb.renter.RenterLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
import java.util.Scanner;

public class MyBnB {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mybnb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mybnb123";

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MyBnB.class.getResource("welcome/welcome.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("MyBnB");
//        stage.setScene(scene);
//        stage.show();
//    }

    public static void main(String[] args) {

        Connection connection = null;


        try {
            // Step 1: Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish the connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database!");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Welcome to the Airbnb Clone!");
                System.out.println("Are you a Host or a Renter?");
                System.out.println("1. Host");
                System.out.println("2. Renter");
                System.out.println("3. Admin");
                System.out.println("3. Exit");
                System.out.print("Please choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                switch (choice) {
                    // Host
                    case 1:
                        HostLoginInterface.run(connection, scanner);
                        break;
                    // Renter
                    case 2:
                        RenterLoginInterface.run(connection, scanner);
                        break;
                    // Admin
                    case 3:
                        // Implement logic for leaving a review
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please choose again.");
                }
            }


        } catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error");
            e.printStackTrace();
        } finally {
            // Step 4: Close the connection in a finally block
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed");
                } catch (SQLException e) {
                    System.err.println("Error closing the connection");
                    e.printStackTrace();
                }
            }
        }

    }
}