package org.example.controller;

import org.example.view.HomeView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeController {

  private final HomeView view;
  private boolean menuOpened = false;

  public HomeController(HomeView view) {
    this.view = view;

    handleListeners();
  }

  private void handleListeners() {
    view.btnMenu().addActionListener(_ -> toggleMenu());
    view.btnClose().addActionListener(_ -> System.exit(0));
    view.btnTheme().addActionListener(_ -> view.changeTheme());

    view.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (menuOpened) toggleMenu();
      }
    });
  }

  private void toggleMenu() {
    menuOpened = !menuOpened;
    view.btnClose().setVisible(menuOpened);
    view.btnTheme().setVisible(menuOpened);
    view.btnChangeMonitor().setVisible(menuOpened);

    view.revalidate();
    view.repaint();
  }
}
