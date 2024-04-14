module com.example.swii2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.swii2 to javafx.fxml;
    opens model to javafx.base;
    exports com.example.swii2;
    exports controller;
    opens controller to javafx.fxml;
}