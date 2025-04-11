package org.example.controller;

import org.example.model.components.ThemedPanel;
import org.example.Constants;
import org.example.view.HomeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HomeController {

  private boolean isDarkMode = true;
  private int monitorIndex;
  private final HomeView view;

  public HomeController(HomeView view, int monitorIndex) {
    this.view = view;
    this.monitorIndex = monitorIndex;

    PowerManager.preventSleep();

    view.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        closeApp();
      }
    });

    handleListeners();
    setTheme(isDarkMode);
  }

  private void handleListeners() {
    view.btnTheme().addActionListener(e -> setTheme(!isDarkMode));
    view.btnChangeMonitor().addActionListener(e -> changeMonitor());
    view.btnAppLog().addActionListener(e -> toggleAppLog());
    view.btnCloseApp().addActionListener(e -> closeApp());

    view.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        hideMenu();
        if (SwingUtilities.isRightMouseButton(e)) view.contextMenu().show(view, e.getX(), e.getY());
      }
    });
  }

  private void hideMenu() {
    if (!view.contextMenu().isVisible()) return;
    view.contextMenu().setVisible(false);

    view.revalidate();
    view.repaint();
  }

  private void setTheme(boolean isDarkMode) {
    hideMenu();
    this.isDarkMode = isDarkMode;
    Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {view.btnAppLog(), view.btnCloseApp(), view.btnChangeMonitor(), view.btnTheme(), view.getContentPane()};
    Component[] foregroundComponents = {view.btnAppLog(), view.btnCloseApp(), view.btnChangeMonitor(), view.btnTheme()};
    ThemedPanel[] themedComponents = {view.weatherPanel(), view.clockPanel(), view.sysInfoPanel(), view.currencyPanel(), view.appLogPanel()};

    for (Component c : backgroundComponents) c.setBackground(background);
    for (Component c : foregroundComponents) c.setForeground(foreground);
    for (ThemedPanel c : themedComponents) c.setTheme(isDarkMode);

    view.revalidate();
    view.repaint();
  }

  private void changeMonitor() {
    hideMenu();
    GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    if (monitorIndex + 1 >= screens.length) monitorIndex = 0;
    else monitorIndex += 1;

    GraphicsDevice screen = screens[monitorIndex];
    DisplayMode dm = screen.getDisplayMode();
    Dimension size = new Dimension(dm.getWidth(), dm.getHeight());
    Rectangle bounds = screen.getDefaultConfiguration().getBounds();

    view.appLogPanel().setScreenSize(new Rectangle(size.width, size.height));

    view.setSize(size.width, size.height);
    view.setLocation(bounds.x, bounds.y);
    view.setExtendedState(JFrame.MAXIMIZED_BOTH);

    int w = view.getWidth(), h = view.getHeight();
    int x = w - 250, y = h - 210;

    view.sysInfoPanel().setBounds(x, y, 250, 200);
    view.currencyPanel().setBounds(50, y, 250, 200);

    view.revalidate();
    view.repaint();
  }

  private void toggleAppLog() {
    hideMenu();
    view.appLogPanel().setVisible(!view.appLogPanel().isVisible());
  }

  private void closeApp() {
    String tit = Constants.APP_TITLE;
    String msg = "Are you sure you want to exit?";
    int resp = JOptionPane.showConfirmDialog(view, msg, tit, JOptionPane.YES_NO_OPTION);
    if (resp == JOptionPane.YES_OPTION) {
      PowerManager.allowSleep();
      view.dispose();
      System.exit(0);
    }
  }
}
