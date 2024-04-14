package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

import static dao.JDBC.connection;

/**
 * The addAppointmentController class handles the actions and events of the add appointment view in the application.
 * It provides methods to handle action events of various buttons such as save and cancel.
 * It also provides methods to check if an appointment overlaps with any existing appointments and to retrieve the ID of a contact from the database using the contact's name.
 * The class interacts with the database to retrieve and manipulate appointment data.
 * It uses the JDBC connection to fetch and insert appointment data based on different criteria such as appointment time and contact name.
 */
public class addAppointmentController implements Initializable {

    Stage stage;
    Parent scene;
    @FXML
    private ComboBox<String> contactNameCbx;
    @FXML
    private TextField appointmentIdTxt;

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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        contactNameCbx.getItems().addAll("Anika Costa", "Daniel Garcia", "Li Lee");
        SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        startTimeHourSpn.setValueFactory(hourValueFactory);
        startTimeHourSpn.getValueFactory().setValue(0); // Set the initial value to 0

        SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        startTimeMinSpn.setValueFactory(minuteValueFactory);
        startTimeMinSpn.getValueFactory().setValue(0); // Set the initial value to 0

        SpinnerValueFactory<Integer> endHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        endTimeHourSpn.setValueFactory(endHourValueFactory);
        endTimeHourSpn.getValueFactory().setValue(0); // Set the initial value to 0

        SpinnerValueFactory<Integer> endMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        endTimeMinSpn.setValueFactory(endMinuteValueFactory);
        endTimeMinSpn.getValueFactory().setValue(0); // Set the initial value to 0
    }

@FXML
void onActionCancel(ActionEvent event) throws IOException, SQLException {
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
     * This method checks if the given appointment overlaps with any existing appointments.
     * It does this by querying the database for any appointments that start or end within
     * the given start and end times, or that encompass the entire given time range.
     *
     * @param startDateTime The start time of the appointment to check.
     * @param endDateTime The end time of the appointment to check.
     * @return true if the appointment overlaps with an existing appointment, false otherwise.
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
     * It does this by preparing a SQL SELECT statement with the contact's name as a parameter,
     * executing the statement, and returning the ID from the result set.
     *
     * @param contactName The name of the contact whose ID is to be retrieved.
     * @return The ID of the contact.
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
     * This method inserts a new appointment into the database.
     * It first retrieves the date and time information from the GUI, and checks if the appointment time is within business hours.
     * If the appointment time is not within business hours, it displays an alert dialog and returns 0.
     * If the appointment time overlaps with another appointment, it displays an alert dialog and returns 0.
     * Otherwise, it retrieves the selected contact name from the ComboBox and converts it to its corresponding ID.
     * Then, it prepares an SQL INSERT statement with the appointment information as parameters, executes the statement, and returns the number of rows affected.
     *
     * @return the number of rows affected by the SQL statement, or 0 if the appointment time is not within business hours or overlaps with another appointment.
     * @throws SQLException if a database access error occurs.
     */
    public int insertAppointment() throws SQLException {
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

    @FXML
    void onActionSave(ActionEvent event) throws SQLException {
        this.insertAppointment();
        System.out.println(userId);
    }
}