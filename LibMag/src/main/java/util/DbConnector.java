package util;

import java.sql.*;
import javax.servlet.ServletException;

public class DbConnector {

    private static Connection makeConnection() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/library_management?useSSL=false", "root", "admin123");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Servlet Could not display records.", e);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found.", e);
        }
    }

    public static Object get(String query, DBQueryRunner runner) throws Exception {
        Connection connection = makeConnection();
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery(query);
            return runner.execute(rs);
        } finally {
            connection.close();
        }
    }

    public static void update(String query) throws Exception {
        Connection connection = makeConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } finally {
            connection.close();
        }
    }

    public static int insert(String query) throws Exception {
        Connection connection = makeConnection();
        try {

            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        } finally {
            connection.close();
        }
    }
}

