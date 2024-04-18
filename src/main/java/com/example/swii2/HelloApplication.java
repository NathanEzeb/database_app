package com.example.swii2;

import dao.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 * The HelloApplication class is the main entry point of the application.
 * It extends the Application class from JavaFX to create a GUI application.
 * The start method is overridden to load the login page view from an FXML file and display it in a new stage.
 * The main method opens a JDBC connection, launches the application, sets the default locale based on the system's default language, and then closes the JDBC connection.
 * The class also handles localization by loading the appropriate resource bundle based on the system's default locale.
 * It translates the text on the login page to the appropriate language (English or French) based on the locale.
 */
public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

        launch();

        Locale france = new Locale("fr", "FR");
        Locale US = new Locale("en", "US");



        ResourceBundle rb = ResourceBundle.getBundle("com.example.swii2.Nat", Locale.getDefault());

        if (java.util.Locale.getDefault().getLanguage().equals("fr") || java.util.Locale.getDefault().getLanguage().equals("en")) {
            System.out.println(rb.getString("login"));
           Locale.setDefault(france);

        }

        JDBC.closeConnection();
    }
}