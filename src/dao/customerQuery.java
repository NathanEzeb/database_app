package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.appointment;
import model.customer;
import model.inStateCustomer;
import model.outStateCustomer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dao.JDBC.connection;



/**
 * The customerQuery class provides methods to interact with the CUSTOMERS table in the database.
 * It uses the JDBC connection to fetch and manipulate customer data.
 * The class provides methods to return customers by country, by appointment month, by appointment type, and all customers.
 * The returnCustomersByCountry method returns a list of customers for a specific country.
 * The returnCustomersByAppointmentMonth method returns a list of customers who have an appointment in a specific month.
 * The returnCustomersByAppointmentType method returns a list of customers who have an appointment of a specific type.
 * The returnCustomers method fetches all customers from the database and adds them to an ObservableList.
 * The getAllCustomers method returns the ObservableList of all customers.
 */
public class customerQuery {
    public static ObservableList<customer> returnCustomersByCountry(String country) throws SQLException {
        ObservableList<customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CUSTOMERS c " +
                "JOIN first_level_divisions fld ON c.Division_ID = fld.Division_ID " +
                "JOIN countries co ON fld.COUNTRY_ID = co.Country_ID " +
                "WHERE co.Country = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            // Fetch the customer details from the ResultSet and create a new customer object
            // Add the new customer object to the customers list
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            customer customerResult = new customer(customerId, customerName, address, postalCode, phone,
                    createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
            customers.add(customerResult);
        }
        return customers;
    }

    /*
    public static ObservableList<customer> getAllCustomerInfo() throws SQLException {
        ObservableList<customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CUSTOMERS " +
                "LEFT JOIN in_state_customers ON CUSTOMERS.Customer_ID = in_state_customers.Customer_ID " +
                "LEFT JOIN out_state_customers ON CUSTOMERS.Customer_ID = out_state_customers.Customer_ID";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            // Fetch the customer details from the ResultSet
            // Check the type of the customer and create an object of the corresponding class
            // Add the new customer object to the customers list
            // ...
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            String distance = rs.getString("Distance");
            String inStateDiscount = rs.getString("in_state_discount");
            String upcharge = rs.getString("upcharge");

            if(inStateDiscount != null) {
                inStateCustomer customerResult = new inStateCustomer(customerId, customerName, address, postalCode, phone,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId, distance, inStateDiscount);
                customers.add(customerResult);
            } else if(upcharge != null) {
                outStateCustomer customerResult = new outStateCustomer(customerId, customerName, address, postalCode, phone,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId, distance, upcharge);
                customers.add(customerResult);
            } else {
                customer customerResult = new customer(customerId, customerName, address, postalCode, phone,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
                customers.add(customerResult);
            }
        }
        return customers;
    }

     */

    public static ObservableList<customer> getAllCustomerInfo() throws SQLException {
        ObservableList<customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT CUSTOMERS.*, " +
                "COALESCE(in_state_customers.Distance, out_state_customers.Distance) AS Distance, " +
                "in_state_customers.in_state_discount, " +
                "out_state_customers.upcharge " +
                "FROM CUSTOMERS " +
                "LEFT JOIN in_state_customers ON CUSTOMERS.Customer_ID = in_state_customers.Customer_ID " +
                "LEFT JOIN out_state_customers ON CUSTOMERS.Customer_ID = out_state_customers.Customer_ID";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            // Fetch the customer details from the ResultSet
            // Check the type of the customer and create an object of the corresponding class
            // Add the new customer object to the customers list
            // ...
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            String distance = rs.getString("Distance");
            String inStateDiscount = rs.getString("in_state_discount");
            String upcharge = rs.getString("upcharge");

            if(inStateDiscount != null) {
                inStateCustomer customerResult = new inStateCustomer(customerId, customerName, address, postalCode, phone,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId, distance, inStateDiscount);
                customers.add(customerResult);
            } else if(upcharge != null) {
                outStateCustomer customerResult = new outStateCustomer(customerId, customerName, address, postalCode, phone,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId, distance, upcharge);
                customers.add(customerResult);
            } else {
                customer customerResult = new customer(customerId, customerName, address, postalCode, phone,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
                customers.add(customerResult);
            }
        }
        return customers;
    }

    public static ObservableList<customer> returnCustomersByAppointmentMonth(String month) throws SQLException {
        ObservableList<customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CUSTOMERS WHERE Customer_ID IN (SELECT Customer_ID FROM APPOINTMENTS WHERE MONTHNAME(Start) = ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, month);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            // Fetch the customer details from the ResultSet and create a new customer object
            // Add the new customer object to the customers list
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            customer customerResult = new customer(customerId, customerName, address, postalCode, phone,
                    createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
            customers.add(customerResult);
        }
        return customers;
    }
    public static ObservableList<customer> returnCustomersByAppointmentType(String type) throws SQLException {
        ObservableList<customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CUSTOMERS WHERE Customer_ID IN (SELECT Customer_ID FROM APPOINTMENTS WHERE Type = ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            // Fetch the customer details from the ResultSet and create a new customer object
            // Add the new customer object to the customers list
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            customer customerResult = new customer(customerId, customerName, address, postalCode, phone,
                    createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
            customers.add(customerResult);
        }
        return customers;
    }

    private static ObservableList<customer> allCustomers = FXCollections.observableArrayList();

    public static ObservableList<customer> getAllCustomers() {
        return allCustomers;
    }

    public static ObservableList<customer> returnCustomers() throws SQLException {
        allCustomers.clear();
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");
            customer customerResult = new customer(customerId, customerName, address, postalCode, phone,
                    createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
            allCustomers.add(customerResult);
        }
    return allCustomers;
}
}
