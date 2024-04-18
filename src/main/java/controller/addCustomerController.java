package controller;

import dao.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static dao.JDBC.connection;

/**
 * The addCustomerController class handles the actions and events of the add customer view in the application.
 * It provides methods to handle action events of various buttons such as save and cancel.
 * It also provides methods to update the division ComboBox based on the selected country, retrieve the ID of a country and division from the database using their names, and insert a new customer into the database.
 * The class interacts with the database to retrieve and manipulate customer data.
 * It uses the JDBC connection to fetch and insert customer data based on different criteria such as country name and division name.
 */
public class addCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField PostalTxt;

    @FXML
    private TextField addressTxt;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> countryCbx;

    @FXML
    private TextField customerIdTxt;

    @FXML
    private ComboBox<String> divisionCbx;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private Button saveBtn;

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/src/view/report.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionSave(ActionEvent event) throws SQLException {
        this.insertCustomer();
    }

    /**
     * This method initializes the addCustomerController.
     * It populates the country ComboBox and sets up a listener for it.
     *
     * The lambda expression is used to set up this listener for efficiency and readability:
     * It listens for changes in the selected item of the countryCbx ComboBox.
     * When a new item is selected, it calls the updateDivisionComboBox method with the new value.
     * This allows for immediate response to user interaction and updates the division ComboBox based on the user's choice.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the state ComboBox
        countryCbx.getItems().addAll("U.S", "UK", "Canada");

        // Add a listener to the state ComboBox
        // This lambda expression listens for changes in the selected item of the countryCbx ComboBox.
        // When a new item is selected, it calls the updateDivisionComboBox method with the new value.
        countryCbx.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // Update the division ComboBox based on the selected state
            updateDivisionComboBox(newValue);
        });
    }

    /**
     * This method updates the items in the division ComboBox based on the selected nation state.
     * It first clears the division ComboBox, then populates it with the divisions corresponding to the selected nation state.
     *
     * @param state The selected country based on which the division ComboBox is to be updated.
     */
    private void updateDivisionComboBox(String state) {
        // Clear the division ComboBox
        divisionCbx.getItems().clear();

        // Populate the division ComboBox based on the selected state
        if (state.equals("U.S")) {
            divisionCbx.getItems().addAll("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
                    "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
                    "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
                    "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                    "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
                    "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin",
                    "Wyoming");
        } else if (state.equals("UK")) {
            divisionCbx.getItems().addAll("England", "Scotland", "Wales", "Northern Ireland");
        } else if (state.equals("Canada")) {
            divisionCbx.getItems().addAll("Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador",
                    "Northwest Territories", "Nova Scotia", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan",
                    "Yukon", "Nunavut", "Northwest Territories");

        }
    }

    @FXML
    private void handleSubmitButtonAction() {
        // Get the selected state and division
        String country = countryCbx.getSelectionModel().getSelectedItem();
        String division = divisionCbx.getSelectionModel().getSelectedItem();

        // Create a new customer in the database with the selected state and division
    }

    /**
     * This method retrieves the ID of a country from the database using the country's name.
     * It does this by preparing a SQL SELECT statement with the country's name as a parameter,
     * executing the statement, and returning the ID from the result set.
     *
     * @param countryName The name of the country whose ID is to be retrieved.
     * @return The ID of the country.
     * @throws SQLException if a database access error occurs or the country is not found.
     */
    private int getCountryId(String countryName) throws SQLException {
        String sql = "SELECT Country_ID FROM COUNTRIES WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, countryName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("Country_ID");
        } else {
            throw new SQLException("Country not found: " + countryName);
        }
    }

    /**
     * This method retrieves the ID of a division from the database using the division's name.
     * It does this by preparing a SQL SELECT statement with the division's name as a parameter,
     * executing the statement, and returning the ID from the result set.
     *
     * @param divisionName The name of the division whose ID is to be retrieved.
     * @return The ID of the division.
     * @throws SQLException if a database access error occurs or the division is not found.
     */
    private int getDivisionId(String divisionName) throws SQLException {
        String sql = "SELECT Division_ID FROM FIRST_LEVEL_DIVISIONS WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, divisionName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("Division_ID");
        } else {
            throw new SQLException("Division not found: " + divisionName);
        }
    }


    /**
     * This method is used to insert a new customer into the database.
     * It first gets the selected country and division from the ComboBoxes,
     * then converts these to their corresponding IDs. It also removes non-numeric
     * characters from the phone number. Finally, it prepares an SQL INSERT statement
     * and executes it.
     *
     * @return the number of rows affected by the SQL statement
     * @throws SQLException if a database access error occurs
     */
        public int insertCustomer() throws SQLException {
            // Get the selected country and division
            String selectedCountry = countryCbx.getSelectionModel().getSelectedItem();
            String selectedDivision = divisionCbx.getSelectionModel().getSelectedItem();

            // Convert the selected country and division to their corresponding IDs
            int countryId = getCountryId(selectedCountry);
            int divisionId = getDivisionId(selectedDivision);

            // Remove non-numeric characters from the phone number
            String phoneNumber = phoneTxt.getText().replaceAll("\\D+", "");

            String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, nameTxt.getText());
            ps.setString(2, addressTxt.getText());
            ps.setInt(3, Integer.parseInt(PostalTxt.getText()));
            ps.setString(4, phoneNumber); // Store phone number as a string
            ps.setInt(5, divisionId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
    }



}