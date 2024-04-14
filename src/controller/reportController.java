package controller;

import dao.appointmentQuery;
import dao.customerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;
import model.customer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static dao.JDBC.connection;

/**
 * The reportController class handles the actions and events of the report view in the application.
 * It provides methods to handle action events of various buttons such as add, update, delete, cancel, and logout.
 * It also provides methods to populate and clear the customer table.
 * The class interacts with the database to retrieve and manipulate customer data.
 * It uses the customerQuery class to fetch customer data based on different criteria such as appointment type, month, and country.
 */
public class reportController {

    Stage stage;
    Parent scene;

    @FXML private TextField textInputField;
    @FXML private ComboBox<String> dropdownMenu;

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<?, ?> addressCol;

    @FXML
    private Button cancelBtn;

    @FXML
    private ToggleGroup contactScheduleGroup;

    @FXML
    private TableColumn<?, ?> createDateCol;

    @FXML
    private TableColumn<?, ?> createdByCol;

    @FXML
    private TableColumn<?, ?> customerIdCol;

    @FXML
    private TableColumn<?, ?> customerNameCol;
    @FXML
    private TableColumn<?, ?> AppointmentIdCol;
    @FXML
    private TableView<appointment> appointmentTbl;
    @FXML
    private TableColumn<?, ?> contactIdCol;
    @FXML
    private TableColumn<?, ?> endCol;
    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<?, ?> descriptionCol;
    @FXML
    private TableColumn<?, ?> locationCol;
    @FXML
    private TableColumn<?, ?> startCol;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TableColumn<?, ?> userIdCol;


    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<?, ?> divisionIdCol;

    @FXML
    private Label infoLbl;

    @FXML
    private TableColumn<?, ?> lastUpdateCol;

    @FXML
    private TableColumn<?, ?> lastUpdatedByCol;

    @FXML
    private Button logoutBtn;

    @FXML
    private TableColumn<?, ?> phoneCol;
    @FXML
    private TableView<customer> customerTbl;
    @FXML
    private TableColumn<?, ?> postalCodeCol;
    @FXML
    private TableColumn<?, ?> addressCol1;
    @FXML
    private TableView<customer> allCustomerDataTbl;
    @FXML
    private TableColumn<?, ?> customerIdCol1;
    @FXML
    private TableColumn<?, ?> customerNameCol1;

    @FXML
    private TableColumn<?, ?> distanceCol;
    @FXML
    private TableColumn<?, ?> divisionIdCol1;
    @FXML
    private TableColumn<?, ?> inStateDiscountCol;
    @FXML
    private RadioButton moreCustomerInfoRbn;
    @FXML
    private TableColumn<?, ?> phoneCol1;

    @FXML
    private TableColumn<?, ?> postalCodeCol1;

    @FXML
    private TableColumn<?, ?> upchargeCol;
    @FXML
    private TableColumn<?, ?> createDateCol1;
    @FXML
    private TableColumn<?, ?> createdByCol1;
    @FXML
    private TableColumn<?, ?> lastUpdateCol1;
    @FXML
    private TableColumn<?, ?> lastUpdatedByCol1;

    @FXML
    private RadioButton searchRbn;



    @FXML
    private Label selectLbl;

    @FXML
    private RadioButton totalByCountryRbn;

    @FXML
    private RadioButton totalByMonthRbn;

    @FXML
    private RadioButton totalByTypeRbn;
    @FXML
    private RadioButton contactScheduleRbn;

    @FXML
    private Button updateBtn;

    private String selectedType;
    private String selectedMonth;
    private String selectedCountry;
    private static int userId;
    public static void setUserId(int userId) {
        reportController.userId = userId;
    }

    /**
     * This method handles the action event of the cancel button.
     * It retrieves the user details from the database and loads the mainMenu scene.
     * It also passes the User_ID to the mainMenuController.
     *
     * @param event The action event triggered by clicking the cancel button.
     * @throws IOException if there is an error loading the mainMenu scene.
     * @throws SQLException if a database access error occurs.
     */
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

    /*
    @FXML
    void onActionMoreInfo(ActionEvent event) {
        try {
            fillTableColumns(); // Call the method here
            ObservableList<customer> customerData = customerQuery.getAllCustomerInfo();
            allCustomerDataTbl.setItems(customerData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
     */
    private void fillTableColumns() {
        customerIdCol1.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol1.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol1.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol1.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol1.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createDateCol1.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol1.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateCol1.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByCol1.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIdCol1.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        if (distanceCol != null) {
            distanceCol.setCellValueFactory(new PropertyValueFactory<>("distance"));
        }
        //else distanceCol.setCellValueFactory(new PropertyValueFactory<>("distance"));
        if (inStateDiscountCol != null) {
            inStateDiscountCol.setCellValueFactory(new PropertyValueFactory<>("inStateDiscount"));
        }
        //else inStateDiscountCol.setCellValueFactory(new PropertyValueFactory<>("inStateDiscount"));
        if (upchargeCol != null) {
            upchargeCol.setCellValueFactory(new PropertyValueFactory<>("upcharge"));
        }
        //else upchargeCol.setCellValueFactory(new PropertyValueFactory<>("upcharge"));
    }


    /**
     * This method handles the action event of the logout button.
     * It loads the LoginPage scene and sets it on the current stage.
     *
     * @param event The action event triggered by clicking the logout button.
     * @throws IOException if there is an error loading the LoginPage scene.
     */
    @FXML
    void onActionLogout(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method handles the action event of the update button.
     * It retrieves the selected customer from the table and passes it to the modifyCustomer scene.
     * Then it loads the modifyCustomer scene and sets it on the current stage.
     *
     * @param event The action event triggered by clicking the update button.
     * @throws IOException if there is an error loading the modifyCustomer scene.
     */
    @FXML
    void updateCustomer(ActionEvent event) throws IOException {
        // Get the selected customer
        customer selectedCustomer = customerTbl.getSelectionModel().getSelectedItem();

        // Check if a customer is selected
        if (selectedCustomer == null) {
            System.out.println("No customer selected");
            return;
        }

        // Pass the selected appointment to the modifyAppointment scene
        modifyCustomerController.setSelectedCustomer(selectedCustomer);

        // Load the modifyAppointment scene
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void populateAppointmentTable(int contactId) {
        try {
            // Fetch appointments based on the contactId
            ObservableList<appointment> appointmentData = appointmentQuery.getAppointmentsByContactId(contactId);
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
            // Set the data to the TableView
            appointmentTbl.setItems(appointmentData);
        } catch (SQLException e) {
            // Handle any SQL exceptions here
            e.printStackTrace();
        }
    }

    @FXML
    void searchCustomer(ActionEvent event) {
        customerTbl.setVisible(true);
        appointmentTbl.setVisible(false);
        allCustomerDataTbl.setVisible(false);
        // Create a TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Customer");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the customer's name:");

        // Get the user's input
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String customerName = result.get();

            try {
                // Prepare the SQL SELECT statement
                String sql = "SELECT * FROM CUSTOMERS WHERE Customer_Name LIKE ?";
                PreparedStatement ps = connection.prepareStatement(sql);

                // Set the placeholder to the entered customer name
                ps.setString(1, "%" + customerName + "%");

                // Execute the PreparedStatement
                ResultSet rs = ps.executeQuery();

                // Clear the TableView
                customerTbl.getItems().clear();

                // Fetch the result and create a customer object
                while (rs.next()) {
                    int customerId = rs.getInt("Customer_ID");
                    String name = rs.getString("Customer_Name");
                    String address = rs.getString("Address");
                    String postalCode = rs.getString("Postal_Code");
                    String phone = rs.getString("Phone");
                    String createDate = rs.getString("Create_Date");
                    String createdBy = rs.getString("Created_By");
                    String lastUpdate = rs.getString("Last_Update");
                    String lastUpdatedBy = rs.getString("Last_Updated_By");
                    int divisionId = rs.getInt("Division_ID");

                    customer customerResult = new customer(customerId, name, address, postalCode, phone,
                            createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);

                    // Add the customer object to the TableView
                    customerTbl.getItems().add(customerResult);
                }
            } catch (SQLException e) {
                // Handle any SQL exceptions here
                e.printStackTrace();
            }
        }
    }

    /**
     * This method deletes a customer from the database.
     * It first checks if the selected customer has any appointments. If they do, it displays an alert dialog and returns.
     * If the customer has no appointments, it prepares a SQL DELETE statement with the customer's ID and executes the statement.
     * It then removes the customer from the TableView and displays a confirmation dialog.
     *
     * @throws SQLException if a database access error occurs or the customer is not found.
     */
    public void deleteCustomerMethod() throws SQLException {
        // Get the selected customer
        customer selectedCustomer = customerTbl.getSelectionModel().getSelectedItem();

        // Check if the customer has any appointments
        String checkSql = "SELECT * FROM APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement checkPs = connection.prepareStatement(checkSql);
        checkPs.setInt(1, selectedCustomer.getCustomerId());
        ResultSet checkRs = checkPs.executeQuery();
        if (checkRs.next()) {
            // Create an alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cannot Delete Customer");
            alert.setHeaderText(null);
            alert.setContentText("The customer's appointments must be deleted first.");

            // Display the dialog
            alert.showAndWait();
            return;
        }
        // Prepare the SQL DELETE statement
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";

        // Create the PreparedStatement
        PreparedStatement ps = connection.prepareStatement(sql);

        // Set the placeholder to the ID of the selected customer
        ps.setInt(1, selectedCustomer.getCustomerId());

        // Execute the PreparedStatement
        ps.executeUpdate();

        // Remove the selected customer from the TableView
        customerTbl.getItems().remove(selectedCustomer);
        // Create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customer Deleted");
        alert.setHeaderText(null);
        alert.setContentText("Customer deleted.");

        // Display the dialog
        alert.showAndWait();
    }

    /**
     * This method handles the action event of the delete button.
     * It retrieves the selected customer from the table and checks if the customer has any appointments.
     * If the customer has appointments, it displays an alert dialog and returns.
     * If the customer has no appointments, it prepares a SQL DELETE statement with the customer's ID and executes the statement.
     * It then removes the customer from the TableView and displays a confirmation dialog.
     *
     * @param event The action event triggered by clicking the delete button.
     * @throws SQLException if a database access error occurs or the customer is not found.
     */
    @FXML
    void deleteCustomer(ActionEvent event) throws SQLException {
    deleteCustomerMethod();
    }
    /**
     * This method handles the action event of the add button.
     * It loads the addCustomer scene and sets it on the current stage.
     *
     * @param event The action event triggered by clicking the add button.
     * @throws IOException if there is an error loading the addCustomer scene.
     */
    @FXML
    void addCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method clears all items from the customer table.
     */
    private void clearTable() { customerTbl.getItems().clear(); }

    private void populateTable() throws SQLException {
        ObservableList<customer> customerData;
        if (selectedType != null) {
            customerData = customerQuery.returnCustomersByAppointmentType(selectedType);
        } else if (selectedMonth != null) {
            customerData = customerQuery.returnCustomersByAppointmentMonth(selectedMonth);
        } else if (selectedCountry != null) {
            customerData = customerQuery.returnCustomersByCountry(selectedCountry);
        } else {
            customerData = customerQuery.returnCustomers();
        }
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        customerTbl.setItems(customerData);
    }

    public void initialize() {
        customerTbl.setVisible(true);
        appointmentTbl.setVisible(false);
        allCustomerDataTbl.setVisible(false);

        moreCustomerInfoRbn.setOnAction(event -> {
            if (moreCustomerInfoRbn.isSelected()) {
                customerTbl.setVisible(false);
                appointmentTbl.setVisible(false);
                allCustomerDataTbl.setVisible(true);
                try {
                    fillTableColumns();
                    ObservableList<customer> customerData = customerQuery.getAllCustomerInfo();
                    allCustomerDataTbl.setItems(customerData);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        totalByCountryRbn.setOnAction(event -> {
            if (totalByCountryRbn.isSelected()) {
                // Reset the other selected variables
                selectedType = null;
                selectedMonth = null;

                allCustomerDataTbl.setVisible(false);
                appointmentTbl.setVisible(false);
                customerTbl.setVisible(true);
                ChoiceDialog<String> dialog = new ChoiceDialog<>("U.S", "UK", "Canada");
                dialog.setTitle("Select Country");
                dialog.setHeaderText(null);
                dialog.setContentText("Choose customer country:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    selectedCountry = result.get();
                }
                try {
                    populateTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        totalByMonthRbn.setOnAction(event -> {
            if (totalByMonthRbn.isSelected()) {
                // Reset the other selected variables
                selectedType = null;
                selectedCountry = null;

                allCustomerDataTbl.setVisible(false);
                appointmentTbl.setVisible(false);
                customerTbl.setVisible(true);
                ChoiceDialog<String> dialog = new ChoiceDialog<>("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
                dialog.setTitle("Select Month");
                dialog.setHeaderText(null);
                dialog.setContentText("Choose appointment month:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    selectedMonth = result.get();
                }
                try {
                    populateTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        totalByTypeRbn.setOnAction(event -> {
            allCustomerDataTbl.setVisible(false);
            appointmentTbl.setVisible(false);
            customerTbl.setVisible(true);
            if (totalByTypeRbn.isSelected()) {
                // Reset the other selected variables
                selectedMonth = null;
                selectedCountry = null;

                ChoiceDialog<String> dialog = new ChoiceDialog<>("De-Briefing", "Planning session");
                dialog.setTitle("Select Type");
                dialog.setHeaderText(null);
                dialog.setContentText("Choose appointment type:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    selectedType = result.get();
                }
                try {
                    populateTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        contactScheduleRbn.setOnAction(event -> {
            if (contactScheduleRbn.isSelected()) {
                allCustomerDataTbl.setVisible(false);
                customerTbl.setVisible(false);
                appointmentTbl.setVisible(true);
                // rest of your code
                ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, 2, 3);
                dialog.setTitle("Select Contact ID");
                dialog.setHeaderText(null);
                dialog.setContentText("Choose contact ID:");

                Optional<Integer> result = dialog.showAndWait();
                if (result.isPresent()){
                    int selectedContactId = result.get();
                    populateAppointmentTable(selectedContactId);
                }
            }
        });
        clearTable();
        try {
            populateTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}