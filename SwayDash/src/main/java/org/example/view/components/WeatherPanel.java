package org.example.view.components;

import org.example.dao.WeatherAPI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class WeatherPanel extends JPanel {

  private WeatherAPI weather;
  private final JLabel lblTemperature = new JLabel();
  private final JLabel lblFeelsLike = new JLabel();
  private final JLabel lblHighLow = new JLabel();

  public WeatherPanel(boolean isThemeDark) throws IOException {
    setLayout(null);

    weather = new WeatherAPI(WeatherAPI.TOCA_WEATHER);

    lblTemperature.setText("__ºC");
    lblTemperature.setForeground(isThemeDark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblTemperature.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    lblTemperature.setHorizontalAlignment(SwingConstants.LEFT);
    lblTemperature.setBounds(0, 0, 80, 30);

    lblFeelsLike.setText("Feels: __ºC");
    lblFeelsLike.setForeground(isThemeDark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblFeelsLike.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    lblFeelsLike.setHorizontalAlignment(SwingConstants.LEFT);
    lblFeelsLike.setBounds(0, 30, 80, 30);

    lblHighLow.setText("H: _ºC   L: _ºC");
    lblHighLow.setForeground(isThemeDark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblHighLow.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    lblHighLow.setHorizontalAlignment(SwingConstants.LEFT);
    lblHighLow.setBounds(0, 60, 80, 30);

    add(lblTemperature);
    add(lblFeelsLike);
    add(lblHighLow);

    updateWeather();
    Timer timer = new Timer(1000, _ -> updateWeather());
    timer.start();
  }

  public void updateWeather() {
    // A chamada ao Selenium aqui pode ser demorada, mas ela rodará em segundo plano
    new Thread(() -> {
      weather = new WeatherAPI(WeatherAPI.TOCA_WEATHER);
      // Atualizando a UI na thread correta
      SwingUtilities.invokeLater(() -> {
        if (weather != null) {
          lblTemperature.setText(weather.temp() + "ºC");
          lblFeelsLike.setText("Feels: " + weather.feelsLike() + "ºC");
          lblHighLow.setText("H: " + weather.highLow().high() + "ºC   L: " + weather.highLow().low() + "ºC");
        }
      });
    }).start();
  }

  public void setDarkMode(boolean isThemeDark) {
    setBackground(isThemeDark ? Color.BLACK : Color.WHITE);
    lblTemperature.setForeground(isThemeDark ? Color.LIGHT_GRAY : Color.DARK_GRAY);

    revalidate();
    repaint();
  }
}
