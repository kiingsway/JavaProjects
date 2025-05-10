package org.example;

import org.example.controller.HomeController;
import org.example.dao.GItemsDB;
import org.example.view.HomeView;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GItemsDB.ensureDatatable();
                HomeView view = new HomeView();
                new HomeController(view);
                view.setVisible(true);
            } catch (SQLException e) {
                Constants.SHOW_ERROR_DIALOG(e);
            }
        });
    }
}