package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.sql.SQLException;

import static dao.JDBC.connection;
/**
 * The loginPageController class handles the actions and events of the login page in the application.
 * It provides methods to handle action events of the login button, log user activity, and initialize the login page.
 * The class interacts with the database to authenticate the user's credentials.
 * It uses the JDBC connection to fetch user data based on the entered username and password.
 * The class also handles localization by loading the appropriate resource bundle based on the system's default locale.
 * It translates the text on the login page to the appropriate language (English or French) based on the locale.
 */
public class loginPageController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Label areaLbl;

    @FXML
    private Label locationLbl;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField passwordTxt;

    @FXML
    private Label loginLbl;

    @FXML
    private Label usernameLbl;


    @FXML
    private TextField usernameTxt;
    private int userId;
    private static String username;

    public static String getUsername() {
        return username;
    }

    @FXML
    private Label errorLabel; // Define the error label

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private ResourceBundle bundle; // Define bundle as an instance variable

    /**
     * This method initializes the login page when it's loaded.
     * It sets the area label to the system's default time zone.
     * It also checks the system's default locale and loads the appropriate resource bundle for localization.
     * If the default locale is French, it translates the text on the login page to French.
     * If the default locale is English, it loads the English resource bundle.
     * The method also sets the error messages for username not found and incorrect password based on the locale.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        areaLbl.setText(ZoneId.systemDefault().toString());
        // Get the default locale
        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale.getLanguage().equals("fr")) {
            // Load the appropriate resource bundle
            bundle = ResourceBundle.getBundle("com.example.swii2.Nat_fr", defaultLocale);
            // Get the translated text from the resource bundle
            String greeting = bundle.getString("login");
            String loc = bundle.getString("location");
            String username2 = bundle.getString("username");
            String password = bundle.getString("password");
            String errorUsernameNotFound = bundle.getString("errorUsernameNotFound");
            String errorIncorrectPassword = bundle.getString("errorIncorrectPassword");
            loginLbl.setText(greeting);
            locationLbl.setText(loc);
            usernameLbl.setText(username2);
            passwordTxt.setText(password);
            loginLbl.setText(greeting);
        }
        if (defaultLocale.getLanguage().equals("en")) {
            // Load the appropriate resource bundle
            bundle = ResourceBundle.getBundle("com.example.swii2.Nat_en", defaultLocale);

            // Get the translated text from the resource bundle
            String errorUsernameNotFound = bundle.getString("errorUsernameNotFound");
            String errorIncorrectPassword = bundle.getString("errorIncorrectPassword");
        }
    }
    /**
     * This method logs the user's login activity.
     * It writes the username, login status (successful or failed), and timestamp of the login attempt to a file named "login_activity.txt".
     * If the login attempt is successful, the status is set to "Successful". If the login attempt fails, the status is set to "Failed".
     *
     * @param username The username of the user attempting to log in.
     * @param isSuccess A boolean indicating whether the login attempt was successful.
     */
    public static void logUserActivity(String username, boolean isSuccess) {
        String status = isSuccess ? "Successful" : "Failed";
        String timestamp = LocalDateTime.now().toString();
        String logMessage = String.format("User: %s, Login Attempt: %s, Timestamp: %s%n", username, status, timestamp);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_activity.txt", true))) {
            writer.write(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles the login action when the login button is clicked.
     * It retrieves the username and password entered by the user, and attempts to authenticate the user by comparing these credentials with those in the database.
     * If the authentication is successful, it loads the main menu scene and passes the user's ID to it.
     * If the authentication fails because the username is not found or the password is incorrect, it displays an appropriate error message.
     * Regardless of whether the login attempt is successful or not, it logs the user's login activity.
     *
     * @param event The ActionEvent object representing the login button click event.
     * @throws IOException if there is an error loading the main menu scene.
     */
    @FXML
    void OnActionLogin(ActionEvent event) throws IOException {
        username = usernameTxt.getText();
        String password = passwordTxt.getText();
        boolean isSuccess = false; // Variable to track if login is successful
        try {
            // Execute a SQL SELECT query to retrieve the User_ID, User_Name, and Password
            String sql = "SELECT User_ID, User_Name, Password FROM users WHERE User_Name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Check if the entered password matches the password in the database
                if (rs.getString("Password").equals(password)) {
                    // Store the User_ID in a variable
                    int userId = rs.getInt("User_ID");
                    System.out.println("User ID: " + userId);

                    // Load the mainMenu scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainMenu.fxml"));
                    Parent root = loader.load();

                    // Get the controller and pass the User_ID to it
                    mainMenuController controller = loader.getController();
                    controller.initData(userId);

                    // Set the scene on the stage
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    isSuccess = true; // Login was successful
                } else {
                    errorLabel.setText(bundle.getString("errorIncorrectPassword")); // Set error message
                }
            } else {
                errorLabel.setText(bundle.getString("errorUsernameNotFound")); // Set error message
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            logUserActivity(username, isSuccess); // Log the user activity
        }
    }
}