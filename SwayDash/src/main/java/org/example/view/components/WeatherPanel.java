package org.example.view.components;

import org.example.Constants;
import org.example.dao.WeatherAPI;
import org.example.model.components.ThemedPanel;
import org.example.model.weather.HourlyForecastItem;
import org.example.model.weather.HourlyForecastModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeatherPanel extends JPanel implements ThemedPanel {

  private int cityIndex = 0;
  private final String[] cities = {WeatherAPI.TOCA_WEATHER, WeatherAPI.SPBR_WEATHER, WeatherAPI.NUGL_WEATHER};
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

  private final HForecastPanel hForecastPanel = new HForecastPanel();

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
        cityIndex++;
        if (cityIndex >= cities.length) cityIndex = 0;
        weather.setCity(cities[cityIndex]);
        updateValues();
      }
    });

    add(cityPanel);
  }

  private void renderForecastPanel() {
    hForecastPanel.setLayout(null);
    hForecastPanel.setBounds(0, 170, 400, 200);
    add(hForecastPanel);
  }

  private void updateValues() {
    new Thread(() -> SwingUtilities.invokeLater(() -> {
      if (weather != null) {
        hForecastPanel.setHourlyForecast(weather.hourlyForecast());

        JLabel[] labels = {lblCity, lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike};
        Object[] values = {weather.city(), weather.temp(), weather.highLow().high(), weather.highLow().low(), weather.status(), weather.feelsLike()};

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
    ThemedPanel[] themedComponents = {hForecastPanel};

    for (Component c : backgroundComponents) c.setBackground(background);
    for (Component c : foreground65Components) c.setForeground(foreground65);
    for (Component c : foreground80Components) c.setForeground(foreground80);
    for (ThemedPanel p : themedComponents) p.setTheme(isDarkMode);

    revalidate();
    repaint();
  }
}

class HForecastPanel extends JPanel implements ThemedPanel {

  // List<Component> backgroundComponents = new ArrayList<>(Arrays.asList(this, hourlyForecastPanel1, hourlyForecastPanel2));
//  private final List<JPanel> hfPanels;
//  private final List<JLabel> titleLabels;
//  private final List<JLabel> tempLabels;
//  private final List<JLabel> feelsLabels;
  private final JPanel[] hfPanels = new JPanel[4];
  private final JLabel[] titleLabels = new JLabel[4];
  private final JLabel[] tempLabels = new JLabel[4];
  private final JLabel[] feelsLabels = new JLabel[4];

  public HForecastPanel() {
    Arrays.setAll(hfPanels, i -> new JPanel());
    Arrays.setAll(titleLabels, i -> new JLabel());
    Arrays.setAll(tempLabels, i -> new JLabel());
    Arrays.setAll(feelsLabels, i -> new JLabel());
  }

  public static <T> List<T> initList(int size, Supplier<T> supplier) {
    return IntStream.range(0, size).mapToObj(_ -> supplier.get()).collect(Collectors.toList());
  }

  public void setHourlyForecast(HourlyForecastModel hourlyForecast) {
    if (hourlyForecast == null) return;

    for (int i = 0; i < hourlyForecast.items().size() - 1; i++) {
      HourlyForecastItem item = hourlyForecast.items().get(i);
      JPanel panel = hfPanels.get(i);
      panel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      JLabel lblTitle = titleLabels.get(i);
      lblTitle.setText(item.title());
      lblTitle.setFont(Constants.FONT_DEFAULT_20);
      lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitle.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.anchor = GridBagConstraints.NORTH;
      panel.add(lblTitle, gbc);

      JLabel lblTemp = tempLabels.get(i);
      //JLabel lblTemp = new JLabel();
      lblTemp.setText(item.temp() + "°C");
      lblTemp.setFont(Constants.FONT_DEFAULT_30);
      lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
      lblTemp.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridy++;
      panel.add(lblTemp, gbc);

      JLabel lblFeels = feelsLabels.get(i);
      lblFeels.setText("Feels: " + item.feels() + "°C");
      lblFeels.setFont(Constants.FONT_DEFAULT_15);
      lblFeels.setHorizontalAlignment(SwingConstants.CENTER);
      lblFeels.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridy++;
      panel.add(lblFeels, gbc);

      panel.setBounds(100 * i, 0, 100, this.getBounds().height);
      add(panel);
    }

    this.revalidate();
    this.repaint();
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    Color background = !isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents1 = {this};
    for (Component comp : backgroundComponents1) comp.setBackground(background);

    List<JPanel> backgroundComponents = new ArrayList<>();
    backgroundComponents.add(this);
    backgroundComponents.addAll(hfPanels);
    for (JPanel comp : backgroundComponents) comp.setBackground(background);

    List<JLabel> foregroundComponents = new ArrayList<>();
    foregroundComponents.addAll(titleLabels);
    foregroundComponents.addAll(tempLabels);
    foregroundComponents.addAll(feelsLabels);
    for (JLabel comp : foregroundComponents) comp.setForeground(background);

    revalidate();
    repaint();
  }
}