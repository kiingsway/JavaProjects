package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class EventsAPI {

    private static final String DB_URL = "jdbc:sqlite:database";
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static void ensureDatatable() throws SQLException {
        final String createTableSQL = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    date TEXT NOT NULL,
                    startTime TEXT NOT NULL,
                    endTime TEXT NOT NULL,
                    location TEXT
                );
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createTableSQL);
        }
    }

}
