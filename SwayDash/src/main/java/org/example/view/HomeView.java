package org.example.view;

import org.example.Constants;
import org.example.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HomeView extends JFrame {

  private boolean isDarkMode = true;

  private final JButton btnMenu = new JButton("⭕");
  private final ChangeMonitorButton btnChangeMonitor = new ChangeMonitorButton("🖥️", this);
  private final JButton btnClose = new JButton("❌");
  private final JButton btnTheme = new JButton("🎨");

  private final BatteryLabel lblBattery = new BatteryLabel();
  private final HDLabel lblHD = new HDLabel();
  private final RAMLabel lblRAM = new RAMLabel();

  private final JPanel weatherPanel = new JPanel();
  //private final WeatherPanel weatherPanel = new WeatherPanel(isDarkMode);
  private final ClockPanel clockPanel = new ClockPanel(isDarkMode);
  private final SystemInfoPanel sysInfoPanel = new SystemInfoPanel(isDarkMode);

  public HomeView(int monitorIndex) throws IOException {
    setUndecorated(true);
    getContentPane().setBackground(Color.BLACK);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(null);
    setResizable(false);

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = ge.getScreenDevices();
    monitorIndex = monitorIndex >= screens.length ? screens.length - 1 : monitorIndex;
    Rectangle screenBounds = screens[monitorIndex].getDefaultConfiguration().getBounds();
    setBounds(screenBounds);

    renderMenu();
    renderClock();
//    renderWeather();
    renderSystemInfo();
    setTheme();
  }

  private void renderClock() {
    clockPanel.setBounds(50, 50, 350, 150);
    clockPanel.setBackground(Color.WHITE);
    add(clockPanel);
  }

  private void renderWeather() {
    int w = weatherPanel.getPreferredSize().width;
    int h = weatherPanel.getPreferredSize().height;
    weatherPanel.setBounds(50, 250, w, h);
    add(weatherPanel);
  }

  private void renderSystemInfo() {
    int x = getWidth() - 100 - 80;
    int y = getHeight() - 50 - (40 * 3);
    int w = sysInfoPanel.getPreferredSize().width;
    int h = sysInfoPanel.getPreferredSize().height;
    sysInfoPanel.setBounds(x, y, w, h);
    add(sysInfoPanel);
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

  private void renderSystemInf1o() {
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

    // Lista de componentes a serem atualizados
    Component[] backgroundComponents = {btnMenu, btnClose, btnChangeMonitor, btnTheme, getContentPane()};
    Component[] foregroundComponents = {btnMenu, btnClose, btnChangeMonitor, btnTheme, lblBattery, lblHD, lblRAM};

    // Atualiza o fundo (background) e a cor do texto (foreground) do componente
    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    // Atualiza o modo do painel do tempo
    //weatherPanel.setDarkMode(isDarkMode);
    clockPanel.setTheme(isDarkMode);


    revalidate();
    repaint();
  }

  public JButton btnMenu() {return btnMenu;}

  public JButton btnTheme() {return btnTheme;}

  public JButton btnChangeMonitor() {return btnChangeMonitor;}

  public JButton btnClose() {return btnClose;}
}
