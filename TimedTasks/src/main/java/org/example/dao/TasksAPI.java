package org.example.dao;

import org.example.model.Task;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

public class TasksAPI {

    private static final String DB_URL = "jdbc:sqlite:database";
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static void ensureDatatable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                eta TEXT,
                start_time TEXT,
                end_time TEXT
            );
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public Task addTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, eta, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, task.title());
            stm.setString(2, task.ETATime());
            stm.setString(3, task.startTime());
            stm.setString(4, task.endTime());
            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                int taskId = rs.getInt(1);
                task.setId(taskId);
                return task;
            }
        }
        return task;
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String eta = rs.getString("eta");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");

                Date ETA = null, startDateTime = null, endDateTime = null;

                try {
                    ETA = Optional.ofNullable(eta).map(this::safeParse).orElse(null);
                    startDateTime = Optional.ofNullable(startTime).map(this::safeParse).orElse(null);
                    endDateTime = Optional.ofNullable(endTime).map(this::safeParse).orElse(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Task task = new Task(id, title, ETA, startDateTime, endDateTime);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void updateTask(Task task) throws SQLException {
        if (task.id() < 1) return;
        String sql = "UPDATE tasks SET title = ?, eta = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.title());
            pstmt.setString(2, task.ETATime());
            pstmt.setString(3, task.startTime());
            pstmt.setString(4, task.endTime());
            pstmt.setInt(5, task.id());
            pstmt.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Date safeParse(String time) {
        try {
            return timeFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
