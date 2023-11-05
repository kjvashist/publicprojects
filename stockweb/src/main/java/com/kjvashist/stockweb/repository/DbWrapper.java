package com.kjvashist.stockweb.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbWrapper implements AutoCloseable {
        private Connection connection=null;

    public DbWrapper(String[] arrdatabasePath) {
        for(int trtCtr=0;trtCtr<2 && this.connection==null;trtCtr++) {
            try {
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + arrdatabasePath[trtCtr]);
            } catch (SQLException sqle) {
            }
        }
    }

    public ResultSet select(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeQuery();
    }

    public void insert(String table, String[] columns, Object[] values) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO " + table + " (");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            query.append("?");
            if (i < values.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        PreparedStatement statement = connection.prepareStatement(query.toString());
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
        statement.executeUpdate();
    }

    public void close() throws SQLException {
        // Check if the connection is open.
        if (connection != null && !connection.isClosed()) {
            // Close the connection.
            connection.close();
        }
    }
}
