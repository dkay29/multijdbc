package org.dkay229.multijdbc;

import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertNull;

public class MultiDriverTests {
    @Test
    public void testConnect() {
        Exception ex=null;
        try {
            String url = "jdbc:multisql://localhost:3306/mydatabase";
            Class.forName("org.dkay229.multijdbc.MultiDriver");
            Connection conn = DriverManager.getConnection(url, "user", "password");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mytable");
            while (rs.next()) {
                System.out.println("Column 1: " + rs.getString(1));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            ex=e;
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            ex=e;
            e.printStackTrace();
        }
        assertNull(ex);
    }

}
