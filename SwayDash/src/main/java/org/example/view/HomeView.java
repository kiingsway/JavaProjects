package org.example.view;

import org.example.Constants;
import org.example.view.components.*;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

  private final JButton btnMenu = new JButton("⭕");
  private final JButton btnChangeMonitor = new JButton("️🖥️");
  private final JButton btnClose = new JButton("❌");
  private final JButton btnTheme = new JButton("🎨");

  private final WeatherPanel weatherPanel = new WeatherPanel(true);
  private final ClockPanel clockPanel = new ClockPanel(true);
  private final SystemInfoPanel sysInfoPanel = new SystemInfoPanel(true);
  private final CurrencyPanel currencyPanel = new CurrencyPanel(true);

  private final JPopupMenu contextMenu = new JPopupMenu();

  public HomeView(int monitorIndex) {
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

    renderContextMenu();
    renderPanels();
  }

  private void renderPanels() {
    clockPanel.setBounds(50, 50, 350, 150);
    add(clockPanel);

    weatherPanel.setBounds(50, 250, 300, 250);
    add(weatherPanel);

    int w = getWidth(), h = getHeight();
    int x = w - 250, y = h - 210;

    sysInfoPanel.setBounds(x, y, 250, 200);
    add(sysInfoPanel);

    currencyPanel.setBounds(50, y, 250, 200);
    add(currencyPanel);
  }

  private void renderContextMenu() {
    UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Color.GRAY, 1));
    UIManager.put("PopupMenu.background", new Color(0, 0, 0, 0));

    JButton[] buttons = {btnTheme, btnChangeMonitor, btnClose};

    int width = 0, height = 0;
    for (JButton btn : buttons) {
      btn.setFont(Constants.FONT_ACTION);
      btn.setFocusPainted(false);
      btn.setBorderPainted(false);
      btn.setSize(new Dimension(80, 80));

      height += btn.getPreferredSize().height;
      width = Math.max(width, btn.getPreferredSize().width);
      contextMenu.add(btn);
    }

    contextMenu.setPreferredSize(new Dimension(width + 2, height + 2));
  }

  public JButton btnMenu() {return btnMenu;}

  public JButton btnTheme() {return btnTheme;}

  public JButton btnChangeMonitor() {return btnChangeMonitor;}

  public JButton btnClose() {return btnClose;}

  public ClockPanel clockPanel() {return clockPanel;}

  public SystemInfoPanel sysInfoPanel() {return sysInfoPanel;}

  public WeatherPanel weatherPanel() {return weatherPanel;}

  public CurrencyPanel currencyPanel() {return currencyPanel;}

  public JPopupMenu contextMenu() {return contextMenu;}
}