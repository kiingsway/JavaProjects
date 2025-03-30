package org.example.controller;

import org.example.view.HomeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.example.Constants.PRINT;

public class HomeController {

  private int monitorIndex;
  private boolean isDarkMode = true;
  private final HomeView view;
  private boolean menuOpened = false;

  public HomeController(HomeView view, int monitorIndex) {
    this.view = view;
    this.monitorIndex = monitorIndex;

    handleListeners();
    setTheme(isDarkMode);
  }

  private void handleListeners() {
    view.btnMenu().addActionListener(_ -> toggleMenu());
    view.btnClose().addActionListener(_ -> System.exit(0));
    view.btnTheme().addActionListener(_ -> setTheme(!isDarkMode));
    view.btnChangeMonitor().addActionListener(_ -> changeMonitor());

    view.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (menuOpened) toggleMenu();
        if (SwingUtilities.isRightMouseButton(e)) view.contextMenu().show(view, e.getX(), e.getY());
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

  private void setTheme(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {view.btnMenu(), view.btnClose(), view.btnChangeMonitor(), view.btnTheme(), view.getContentPane()};
    Component[] foregroundComponents = {view.btnMenu(), view.btnClose(), view.btnChangeMonitor(), view.btnTheme()};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    view.weatherPanel().setTheme(isDarkMode);
    view.clockPanel().setTheme(isDarkMode);
    view.sysInfoPanel().setTheme(isDarkMode);
    view.currencyPanel().setTheme(isDarkMode);

    view.revalidate();
    view.repaint();
  }

  private void changeMonitor() {
    GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    if (monitorIndex + 1 >= screens.length) monitorIndex = 0;
    else monitorIndex += 1;

    GraphicsDevice screen = screens[monitorIndex];
    DisplayMode dm = screen.getDisplayMode();
    int width = dm.getWidth();
    int height = dm.getHeight();
    Rectangle bounds = screen.getDefaultConfiguration().getBounds();

    view.setSize(width, height);
    view.setLocation(bounds.x, bounds.y);
    view.setExtendedState(JFrame.MAXIMIZED_BOTH);

    int w = view.getWidth(), h = view.getHeight();
    int x = w - 250, y = h - 210;

    view.sysInfoPanel().setBounds(x, y, 250, 200);
    view.currencyPanel().setBounds(50, y, 250, 200);

    view.revalidate();
    view.repaint();
  }
}
