package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GItemsDB {

    private static final String DB_URL = "jdbc:sqlite:database";

    public static void ensureDatatable() throws SQLException {
        if (true) return;
        final String createTableSQL = """
                CREATE TABLE IF NOT EXISTS gitems (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createTableSQL);
        }
    }
}
