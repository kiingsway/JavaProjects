package org.example;

import org.example.controller.AppController;
import org.example.dao.TasksAPI;
import org.example.view.AppView;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                TasksAPI.ensureDatatable();
                AppView app = new AppView();
                new AppController(app);
                app.setVisible(true);
            } catch (SQLException e) {
                Constants.SHOW_ERROR_DIALOG(e);
            }
        });
    }
}