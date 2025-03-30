package org.example.view.components;

import org.example.Constants;
import org.example.dao.WeatherAPI;
import org.example.model.components.ThemedPanel;

import javax.swing.*;
import java.awt.*;

public class WeatherPanel extends JPanel implements ThemedPanel {

  private boolean isDarkMode;
  private final WeatherAPI weather = new WeatherAPI(WeatherAPI.TOCA_WEATHER);
  private final JLabel lblTemperature = new JLabel();
  private final JLabel lblHigh = new JLabel();
  private final JLabel lblLow = new JLabel();
  private final JLabel lblStatus = new JLabel();
  private final JLabel lblFeelsLike = new JLabel();

  public WeatherPanel(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    setTheme(isDarkMode);

    setLayout(null);

    lblTemperature.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblTemperature.setFont(Constants.FONT_WEATHER);
    lblTemperature.setHorizontalAlignment(SwingConstants.LEFT);
    lblTemperature.setBounds(0, 0, 120, 70);

    lblHigh.setForeground(isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80);
    lblHigh.setFont(Constants.FONT_DATE);
    lblHigh.setHorizontalAlignment(SwingConstants.LEFT);
    lblHigh.setBounds(120, 5, 100, 30);

    lblLow.setForeground(isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80);
    lblLow.setFont(Constants.FONT_DATE);
    lblLow.setHorizontalAlignment(SwingConstants.LEFT);
    lblLow.setBounds(120, 35, 100, 30);

    lblStatus.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblStatus.setFont(Constants.FONT_DATE);
    lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
    lblStatus.setBounds(0, 70, 150, 30);

    lblFeelsLike.setForeground(isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80);
    lblFeelsLike.setFont(Constants.FONT_DATE);
    lblFeelsLike.setHorizontalAlignment(SwingConstants.LEFT);
    lblFeelsLike.setBounds(0, 105, 150, 30);

    add(lblTemperature);
    add(lblHigh);
    add(lblLow);
    add(lblStatus);
    add(lblFeelsLike);

    updateWeather();
    Timer timer = new Timer(1000, _ -> updateWeather());
    timer.start();
  }

  private void updateWeather() {
    new Thread(() -> SwingUtilities.invokeLater(() -> {
      if (weather != null) {
        JLabel[] labels = {lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike};
        Object[] values = {weather.temp(), weather.highLow().high(), weather.highLow().low(), weather.status(), weather.feelsLike()};
        for (int i = 0; i < labels.length; i++) {
          JLabel label = labels[i];
          Object value = values[i];
          String val = value + "°C";

          if (label == lblStatus) val = value + "";
          else if (label == lblFeelsLike) val = "Feels: " + value + "°C";
          else if (value == null) val = "";

          label.setText(val);
        }
      }
    })).start();
  }

  public void setTheme(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;

    Color foreground = this.isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
    Color background = this.isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foregroundComponents = {lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }
}
