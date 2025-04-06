package org.example.view.components.weather;

import org.example.Constants;
import org.example.dao.WeatherAPI;
import org.example.model.components.ThemedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WeatherPanel extends JPanel implements ThemedPanel {

  private int cityIndex = 0;
  private final String[] cities = {WeatherAPI.TOCA_WEATHER, WeatherAPI.SPBR_WEATHER, WeatherAPI.NUGL_WEATHER, WeatherAPI.NORU_WEATHER};
  private final WeatherAPI weather = new WeatherAPI(cities[cityIndex]);
  private final JLabel lblTemperature = new JLabel();
  private final JLabel lblHigh = new JLabel();
  private final JLabel lblLow = new JLabel();
  private final JLabel lblStatus = new JLabel();
  private final JLabel lblFeelsLike = new JLabel();
  private final JLabel lblCity = new JLabel();

  private final JPanel tempHighLowPanel = new JPanel();
  private final JPanel statusFeelsPanel = new JPanel();
  private final JPanel cityPanel = new JPanel();

  private final ForecastPanel forecastPanel = new ForecastPanel();

  public WeatherPanel(boolean isDarkMode) {
    setLayout(null);
    setTheme(isDarkMode);

    renderTempHighLow();
    renderStatusFeels();
    renderCity();
    renderForecastPanel();

    updateValues();
    Timer timer = new Timer(5000, _ -> updateValues());
    timer.start();
  }

  private void renderTempHighLow() {
    lblTemperature.setFont(Constants.FONT_WEATHER);
    lblTemperature.setHorizontalAlignment(SwingConstants.LEFT);
    lblTemperature.setBounds(0, 0, 150, 70);

    lblHigh.setFont(Constants.FONT_DATE);
    lblHigh.setHorizontalAlignment(SwingConstants.LEFT);
    lblHigh.setBounds(150, 5, 100, 35);

    lblLow.setFont(Constants.FONT_DATE);
    lblLow.setHorizontalAlignment(SwingConstants.LEFT);
    lblLow.setBounds(150, 32, 100, 35);

    tempHighLowPanel.setLayout(null);
    tempHighLowPanel.setBounds(0, 0, 200, 70);
    tempHighLowPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    tempHighLowPanel.setToolTipText("Click to open The Weather Network");

    tempHighLowPanel.add(lblTemperature);
    tempHighLowPanel.add(lblHigh);
    tempHighLowPanel.add(lblLow);

    tempHighLowPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) Constants.OPEN_WEBSITE(weather.url());
      }
    });


    add(tempHighLowPanel);
  }

  private void renderStatusFeels() {
    statusFeelsPanel.setLayout(null);
    statusFeelsPanel.setBounds(0, 70, 300, 60);

    lblStatus.setFont(Constants.FONT_DEFAULT_30);
    lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
    lblStatus.setBounds(0, 0, 100, 30);

    lblFeelsLike.setFont(Constants.FONT_DEFAULT_20);
    lblFeelsLike.setHorizontalAlignment(SwingConstants.LEFT);
    lblFeelsLike.setBounds(0, 35, 150, 30);

    statusFeelsPanel.add(lblStatus, JLayeredPane.POPUP_LAYER);
    statusFeelsPanel.add(lblFeelsLike, JLayeredPane.POPUP_LAYER);

    add(statusFeelsPanel);
  }

  private void renderCity() {
    lblCity.setFont(Constants.FONT_DEFAULT_30);
    lblCity.setHorizontalAlignment(SwingConstants.LEFT);
    lblCity.setBounds(0, 0, 200, 30);
    cityPanel.add(lblCity);

    cityPanel.setLayout(null);
    cityPanel.setBounds(0, 130, 200, 30);
    cityPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    cityPanel.setToolTipText("Click to change the city");

    cityPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        cityIndex = (cityIndex + 1) % cities.length;
        weather.setCity(cities[cityIndex], () -> updateValues());
      }
    });

    add(cityPanel);
  }

  private void renderForecastPanel() {
    forecastPanel.setLayout(null);
    forecastPanel.setBounds(0, 170, 400, 100);
    add(forecastPanel);
  }

  private void updateValues() {
    new Thread(() -> SwingUtilities.invokeLater(() -> {
      if (weather != null) {
        forecastPanel.setHourlyForecast(weather.hourlyForecast());
        forecastPanel.setDailyForecast(weather.dailyForecast());

        JLabel[] labels = {lblCity, lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike};
        Object[] values = {weather.city(), weather.temperature(), weather.tempRange().high(), weather.tempRange().low(), weather.status(), weather.feelsLike()};

        for (int i = 0; i < labels.length; i++) {
          JLabel label = labels[i];
          Object value = values[i];

          String val = value + "°C";
          if (label == lblStatus || label == lblCity) val = "" + value;
          else if (label == lblFeelsLike) val = "Feels: " + value + "°C";
          else if (value == null) val = "";

          label.setText(val);

          Dimension size = label.getPreferredSize();
          label.setSize(size);

          if (label == lblTemperature) {
            lblLow.setBounds(size.width + 20, lblLow.getBounds().y, lblLow.getPreferredSize().width, lblLow.getPreferredSize().height);
            lblHigh.setBounds(size.width + 20, lblHigh.getBounds().y, lblHigh.getPreferredSize().width, lblHigh.getPreferredSize().height);
          }
        }
      }
    })).start();
  }

  public void setTheme(boolean isDarkMode) {
    Color foreground65 = isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
    Color foreground80 = isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this, tempHighLowPanel, statusFeelsPanel, cityPanel};
    Component[] foreground65Components = {lblHigh, lblLow, lblCity, lblFeelsLike};
    Component[] foreground80Components = {lblTemperature, lblStatus};
    ThemedPanel[] themedComponents = {forecastPanel};

    for (Component c : backgroundComponents) c.setBackground(background);
    for (Component c : foreground65Components) c.setForeground(foreground65);
    for (Component c : foreground80Components) c.setForeground(foreground80);
    for (ThemedPanel p : themedComponents) p.setTheme(isDarkMode);

    revalidate();
    repaint();
  }
}