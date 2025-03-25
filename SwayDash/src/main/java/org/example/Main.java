package org.example;

import org.example.controller.HomeController;
import org.example.view.HomeView;

import javax.swing.*;

public class Main {
  public static int DEFAULT_MONITOR_INDEX = 1; // 0 = Main, 1 = Other display

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      HomeView frame = new HomeView(DEFAULT_MONITOR_INDEX);
      new HomeController(frame);
      frame.setVisible(true);
    });
  }
}