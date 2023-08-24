module com.mybnb.mybnb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires commons.cli;

    opens com.mybnb.mybnb to javafx.fxml;
    exports com.mybnb.mybnb;
    exports com.mybnb.mybnb.renter;
    opens com.mybnb.mybnb.renter to javafx.fxml;
}