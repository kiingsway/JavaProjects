package org.example;

import org.example.controller.HomeController;
import org.example.view.Home;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Home view = new Home();
            new HomeController(view);
            view.setVisible(true);
        });
    }
}