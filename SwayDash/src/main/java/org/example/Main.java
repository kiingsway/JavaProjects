package org.example;

import org.example.controller.HomeController;
import org.example.view.HomeView;

import javax.swing.*;

public class Main {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

      int monitor = Constants.DEFAULT_MONITOR_INDEX;
      boolean darkMode = Constants.INITIAL_DARK_MODE;

      HomeView view = new HomeView(monitor);
      new HomeController(view, monitor, darkMode);
      view.setVisible(true);

    });
  }
}