package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dao.JDBC.connection;

public class userQuery {

    public static int insert(String username, String password) throws SQLException {
        String sql = "INSERT INTO USERS (User_Name, Password) VALUES(?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }


    public static int update(int userId, String username) throws SQLException {
        String sql = "UPDATE USERS SET User_Name = ? WHERE User_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, userId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int delete(int userId) throws SQLException {
        String sql = "DELETE FROM USERS WHERE User_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static void select() throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            System.out.print(userId + "  ");
            System.out.print(username + "\n");
        }
    }
    public static void selectFiltered(int userid) throws SQLException{
        String sql = "SELECT * FROM USERS WHERE User_ID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userid);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");
            System.out.print(userId + "  ");
            System.out.print(password + "  ");
            System.out.print(username + "\n");
        }
    }
}
