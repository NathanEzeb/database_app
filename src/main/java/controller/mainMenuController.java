package controller;

import dao.JDBC;
import dao.appointmentQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static dao.JDBC.connection;
/**
 * The mainMenuController class handles the actions and events of the main menu view in the application.
 * It provides methods to handle action events of various buttons such as add appointment, modify appointment, delete appointment, reports, and logout.
 * It also provides methods to populate the appointment table based on different criteria such as all appointments, appointments by month, and appointments by week.
 * The class interacts with the database to retrieve and manipulate appointment data.
 * It uses the JDBC connection to fetch and delete appointment data based on different criteria such as appointment ID, user ID, and time.
 * The class also handles the initialization of the main menu by checking for any upcoming appointments within the next 15 minutes and displaying an alert if there are any.
 * It translates the text on the main menu to the appropriate language (English or French) based on the locale.
 */
public class mainMenuController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<appointment> appointmentTbl;
    @FXML
    private TableColumn<appointment, Integer> AppointmentIdCol;

    @FXML
    private Button addAppointmentBtn;

    @FXML
    private DatePicker appointmentDate;

    @FXML
    private ToggleGroup appointmentScheduleGroup;

    @FXML
    private TableColumn<?, ?> contactCol;

    @FXML
    private TableColumn<appointment, Integer> customerIdCol;

    @FXML
    private Button deleteAppointmentBtn;

    @FXML
    private TableColumn<appointment, String> descriptionCol;

    @FXML
    private TableColumn<?, ?> endCol;


    @FXML
    private TableColumn<?, ?> createDateCol;

    @FXML
    private TableColumn<appointment, String> locationCol;

    @FXML
    private Button logoutButton;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button modifyAppointmentBtn;

    @FXML
    private TableColumn<?, ?> startCol;

    @FXML
    private TableColumn<?, ?> createdByCol;


    @FXML
    private Label ApmtScheLbl;

    @FXML
    private Label timezoneLbl;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<appointment, String> lastUpdateCol;

    @FXML
    private TableColumn<?, ?> lastUpdatedByCol;

    @FXML
    private TableColumn<?, ?> contactIdCol;

    @FXML
    private TableColumn<?, ?> userIdCol;

    @FXML
    private RadioButton viewAllRbn;

    @FXML
    private RadioButton viewByMonthRbn;
    private Month selectedMonth;

    @FXML
    private RadioButton viewByWeekRbn;

    @FXML
    private RadioButton viewCustomerRbn;

    private static String username = null;
    private static String password = null;
    private int selectedWeek;


    private static ObservableList<appointment> allAppointments = FXCollections.observableArrayList();
    public static ObservableList<appointment> getAllAppointments() {
        return allAppointments;
    }


    private static int userId;

    public static void setUserId(int userId) {
        mainMenuController.userId = userId;
    }
    /**
     * This method retrieves all appointments for a specific user from the database.
     * It prepares a SQL SELECT statement with the user's ID as a parameter, executes the statement,
     * and adds each returned appointment to an ObservableList.
     *
     * @return An ObservableList of all appointments for the specific user.
     * @throws SQLException if a database access error occurs.
     */
    public static ObservableList<appointment> returnSelectFill() throws SQLException {
        allAppointments.clear();
        String sql = "SELECT * FROM APPOINTMENTS WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId); // Use the userId field to filter the appointments
        ResultSet rs = ps.executeQuery();
        System.out.println("User ID: " + userId);

        while(rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");

            // Convert the start and end times to the user's local time zone
            Timestamp startTimestamp = rs.getTimestamp("Start");
            Timestamp endTimestamp = rs.getTimestamp("End");
            ZonedDateTime startUtc = startTimestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
            ZonedDateTime endUtc = endTimestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
            ZonedDateTime startLocal = startUtc.withZoneSameInstant(ZoneId.systemDefault());
            ZonedDateTime endLocal = endUtc.withZoneSameInstant(ZoneId.systemDefault());

            // Format the start and end times as strings
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String start = startLocal.format(formatter);
            String end = endLocal.format(formatter);

            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            appointment appointmentResult= new appointment(appointmentId, title, description, location,
                    type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId);
            allAppointments.add(appointmentResult);
        }
        return allAppointments;
    }


    /**
     * This method populates the TableView with appointment data for a specific user.
     * It retrieves all appointments for the user from the database by calling the returnSelectFill method,
     * and sets this data as the items of the TableView.
     * It also sets the cell value factories for each column of the TableView.
     *
     * @throws SQLException if a database access error occurs.
     */
    private void populateSelectedTable() throws SQLException {
        ObservableList<appointment> appointmentData = returnSelectFill();
        AppointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTbl.setItems(appointmentData);
    }

    public void populateTable() throws SQLException {
        ObservableList<appointment> appointmentData;
        if (viewByMonthRbn.isSelected() && selectedMonth != null) {
            appointmentData = appointmentQuery.returnAppointmentsByMonth(selectedMonth, userId);
        } else if (viewByWeekRbn.isSelected() && selectedWeek != 0) {
            appointmentData = appointmentQuery.returnAppointmentsByWeek(selectedWeek, userId);
        } else {
            appointmentData = appointmentQuery.returnSelect();
        }
        appointmentTbl.setItems(appointmentData);
    }
    private void clearTable() {
        appointmentTbl.getItems().clear();
    }

    /**
     * This method initializes the main menu controller with the user's ID.
     * It sets the user ID and populates the table with the user's appointments.
     * It also checks for any upcoming appointments within the next 15 minutes and displays an alert if there are any.
     * Additionally, it sets up listeners for the viewAllRbn, viewByWeekRbn, and viewByMonthRbn RadioButtons to populate the table based on the selected view.
     *
     * @param userId The ID of the user who has logged in.
     * @throws RuntimeException if there is an error checking for upcoming appointments or populating the table.
     */
    public void initData(int userId) {
        setUserId(userId);
        try {
            populateSelectedTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method checks for any upcoming appointments within the next 15 minutes for the logged-in user.
     * It prepares a SQL SELECT statement with the user's ID and the current time as parameters, and executes the statement.
     * If there is an appointment in the next 15 minutes, it displays an alert with the appointment details.
     * If there are no upcoming appointments in the next 15 minutes, it displays an alert indicating this.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void checkUpcomingAppointments() throws SQLException {
        // Get the current time
        LocalDateTime now = LocalDateTime.now();

        // Prepare the SQL SELECT statement
        String sql = "SELECT * FROM APPOINTMENTS WHERE User_ID = ? AND Start BETWEEN ? AND ?";

        // Create the PreparedStatement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Set the placeholders
        ps.setInt(1, userId);
        ps.setTimestamp(2, Timestamp.valueOf(now));
        ps.setTimestamp(3, Timestamp.valueOf(now.plus(15, ChronoUnit.MINUTES)));

        // Execute the PreparedStatement
        ResultSet rs = ps.executeQuery();

        // Create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcoming Appointments");
        alert.setHeaderText(null);

        // Check if there is an appointment in the next 15 minutes
        if (rs.next()) {
            // Get the appointment details
            int appointmentId = rs.getInt("Appointment_ID");
            String start = rs.getString("Start");

            // Set the content of the alert dialog
            alert.setContentText("You have an appointment in the next 15 minutes. Appointment ID: " + appointmentId + ", Start Time: " + start);
        } else {
            // Set the content of the alert dialog
            alert.setContentText("There are no upcoming appointments in the next 15 minutes.");
        }

        // Display the dialog
        alert.showAndWait();
    }

    /**
     * This method initializes the main menu controller.
     * It sets the username and checks for any upcoming appointments.
     * It also sets up listeners for the viewAllRbn, viewByWeekRbn, and viewByMonthRbn RadioButtons to populate the table based on the selected view.
     *
     * The lambda expressions are used to set up these listeners for efficiency and readability:
     * 1. The first lambda expression listens for changes in the selected item of the viewAllRbn RadioButton.
     *    When the RadioButton is selected, it calls the populateTable method. This allows for immediate response to user interaction.
     * 2. The second lambda expression listens for changes in the selected item of the viewByWeekRbn RadioButton.
     *    When the RadioButton is selected, it opens a dialog for the user to enter a week number,
     *    then calls the populateTable method with the entered week number. This provides a convenient and compact way to handle user input and update the table accordingly.
     * 3. The third lambda expression listens for changes in the selected item of the viewByMonthRbn RadioButton.
     *    When the RadioButton is selected, it opens a dialog for the user to choose a month,
     *    then calls the populateTable method with the chosen month. This allows for immediate response to user interaction and updates the table based on the user's choice.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     * @throws RuntimeException if there is an error checking for upcoming appointments.
     */
    public void initialize(URL location, ResourceBundle resources){
        username = loginPageController.getUsername();
        timezoneLbl.setText(username);
        // Check for upcoming appointments
        try {
            checkUpcomingAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // This lambda expression listens for changes in the selected item of the viewAllRbn RadioButton.
        // When the RadioButton is selected, it calls the populateTable method.
        viewAllRbn.setOnAction(event -> {
            if (viewAllRbn.isSelected()) {
                try {
                    populateTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // This lambda expression listens for changes in the selected item of the viewByWeekRbn RadioButton.
        // When the RadioButton is selected, it opens a dialog for the user to enter a week number,
        // then calls the populateTable method with the entered week number.
        viewByWeekRbn.setOnAction(event -> {
            if (viewByWeekRbn.isSelected()) {
                TextInputDialog dialog = new TextInputDialog("1");
                dialog.setTitle("Select Week");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter a week number (1-52):");

                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    selectedWeek = Integer.parseInt(result.get());
                    try {
                        populateTable();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        viewByMonthRbn.setOnAction(event -> {
            if (viewByMonthRbn.isSelected()) {
                List<Month> choices = Arrays.asList(Month.values());
                ChoiceDialog<Month> dialog = new ChoiceDialog<>(Month.JANUARY, choices);
                dialog.setTitle("Select Month");
                dialog.setHeaderText(null);
                dialog.setContentText("Choose a month:");

                // Traditional way to get the response value
                Optional<Month> result = dialog.showAndWait();
                if (result.isPresent()){
                    selectedMonth = result.get();
                    try {
                        populateTable();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    /**
     * This method handles the action when the "Modify Appointment" button is clicked.
     * It retrieves the selected appointment from the TableView and passes it to the modifyAppointment scene.
     * It then loads the modifyAppointment scene and sets it on the stage.
     *
     * @param event The ActionEvent object representing the "Modify Appointment" button click event.
     * @throws IOException if there is an error loading the modifyAppointment scene.
     */
    @FXML
    void goToModifyAppointment(ActionEvent event) throws IOException {
        // Get the selected appointment

        appointment selectedAppointment = appointmentTbl.getSelectionModel().getSelectedItem();

        // Check if an appointment is selected
        if (selectedAppointment == null) {
            System.out.println("No appointment selected");
            return;
        }

        // Pass the selected appointment to the modifyAppointment scene
        modifyAppointmentController.setSelectedAppointment(selectedAppointment);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyAppointment.fxml"));
        Parent root = loader.load();
        modifyAppointmentController controller = loader.getController();
        controller.setUserId(userId); // Pass the userId to the addAppointmentController
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method handles the action when the "Add Appointment" button is clicked.
     * It loads the addAppointment scene and sets it on the stage.
     * It also passes the user's ID to the addAppointmentController.
     *
     * @param event The ActionEvent object representing the "Add Appointment" button click event.
     * @throws IOException if there is an error loading the addAppointment scene.
     */
    @FXML
    void goToAddAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addAppointment.fxml"));
        Parent root = loader.load();
        addAppointmentController controller = loader.getController();
        controller.setUserId(userId); // Pass the userId to the addAppointmentController
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    /**
     * This method deletes the selected appointment from the database.
     * It retrieves the selected appointment from the TableView, prepares a SQL DELETE statement with the appointment's ID as a parameter, and executes the statement.
     * It then removes the selected appointment from the TableView.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void deleteAppointment() throws SQLException {
        // Get the selected appointment
        appointment selectedAppointment = appointmentTbl.getSelectionModel().getSelectedItem();

        // Prepare the SQL DELETE statement
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";

        // Create the PreparedStatement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Set the placeholder to the ID of the selected appointment
        ps.setInt(1, selectedAppointment.getAppointmentId());

        // Execute the PreparedStatement
        ps.executeUpdate();

        // Remove the selected appointment from the TableView
        appointmentTbl.getItems().remove(selectedAppointment);
    }

    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws SQLException {
        // Get the selected appointment
        appointment selectedAppointment = appointmentTbl.getSelectionModel().getSelectedItem();

        // Delete the appointment
        deleteAppointment();

        // Create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Cancelled");
        alert.setHeaderText(null);
        alert.setContentText("Appointment with ID: " + selectedAppointment.getAppointmentId() + " and type: " + selectedAppointment.getType() + " has been cancelled.");

        // Display the dialog
        alert.showAndWait();
    }

    @FXML
    void goToReports(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report.fxml"));
        Parent root = loader.load();
        reportController controller = loader.getController();
        controller.setUserId(userId); // Pass the userId to the reportController
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("User ID: " + userId);
    }






    @FXML
    void onActionLogout(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/loginPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
