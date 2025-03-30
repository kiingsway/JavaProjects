package org.example.controller;

import org.example.model.components.ThemedPanel;
import org.example.view.HomeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeController {

  private boolean menuOpened = false, isDarkMode = true;
  private int monitorIndex;
  private final HomeView view;

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
    ThemedPanel[] themedComponents = {view.weatherPanel(), view.clockPanel(), view.sysInfoPanel(), view.currencyPanel()};

    for (Component c : backgroundComponents) c.setBackground(background);
    for (Component c : foregroundComponents) c.setForeground(foreground);
    for (ThemedPanel c : themedComponents) c.setTheme(isDarkMode);

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
