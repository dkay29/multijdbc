package org.dkay229.multijdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class MultiDriver implements Driver {

    static {
        try {
            DriverManager.registerDriver(new MultiDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            throw new SQLException(new MultiException(MultiErrorCode.MALFORMED_JDBC_URL,url));
        }
        // Implement connection logic here
        MultiConnection connection = new MultiConnection();
        connection
        return connection;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        // Check if the URL is valid for this driver
        return url.startsWith("jdbc:multisql:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        // Return driver-specific properties
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}

