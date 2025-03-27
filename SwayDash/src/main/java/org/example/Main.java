package org.example;

import org.example.controller.HomeController;
import org.example.view.HomeView;

import javax.swing.*;
import java.io.IOException;

public class Main {
  public static final int DEFAULT_MONITOR_INDEX = 0; // 0 = Main, 1 = Other display

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        HomeView frame = new HomeView(DEFAULT_MONITOR_INDEX);
        new HomeController(frame);
        frame.setVisible(true);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}