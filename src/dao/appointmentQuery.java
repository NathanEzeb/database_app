package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

import static dao.JDBC.connection;

/**
 * The appointmentQuery class provides methods to interact with the APPOINTMENTS table in the database.
 * It uses the JDBC connection to fetch and manipulate appointment data.
 * The class provides methods to return appointments by month and week, select all appointments, and insert a new appointment.
 * The returnAppointmentsByMonth method returns a list of appointments for a specific user in a specific month.
 * The returnAppointmentsByWeek method returns a list of appointments for a specific user in a specific week.
 * The select method fetches all appointments from the database and adds them to an ObservableList.
 * The returnSelect method fetches all appointments from the database and returns them as an ObservableList.
 * The insertAppointment method inserts a new appointment into the database and returns the number of rows affected.
 */
public class appointmentQuery {
    public static ObservableList<appointment> returnAppointmentsByMonth(Month month, int userId) throws SQLException {
        ObservableList<appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM APPOINTMENTS WHERE User_ID = ? AND MONTH(Start) = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, month.getValue());
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String start = rs.getString("Start");
            String end = rs.getString("End");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int contactId = rs.getInt("Contact_ID");
            appointment appointmentResult= new appointment(appointmentId, title, description, location,
                    type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId);
            appointments.add(appointmentResult);
        }
        return appointments;
    }
    public static ObservableList<appointment> returnAppointmentsByWeek(int week, int userId) throws SQLException {
        ObservableList<appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM APPOINTMENTS WHERE User_ID = ? AND WEEK(Start) = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, week);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String start = rs.getString("Start");
            String end = rs.getString("End");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int contactId = rs.getInt("Contact_ID");

            appointment appointmentResult= new appointment(appointmentId, title, description, location,
                    type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId);
            appointments.add(appointmentResult);
        }
        return appointments;
    }
    private static ObservableList<appointment> allAppointments = FXCollections.observableArrayList();

    public static ObservableList<appointment> getAllAppointments() {
        return allAppointments;
    }

    public static void select() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String type = rs.getString("Type");
            allAppointments.add((appointment) rs);
            System.out.print(appointmentId + "  ");
            System.out.print(type + "\n");
        }
    }
    public static ObservableList<appointment> returnSelect() throws SQLException {
        allAppointments.clear();
        String sql = "SELECT * FROM APPOINTMENTS";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();


        while(rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String start = rs.getString("Start");
            String end = rs.getString("End");
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

    public static ObservableList<appointment> getAppointmentsByContactId(int contactId) throws SQLException {
        ObservableList<appointment> appointmentData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM APPOINTMENTS WHERE Contact_ID = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, contactId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String start = rs.getString("Start");
            String end = rs.getString("End");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            //int contactId = rs.getInt("Contact_ID"); // You already have contactId as a parameter

            appointment appointment = new appointment(appointmentId, title, description, location,
                    type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId);
            appointmentData.add(appointment);
        }

        return appointmentData;
    }

    public static int insertAppointment(String username, String password) throws SQLException {
        String sql = "INSERT INTO USERS (appointmentId, title, description, location,\n" +
                "             type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, " +
                "userId, contactId) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
