package org.example.view;

import org.example.Constants;
import org.example.view.components.*;
import org.example.model.SystemInfo;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

  private boolean isDarkMode = true;

  private final JButton btnMenu = new JButton("⭕");
  private final ChangeMonitorButton btnChangeMonitor = new ChangeMonitorButton("🖥️", this);
  private final JButton btnClose = new JButton("❌");
  private final JButton btnTheme = new JButton("🎨");

  private final DateLabel lblDate = new DateLabel();
  private final ClockLabel lblClock = new ClockLabel();
  private final BatteryLabel lblBattery = new BatteryLabel();
  private final HDLabel lblHD = new HDLabel();
  private final RAMLabel lblRAM = new RAMLabel();

  WeatherPanel weatherPanel = new WeatherPanel(isDarkMode);

  public HomeView(int monitorIndex) {
    setUndecorated(true);
    getContentPane().setBackground(Color.BLACK);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);
    setResizable(false);

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = ge.getScreenDevices();
    Rectangle screenBounds = screens[monitorIndex].getDefaultConfiguration().getBounds();
    setBounds(screenBounds);

    renderMenu();
    //renderWeather();
    renderClock();
    renderSystemInfo();
    setTheme();
  }

  private void renderClock() {
    lblClock.setFont(Constants.FONT_CLOCK);
    Dimension size = lblClock.getPreferredSize();
    lblClock.setBounds(50, 50, size.width, size.height);
    add(lblClock);

    int clockHeight = size.height;

    lblDate.setFont(Constants.FONT_DATE);
    size = lblDate.getPreferredSize();
    lblDate.setBounds(50, clockHeight + 30, size.width, size.height);
    add(lblDate);
  }

  private void renderWeather() {
    weatherPanel.setBounds(50, 250, 200,200);
    add(weatherPanel);
  }

  private void renderMenu() {
    int x = getWidth() - 50 - 80;
    int y = 50;

    btnMenu.setFont(Constants.FONT_ACTION);
    btnMenu.setFocusPainted(false);
    btnMenu.setBorderPainted(false);
    btnMenu.setBounds(x, y, 80, 70);
    add(btnMenu);

    JButton[] buttons = {btnTheme, btnChangeMonitor, btnClose};

    for (int i = 0; i < buttons.length; i++) {
      JButton btn = buttons[i];
      btn.setFont(Constants.FONT_ACTION);
      btn.setFocusPainted(false);
      btn.setBorderPainted(false);
      btn.setBounds(x, y + ((i + 1) * 70) + 10, 80, 70);
      btn.setVisible(false);
      add(btn);
    }
  }

  private void renderSystemInfo() {
    int x = getWidth() - 100 - 80;
    int y = getHeight() - 50 - (40 * 3);

    Component[] components = {lblBattery, lblHD, lblRAM};

    for (Component component : components) {
      component.setBounds(x, y, 110, 40);
      component.setFont(Constants.FONT_ACTION_SM);
      add(component);
      y += 40;
    }
  }

  public void changeTheme() {
    isDarkMode = !isDarkMode;
    setTheme();
  }

  private void setTheme() {
    Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    getContentPane().setBackground(background);
    btnMenu.setBackground(background);
    btnClose.setBackground(background);
    btnChangeMonitor.setBackground(background);
    btnTheme.setBackground(background);

    lblClock.setForeground(foreground);
    lblDate.setForeground(foreground);
    btnMenu.setForeground(foreground);
    btnClose.setForeground(foreground);
    btnChangeMonitor.setForeground(foreground);
    btnTheme.setForeground(foreground);
    lblBattery.setForeground(foreground);
    lblHD.setForeground(foreground);
    lblRAM.setForeground(foreground);

    weatherPanel.setDarkMode(isDarkMode);

    revalidate();
    repaint();
  }

  public JButton btnMenu() {return btnMenu;}

  public JButton btnTheme() {return btnTheme;}

  public JButton btnChangeMonitor() {return btnChangeMonitor;}

  public JButton btnClose() {return btnClose;}
}
