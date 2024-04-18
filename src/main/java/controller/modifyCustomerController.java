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
import model.customer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static dao.JDBC.connection;

/**
 * The modifyCustomerController class handles the actions and events of the modify customer view in the application.
 * It provides methods to handle action events of various buttons such as save and cancel.
 * It also provides methods to update the division ComboBox based on the selected country, retrieve the ID of a country and division from the database using their names, and update an existing customer in the database.
 * The class interacts with the database to retrieve and manipulate customer data.
 * It uses the JDBC connection to fetch and update customer data based on different criteria such as country name and division name.
 * The class also handles the initialization of the modify customer view by setting up the ComboBox for country and division, and populating the fields with the data from the selected customer if one exists.
 */
public class modifyCustomerController implements Initializable {

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

    private int getCountry(int countryId) throws SQLException {
        String sql = "SELECT Country FROM COUNTRIES WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("Country");
        } else {
            throw new SQLException("Country not found: " + countryId);
        }
    }

    private String getDivisionName(int divisionId) throws SQLException {
        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("Division");
        } else {
            throw new SQLException("Division not found: " + divisionId);
        }
    }

    /**
     * This method retrieves the name of a division from the database using the division's ID.
     * It prepares a SQL SELECT statement with the division's ID as a parameter and executes the statement.
     * If the ResultSet returned by the query is not empty, it means that the division was found and its name is returned.
     *
     * @param divisionId The ID of the division to retrieve the name for.
     * @return The name of the division if found.
     * @throws SQLException if a database access error occurs or the division is not found.
     */
    private String getDivisionNameFromId(int divisionId) throws SQLException {
            String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("Division");
        } else {
            throw new SQLException("Division not found: " + divisionId);
        }
    }
    /**
     * This method retrieves the ID of a country from the database using the division's ID.
     * It prepares a SQL SELECT statement with the division's ID as a parameter and executes the statement.
     * If the ResultSet returned by the query is not empty, it means that the country was found and its ID is returned.
     *
     * @param divisionId The ID of the division to retrieve the country ID for.
     * @return The ID of the country if found.
     * @throws SQLException if a database access error occurs or the country is not found.
     */
    private int getCountryIdFromDivisionId(int divisionId) throws SQLException {
        String sql = "SELECT Country_ID FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("Country_ID");
        } else {
            throw new SQLException("Country not found: " + divisionId);
        }
    }
    /**
     * This method retrieves the name of a country from the database using the division's ID.
     * It first calls the `getCountryIdFromDivisionId` method to get the country ID associated with the division ID.
     * Then it calls the `getCountryFromCountryId` method to get the country name associated with the country ID.
     *
     * @param divisionId The ID of the division to retrieve the country name for.
     * @return The name of the country if found.
     * @throws SQLException if a database access error occurs or the country is not found.
     */
    public String getCountryFromDivisionId(int divisionId) throws SQLException {
        int countryId = getCountryIdFromDivisionId(divisionId);
        return getCountryFromCountryId(countryId);
    }

    /**
     * This method retrieves the name of a country from the database using the country's ID.
     * It prepares a SQL SELECT statement with the country's ID as a parameter and executes the statement.
     * If the ResultSet returned by the query is not empty, it means that the country was found and its name is returned.
     *
     * @param countryId The ID of the country to retrieve the name for.
     * @return The name of the country if found.
     * @throws SQLException if a database access error occurs or the country is not found.
     */
    private String getCountryFromCountryId(int countryId) throws SQLException {
        String sql = "SELECT Country FROM COUNTRIES WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("Country");
        } else {
            throw new SQLException("Country not found for Country ID: " + countryId);
        }
    }

    /**
     * This method initializes the modifyCustomerController.
     * It populates the country ComboBox with the available countries and sets up a listener to update the division ComboBox based on the selected country.
     * If a customer has been selected for modification, it populates the fields with the data from the selected customer and sets the selected country in the country ComboBox.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     * @throws SQLException if a database access error occurs or the country or division is not found.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the state ComboBox
        countryCbx.getItems().addAll("U.S", "UK", "Canada");
        countryCbx.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // Update the division ComboBox based on the selected state
            updateDivisionComboBox(newValue);
        });


        if (selectedCustomer != null) {
            // Populate the fields with the data from the selected appointment
            nameTxt.setText(selectedCustomer.getCustomerName());
            phoneTxt.setText(selectedCustomer.getPhone());
            PostalTxt.setText(selectedCustomer.getPostalCode());
            addressTxt.setText(selectedCustomer.getAddress());
            countryCbx.getItems().addAll("U.S", "UK", "Canada");
            String division = null;
            try {
                division = getDivisionNameFromId(selectedCustomer.getDivisionId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            divisionCbx.getSelectionModel().select(division);

            int divisionId = selectedCustomer.getDivisionId();

            // Get the Country from the Division_ID
            String country;
            try {
                country = getCountryFromDivisionId(divisionId);
            } catch (SQLException e) {
                e.printStackTrace();
                country = "";
            }

            // Set the selected country in the countryCbx combo box
            countryCbx.getSelectionModel().select(country);
        }
    }
    /**
     * This method updates the division ComboBox based on the selected country.
     * It first clears the division ComboBox, then populates it with divisions corresponding to the selected country.
     *
     * @param state The selected country for which the divisions are to be retrieved.
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

    /**
     * This method retrieves the ID of a country from the database using the country's name.
     * It prepares a SQL SELECT statement with the country's name as a parameter and executes the statement.
     * If the ResultSet returned by the query is not empty, it means that the country was found and its ID is returned.
     *
     * @param countryName The name of the country to retrieve the ID for.
     * @return The ID of the country if found.
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

    private String getDivision(int divisionId) throws SQLException {
        String sql = "SELECT Division FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("Division");
        } else {
            throw new SQLException("Division not found: " + divisionId);
        }
    }
    private static customer selectedCustomer;
    public static void setSelectedCustomer(customer customer) {
        selectedCustomer = customer;
    }

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/report.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionSave(ActionEvent event) throws SQLException {
        this.updateCustomer();
    }

    /**
     * This method updates the details of a customer in the database.
     * It retrieves the selected country and division from the ComboBoxes and converts them to their corresponding IDs.
     * Then it prepares a SQL UPDATE statement with the new customer details and executes the statement.
     *
     * @return The number of rows affected by the update operation.
     * @throws SQLException if a database access error occurs or the country or division is not found.
     */
    public int updateCustomer() throws SQLException {
        String selectedCountry = countryCbx.getSelectionModel().getSelectedItem();
        String selectedDivision = divisionCbx.getSelectionModel().getSelectedItem();

        // Convert the selected country and division to their corresponding IDs
        int divisionId = getDivisionId(selectedDivision);

        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, nameTxt.getText());
        ps.setString(2, addressTxt.getText());
        ps.setString(3, PostalTxt.getText());
        ps.setString(4, phoneTxt.getText());
        ps.setInt(5, divisionId);
        ps.setInt(6, selectedCustomer.getCustomerId()); // Assuming selectedCustomer is the customer to be updated
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * This method retrieves the ID of a division from the database using the division's name.
     * It prepares a SQL SELECT statement with the division's name as a parameter and executes the statement.
     * If the ResultSet returned by the query is not empty, it means that the division was found and its ID is returned.
     *
     * @param divisionName The name of the division to retrieve the ID for.
     * @return The ID of the division if found.
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


    public int insertCustomer() throws SQLException{
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