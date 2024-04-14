package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static dao.JDBC.connection;

/**
 * The modifyAppointmentController class handles the actions and events of the modify appointment view in the application.
 * It provides methods to handle action events of various buttons such as save and cancel.
 * It also provides methods to check if an appointment overlaps with any existing appointments, retrieve the ID of a contact from the database using the contact's name, and update an existing appointment in the database.
 * The class interacts with the database to retrieve and manipulate appointment data.
 * It uses the JDBC connection to fetch and update appointment data based on different criteria such as appointment time and contact name.
 * The class also handles the initialization of the modify appointment view by setting up the SpinnerValueFactory for startTimeHourSpn, startTimeMinSpn, endTimeHourSpn and endTimeMinSpn, and populating the fields with the data from the selected appointment if one exists.
 */
public class modifyAppointmentController implements Initializable {

    Stage stage;
    Parent scene;
    private TextField appointmentIdTxt;
    @FXML
    private ComboBox<String> contactNameCbx;
    @FXML
    private Button cancelBtn;

    @FXML
    private TextField contactIdTxt;

    @FXML
    private TextField customerIdTxt;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private DatePicker endDateCal;

    @FXML
    private Spinner<Integer> endTimeHourSpn;

    @FXML
    private Spinner<Integer> endTimeMinSpn;

    @FXML
    private TextField userIdTxt;

    @FXML
    private TextField locationTxt;

    @FXML
    private Button saveBtn;

    @FXML
    private DatePicker startDateCal;

    @FXML
    private Spinner<Integer> startTimeHourSpn;

    @FXML
    private Spinner<Integer> startTimeMinSpn;

    @FXML
    private TextField titleTxt;

    @FXML
    private TextField typeTxt;
    private int userId;

    private static appointment selectedAppointment;
    public static void setSelectedAppointment(appointment appointment) {
        selectedAppointment = appointment;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * This method initializes the modifyAppointmentController.
     * It sets up the SpinnerValueFactory for startTimeHourSpn, startTimeMinSpn, endTimeHourSpn and endTimeMinSpn.
     * It also populates the fields with the data from the selected appointment if one exists.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (selectedAppointment != null) {
            // Initialize the SpinnerValueFactory for startTimeHourSpn, startTimeMinSpn, endTimeHourSpn and endTimeMinSpn
            startTimeHourSpn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
            startTimeMinSpn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
            endTimeHourSpn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
            endTimeMinSpn.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));

            // Convert the start timestamp to LocalDateTime
            LocalDateTime startDateTime = LocalDateTime.parse(selectedAppointment.getStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Get the hour and minute values
            int startHour = startDateTime.getHour();
            int startMinute = startDateTime.getMinute();
            // Set the spinner values
            startTimeHourSpn.getValueFactory().setValue(startHour);
            startTimeMinSpn.getValueFactory().setValue(startMinute);

            // Convert the end timestamp to LocalDateTime
            LocalDateTime endDateTime = LocalDateTime.parse(selectedAppointment.getEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Get the hour and minute values
            int endHour = endDateTime.getHour();
            int endMinute = endDateTime.getMinute();
            // Set the spinner values
            endTimeHourSpn.getValueFactory().setValue(endHour);
            endTimeMinSpn.getValueFactory().setValue(endMinute);

            // Populate the fields with the data from the selected appointment
            titleTxt.setText(selectedAppointment.getTitle());
            descriptionTxt.setText(selectedAppointment.getDescription());
            locationTxt.setText(selectedAppointment.getLocation());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate startDate = LocalDate.parse(selectedAppointment.getStart(), formatter);
            startDateCal.setValue(startDate);
            LocalDate endDate = LocalDate.parse(selectedAppointment.getEnd(), formatter);
            endDateCal.setValue(endDate);
            contactIdTxt.setText(String.valueOf(selectedAppointment.getContactId()));
            customerIdTxt.setText(String.valueOf(selectedAppointment.getCustomerId()));
            userIdTxt.setText(String.valueOf(selectedAppointment.getUserId()));
            typeTxt.setText(selectedAppointment.getType());
        }

    }

    /**
     * This method handles the action when the "Cancel" button is clicked.
     * It retrieves the user ID of the selected appointment and loads the mainMenu scene.
     * It also passes the user ID to the mainMenuController.
     *
     * @param event The ActionEvent object representing the "Cancel" button click event.
     * @throws IOException if there is an error loading the mainMenu scene.
     * @throws SQLException if a database access error occurs.
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException, SQLException {
        int userId = selectedAppointment.getUserId();
        String sql = "SELECT User_ID, User_Name, Password FROM users WHERE User_Name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {}
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
    }

    /**
     * This method handles the action when the "Save" button is clicked.
     * It retrieves the data from the form fields, checks if the appointment time is within business hours and if it overlaps with another appointment.
     * If the checks pass, it converts the start and end times to UTC and inserts the appointment into the database.
     *
     * @param event The ActionEvent object representing the "Save" button click event.
     * @throws SQLException if a database access error occurs.
     */
    @FXML
    void onActionSave(ActionEvent event) throws SQLException {
        this.updateAppointment();
    }

    /**
     * This method checks if the given appointment time overlaps with any existing appointments.
     * It prepares a SQL SELECT statement with the start and end times as parameters and executes the statement.
     * If the ResultSet returned by the query is not empty, it means that there is an overlap.
     *
     * @param startDateTime The start time of the appointment to check.
     * @param endDateTime The end time of the appointment to check.
     * @return true if the appointment time overlaps with an existing appointment, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isAppointmentOverlap(LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE (Start BETWEEN ? AND ?) OR (End BETWEEN ? AND ?) OR (Start <= ? AND End >= ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(startDateTime));
        ps.setTimestamp(2, Timestamp.valueOf(endDateTime));
        ps.setTimestamp(3, Timestamp.valueOf(startDateTime));
        ps.setTimestamp(4, Timestamp.valueOf(endDateTime));
        ps.setTimestamp(5, Timestamp.valueOf(startDateTime));
        ps.setTimestamp(6, Timestamp.valueOf(endDateTime));
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * This method retrieves the ID of a contact from the database using the contact's name.
     * It prepares a SQL SELECT statement with the contact's name as a parameter and executes the statement.
     * If the ResultSet returned by the query is not empty, it means that the contact was found and its ID is returned.
     *
     * @param contactName The name of the contact to retrieve the ID for.
     * @return The ID of the contact if found.
     * @throws SQLException if a database access error occurs or the contact is not found.
     */
    public int getContactId(String contactName) throws SQLException {
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("Contact_ID");
        } else {
            throw new SQLException("Contact not found: " + contactName);
        }
    }
    /**
     * This method updates an appointment in the database.
     * It retrieves the data from the form fields, checks if the appointment time is within business hours and if it overlaps with another appointment.
     * If the checks pass, it converts the start and end times to UTC and updates the appointment in the database.
     *
     * @return The number of rows affected by the update operation.
     * @throws SQLException if a database access error occurs.
     */
    public int updateAppointment() throws SQLException {
        LocalDate startDate = startDateCal.getValue();
        LocalDate endDate = endDateCal.getValue();
        int startHour = startTimeHourSpn.getValue();
        int startMinute = startTimeMinSpn.getValue();
        int endHour = endTimeHourSpn.getValue();
        int endMinute = endTimeMinSpn.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(startHour, startMinute));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(endHour, endMinute));

        // Convert the LocalDateTime to a java.sql.Timestamp for Start and End
        ZonedDateTime startZonedDateTime = ZonedDateTime.of(startDateTime, ZoneId.systemDefault());
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(endDateTime, ZoneId.systemDefault());
        ZonedDateTime startUtc = startZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUtc = endZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        java.sql.Timestamp startTimestamp = java.sql.Timestamp.valueOf(startUtc.toLocalDateTime());
        java.sql.Timestamp endTimestamp = java.sql.Timestamp.valueOf(endUtc.toLocalDateTime());

        // Check if the appointment time is within business hours
        if (startHour < 7 || endHour > 21) {
            // Create an alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Appointment Time");
            alert.setHeaderText(null);
            alert.setContentText("Please schedule the appointment between business hours (7:00am to 9:00pm CST).");
            // Display the dialog
            alert.showAndWait();
            return 0;
        }
        if (isAppointmentOverlap(startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment Overlap");
            alert.setHeaderText(null);
            alert.setContentText("The appointment time overlaps with another appointment. Please choose a different time.");
            alert.showAndWait();
            return 0;
        }
        else {
            // Get the selected contact name and convert it to its corresponding ID
            String selectedContactName = contactNameCbx.getSelectionModel().getSelectedItem();
            int contactId = getContactId(selectedContactName);

            String sql = "INSERT INTO APPOINTMENTS (Title, Description, Contact_ID, Customer_ID, User_ID, Location, Type, Start, End) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, titleTxt.getText());
            ps.setString(2, descriptionTxt.getText());
            ps.setInt(3, contactId); // Set the Contact_ID value to the contactId
            ps.setInt(4, Integer.parseInt(customerIdTxt.getText()));
            ps.setInt(5, userId); // Set the User_ID value to the userId
            ps.setString(6, locationTxt.getText());
            ps.setString(7, typeTxt.getText());
            ps.setTimestamp(8, startTimestamp); // Set the Start value to the startTimestamp
            ps.setTimestamp(9, endTimestamp); // Set the End value to the endTimestamp
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        }
    }
}