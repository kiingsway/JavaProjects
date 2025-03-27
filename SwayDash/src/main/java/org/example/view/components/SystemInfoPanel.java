package org.example.view.components;

import org.example.model.SystemInfo;

import javax.swing.*;
import java.awt.*;

public class SystemInfoPanel extends JPanel {

  private boolean isDarkMode;
  private final SystemInfo sys = new SystemInfo();

  private static final IconLabelPanel lblBattery = new IconLabelPanel();
  private static final JLabel lblHD = new JLabel();
  private static final JLabel lblRAM = new JLabel();

  public SystemInfoPanel(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    setTheme(isDarkMode);

    setLayout(null);

    lblBattery.setBounds(10, 10, 100, 20);

    add(lblBattery);

    updateValues();
    Timer timer = new Timer(1000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    lblBattery.setText(sys.getBatteryPercentage(true));

  }

  public void setTheme(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    Color foreground = this.isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = this.isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foregroundComponents = {lblBattery, lblHD, lblRAM};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }
}
