package org.example.lanchonete.dao;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DaoUtil {

    private static final String URL = "jdbc:postgresql://localhost:5432/lanchonete";
    private static final String USER = "postgres";
    private static final String PASSWORD = "paixaoh305";

    public Connection conecta() {
        try {
            // Register the PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            return getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // Driver not found
            throw new RuntimeException("PostgreSQL driver not found", e);
        } catch (SQLException e) {
            // Error connecting to the database
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}