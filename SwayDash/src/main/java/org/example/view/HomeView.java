package org.example.view;

import org.example.Constants;
import org.example.view.components.*;

import javax.swing.*;
import java.awt.*;

import static org.example.Constants.PRINT;

public class HomeView extends JFrame {

  private final JButton btnTheme = new JButton("🎨");
  private final JButton btnChangeMonitor = new JButton("️🖥️");
  private final JButton btnAppLog = new JButton("\uD83D\uDDC2\uFE0F");
  private final JButton btnClose = new JButton("❌");

  private final WeatherPanel weatherPanel = new WeatherPanel(true);
  private final AppLogPanel appLogPanel;
  private final ClockPanel clockPanel = new ClockPanel(true);
  private final SystemInfoPanel sysInfoPanel = new SystemInfoPanel(true);
  private final CurrencyPanel currencyPanel = new CurrencyPanel(true);

  private final JPopupMenu contextMenu = new JPopupMenu();

  public HomeView(int initialMonitorIndex) {
    setTitle(Constants.APP_TITLE);
    getContentPane().setBackground(Color.BLACK);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setUndecorated(true);
    setResizable(false);
    setLayout(null);

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = ge.getScreenDevices();
    initialMonitorIndex = initialMonitorIndex >= screens.length ? screens.length - 1 : initialMonitorIndex;
    Rectangle screenBounds = screens[initialMonitorIndex].getDefaultConfiguration().getBounds();
    setBounds(screenBounds);
    appLogPanel = new AppLogPanel(true, screenBounds);

    renderContextMenu();
    renderPanels();
  }

  private void renderPanels() {
    clockPanel.setBounds(50, 50, 350, 115);
    add(clockPanel);

    weatherPanel.setBounds(50, 250, 300, 250);
    add(weatherPanel);

    int w = getWidth(), h = getHeight();
    int x = w - 250, y = h - 210;

    appLogPanel.setBounds(w - 450, 50, 400, 300);
    appLogPanel.setVisible(false);
    add(appLogPanel);

    sysInfoPanel.setBounds(x, y, 250, 200);
    add(sysInfoPanel);

    currencyPanel.setBounds(50, y, 250, 200);
    add(currencyPanel);
  }

  private void renderContextMenu() {
    UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Color.GRAY, 1));
    UIManager.put("PopupMenu.background", new Color(0, 0, 0, 0));

    JButton[] buttons = {btnTheme, btnChangeMonitor, btnAppLog, btnClose};

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

  public JButton btnTheme() {return btnTheme;}

  public JButton btnChangeMonitor() {return btnChangeMonitor;}

  public JButton btnAppLog() {return btnAppLog;}

  public JButton btnClose() {return btnClose;}

  public ClockPanel clockPanel() {return clockPanel;}

  public SystemInfoPanel sysInfoPanel() {return sysInfoPanel;}

  public WeatherPanel weatherPanel() {return weatherPanel;}

  public CurrencyPanel currencyPanel() {return currencyPanel;}

  public AppLogPanel appLogPanel() {return appLogPanel;}

  public JPopupMenu contextMenu() {return contextMenu;}
}