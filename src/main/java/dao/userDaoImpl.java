package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.appointment;
import model.user;

import java.sql.ResultSet;
import java.sql.SQLException;

public class userDaoImpl {

    static boolean act;
        public static user getUser(String userName) throws SQLException, Exception{
            // type is name or phone, value is the name or the phone #
            JDBC.openConnection();
            String sqlStatement="select * FROM users WHERE User_Name  = '" + userName+ "'";
            //  String sqlStatement="select FROM address";
            Query.makeQuery(sqlStatement);
            user userResult;
            ResultSet result=Query.getResult();
            while(result.next()){
                int userid=result.getInt("User_ID");
                String userNameG=result.getString("User_Name");
                String password=result.getString("Password");
                userResult= new user(userid, userName, password);
                return userResult;
            }
            JDBC.closeConnection();
            return null;
        }


    public static int getUserId(String username) throws SQLException {
        JDBC.openConnection();
        String sqlStatement = "SELECT User_ID FROM users WHERE User_Name = '" + username + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        if (result.next()) {
            int userId = result.getInt("User_ID");
            JDBC.closeConnection();
            return userId;
        } else {
            JDBC.closeConnection();
            throw new RuntimeException("User not found");
        }
    }


        public static ObservableList<user> getAllUsers() throws SQLException, Exception{
            ObservableList<user> allUsers= FXCollections.observableArrayList();
            JDBC.openConnection();
            String sqlStatement="select * from users";
            Query.makeQuery(sqlStatement);
            ResultSet result=Query.getResult();
            while(result.next()){
                int userid=result.getInt("User_ID");
                String userNameG=result.getString("User_Name");
                String password=result.getString("Password");
                user userResult= new user(userid, userNameG, password);
                allUsers.add(userResult);

            }
            JDBC.closeConnection();
            return allUsers;
        }

    public static ObservableList<appointment> getAllAppointments() throws SQLException, Exception{
        ObservableList<appointment> allAppointments= FXCollections.observableArrayList();
        JDBC.openConnection();
        String sqlStatement="select * from appointments";
        Query.makeQuery(sqlStatement);
        ResultSet result=Query.getResult();
        while(result.next()){
            int appointmentId=result.getInt("Appointment_ID");
            String Title=result.getString("title");
            String description=result.getString("Description");
            String location =result.getString("Location");
            String type = result.getString("Type");
            String start = result.getString("Start");
            String end = result.getString("End");
            String createDate = result.getString("Create_Date");
            String createdBy = result.getString("Created_By");
            String lastUpdate = result.getString("Last_Update");
            String lastUpdatedBy = result.getString("Last_Updated_By");
            int customerId = result.getInt("Customer_ID");
            int userId = result.getInt("User_ID");
            int contactId = result.getInt("Contact_ID");
            appointment appointmentResult= new appointment(appointmentId, Title, description, location,
            type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId);
            allAppointments.add(appointmentResult);

        }
        JDBC.closeConnection();
        return allAppointments;
    }

}
