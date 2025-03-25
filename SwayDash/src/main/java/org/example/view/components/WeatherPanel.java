package org.example.view.components;

import org.example.Constants;

import javax.swing.*;
import java.awt.*;

public class WeatherPanel extends JPanel {

  private final JLabel txtTemperature = new JLabel("0C");

  public WeatherPanel(boolean isThemeDark) {
    txtTemperature.setForeground(isThemeDark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    txtTemperature.setFont(Constants.FONT_CLOCK);
    txtTemperature.setHorizontalAlignment(SwingConstants.LEFT);
    add(txtTemperature, BorderLayout.WEST);
  }

  public void setDarkMode(boolean isThemeDark) {
    setBackground(!isThemeDark ? Color.BLACK : Color.WHITE);
    txtTemperature.setForeground(isThemeDark ? Color.LIGHT_GRAY : Color.DARK_GRAY);

    revalidate();
    repaint();
  }
}
