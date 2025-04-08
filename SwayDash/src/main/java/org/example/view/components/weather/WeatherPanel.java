package org.example.view.components.weather;

import org.example.Constants;
import org.example.dao.WeatherAPI;
import org.example.model.components.ThemedPanel;
import org.example.model.log.LogItem;
import org.example.model.log.LogItemLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class WeatherPanel extends JPanel implements ThemedPanel {

  private final Consumer<LogItem> addLog;
  private int cityIndex = 0;
  private final String[] cities = {WeatherAPI.TOCA_WEATHER, WeatherAPI.SPBR_WEATHER, WeatherAPI.NUGL_WEATHER, WeatherAPI.NORU_WEATHER};
  private final WeatherAPI weather;
  private final JLabel lblTemperature = new JLabel();
  private final JLabel lblHigh = new JLabel();
  private final JLabel lblLow = new JLabel();
  private final JLabel lblStatus = new JLabel();
  private final JLabel lblFeelsLike = new JLabel();

  private final JPanel tempRangePanel = new JPanel();
  private final JPanel statusFeelsPanel = new JPanel();

  private final ForecastPanel forecastPanel = new ForecastPanel();
  private final SunlightTimePanel sunlightTimePanel = new SunlightTimePanel();
  private final CityPanel cityPanel;

  public WeatherPanel(boolean isDarkMode, Consumer<LogItem> addLog) {
    this.addLog = addLog;
    setLayout(null);

    weather = new WeatherAPI(cities[cityIndex], addLog);
    cityPanel = new CityPanel(this::changeCity);

    setTheme(isDarkMode);

    renderTemperatureRange();
    renderStatusFeels();
    renderPanels();

    updateValues();
    Timer timer = new Timer(5000, _ -> updateValues());
    timer.start();
  }

  private void changeCity() {
    try {
      cityIndex = (cityIndex + 1) % cities.length;
      weather.setCity(cities[cityIndex], this::updateValues);
    } catch (Exception ex) {
      addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), ex));
    }
  }

  private void renderTemperatureRange() {
    lblTemperature.setFont(Constants.FONT_WEATHER);
    lblTemperature.setHorizontalAlignment(SwingConstants.LEFT);
    lblTemperature.setBounds(0, 0, 150, 70);

    lblHigh.setFont(Constants.FONT_DATE);
    lblHigh.setHorizontalAlignment(SwingConstants.LEFT);
    lblHigh.setBounds(150, 5, 100, 35);

    lblLow.setFont(Constants.FONT_DATE);
    lblLow.setHorizontalAlignment(SwingConstants.LEFT);
    lblLow.setBounds(150, 32, 100, 35);

    tempRangePanel.setLayout(null);
    tempRangePanel.setBounds(0, 0, 200, 70);
    tempRangePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    tempRangePanel.setToolTipText("Click to open The Weather Network");

    tempRangePanel.add(lblTemperature);
    tempRangePanel.add(lblHigh);
    tempRangePanel.add(lblLow);

    tempRangePanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        try {
          if (SwingUtilities.isLeftMouseButton(e)) Constants.OPEN_WEBSITE(weather.url());
        } catch (Exception ex) {
          addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), ex));
        }
      }
    });


    add(tempRangePanel);
  }

  private void renderStatusFeels() {
    statusFeelsPanel.setLayout(null);
    statusFeelsPanel.setBounds(0, 70, 200, 60);

    lblStatus.setFont(Constants.FONT_DEFAULT_30);
    lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
    lblStatus.setBounds(0, 0, 100, 30);

    lblFeelsLike.setFont(Constants.FONT_DEFAULT_20);
    lblFeelsLike.setHorizontalAlignment(SwingConstants.LEFT);
    lblFeelsLike.setBounds(0, 35, 100, 30);

    statusFeelsPanel.add(lblStatus, JLayeredPane.POPUP_LAYER);
    statusFeelsPanel.add(lblFeelsLike, JLayeredPane.POPUP_LAYER);

    add(statusFeelsPanel);
  }

  private void renderPanels() {
    cityPanel.setBounds(0, 130, 200, 30);
    add(cityPanel);

    forecastPanel.setBounds(0, 170, 400, 100);
    add(forecastPanel);

    sunlightTimePanel.setBounds(200, 0, 200, 170);
    add(sunlightTimePanel);
  }

  private void updateValues() {
    new Thread(() -> SwingUtilities.invokeLater(() -> {
      try {
        if (weather != null) {
          forecastPanel.setForecasts(weather);
          sunlightTimePanel.setSunlightTime(weather.sunlightTime());
          cityPanel.setCity(weather.city());

          JLabel[] labels = {lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike};
          Object[] values = {weather.temperature(), weather.tempRange().high(), weather.tempRange().low(), weather.status(), weather.feelsLike()};

          for (int i = 0; i < labels.length; i++) {
            JLabel label = labels[i];
            Object value = values[i];

            String val = value + "°C";
            if (value == null) val = "";
            else if (label == lblStatus) val = (String) value;
            else if (label == lblFeelsLike) val = "Feels: " + value + "°C";

            label.setText(val);

            Dimension size = label.getPreferredSize();
            label.setSize(size);

            if (label == lblTemperature) {
              lblLow.setBounds(size.width + 20, lblLow.getBounds().y, lblLow.getPreferredSize().width, lblLow.getPreferredSize().height);
              lblHigh.setBounds(size.width + 20, lblHigh.getBounds().y, lblHigh.getPreferredSize().width, lblHigh.getPreferredSize().height);
            }
          }
        }
      } catch (Exception ex) {
        addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), ex));
      }
    })).start();
  }

  public void setTheme(boolean isDarkMode) {
    try {
      Color foreground65 = isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
      Color foreground80 = isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80;
      Color background = isDarkMode ? Color.BLACK : Color.WHITE;

      Component[] backgroundComponents = {this, tempRangePanel, statusFeelsPanel};
      Component[] foreground65Components = {lblHigh, lblLow, lblFeelsLike};
      Component[] foreground80Components = {lblTemperature, lblStatus};
      ThemedPanel[] themedComponents = {forecastPanel, sunlightTimePanel, cityPanel};

      for (Component c : backgroundComponents) c.setBackground(background);
      for (Component c : foreground65Components) c.setForeground(foreground65);
      for (Component c : foreground80Components) c.setForeground(foreground80);
      for (ThemedPanel p : themedComponents) p.setTheme(isDarkMode);

      revalidate();
      repaint();
    } catch (Exception e) {
      addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), e));
    }
  }
}