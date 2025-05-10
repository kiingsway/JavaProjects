package org.example.controller;

import org.example.view.HomeView;

import java.sql.SQLException;

public class HomeController {
    private final HomeView view;

    public HomeController(HomeView view) throws SQLException {
        this.view = view;

        view.btnAddEvent().addActionListener(this::createEvent);

        tasksList = new TasksList();
        refreshTaskPanels();
    }
}
