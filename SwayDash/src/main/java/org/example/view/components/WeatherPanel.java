package org.example.view.components;

import org.example.Constants;
import org.example.dao.WeatherAPI;
import org.example.model.components.ThemedPanel;
import org.example.model.weather.HourlyForecastItem;
import org.example.model.weather.HourlyForecastModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WeatherPanel extends JPanel implements ThemedPanel {

  private boolean isDarkMode;
  private final WeatherAPI weather = new WeatherAPI(WeatherAPI.TOCA_WEATHER);
  private final JLabel lblTemperature = new JLabel();
  private final JLabel lblHigh = new JLabel();
  private final JLabel lblLow = new JLabel();
  private final JLabel lblStatus = new JLabel();
  private final JLabel lblFeelsLike = new JLabel();
  private final JLabel lblCity = new JLabel();

  private final HForecastPanel hForecastPanel;

  public WeatherPanel(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    hForecastPanel = new HForecastPanel(isDarkMode);
    setTheme(isDarkMode);
    setLayout(null);

    lblTemperature.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblTemperature.setFont(Constants.FONT_WEATHER);
    lblTemperature.setHorizontalAlignment(SwingConstants.LEFT);
    lblTemperature.setBounds(0, 0, 150, 70);

    lblHigh.setForeground(isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80);
    lblHigh.setFont(Constants.FONT_DATE);
    lblHigh.setHorizontalAlignment(SwingConstants.LEFT);
    lblHigh.setBounds(150, 5, 100, 35);

    lblLow.setForeground(isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80);
    lblLow.setFont(Constants.FONT_DATE);
    lblLow.setHorizontalAlignment(SwingConstants.LEFT);
    lblLow.setBounds(150, 32, 100, 35);

    lblStatus.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblStatus.setFont(Constants.FONT_DEFAULT_30);
    lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
    lblStatus.setBounds(0, 65, 300, 30);

    lblFeelsLike.setForeground(isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80);
    lblFeelsLike.setFont(Constants.FONT_DEFAULT_20);
    lblFeelsLike.setHorizontalAlignment(SwingConstants.LEFT);
    lblFeelsLike.setBounds(0, 100, 150, 30);

    lblCity.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblCity.setFont(Constants.FONT_DEFAULT_30);
    lblCity.setHorizontalAlignment(SwingConstants.LEFT);
    lblCity.setBounds(0, 130, 150, 30);

    hForecastPanel.setBounds(0, 170, 400, 100);

    add(lblTemperature);
    add(lblHigh);
    add(lblLow);
    add(lblStatus);
    add(lblFeelsLike);
    add(lblCity);
    add(hForecastPanel);

    updateValues();
    Timer timer = new Timer(5000, _ -> updateValues());
    timer.start();

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        try {
          Desktop desktop = Desktop.getDesktop();
          URI uri = new URI(weather.url());
          desktop.browse(uri);
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    });
  }

  private void updateValues() {
    new Thread(() -> SwingUtilities.invokeLater(() -> {
      if (weather != null) {

        hForecastPanel.setHourlyForecast(weather.hourlyForecast());

        JLabel[] labels = {lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike, lblCity};
        Object[] values = {weather.temp(), weather.highLow().high(), weather.highLow().low(), weather.status(), weather.feelsLike(), weather.city()};

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
    this.isDarkMode = isDarkMode;

    Color foreground = this.isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
    Color background = this.isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foregroundComponents = {lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike, lblCity};
    ThemedPanel[] themedComponents = {hForecastPanel};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);
    for (ThemedPanel comp : themedComponents) comp.setTheme(this.isDarkMode);

    revalidate();
    repaint();
  }
}

class HForecastPanel extends JPanel implements ThemedPanel {

  private HourlyForecastModel hourlyForecast;
  private boolean isDarkMode;

  public HForecastPanel(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    setTheme(isDarkMode);

    setLayout(null);
  }

  public void setHourlyForecast(HourlyForecastModel hourlyForecast) {
    if (hourlyForecast == null) return;
    this.hourlyForecast = hourlyForecast;

    removeAllComponents();

    for (int i = 0; i < hourlyForecast.items().size(); i++) {
      HourlyForecastItem item = hourlyForecast.items().get(i);
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      panel.setBackground(this.isDarkMode ? Color.BLACK : Color.WHITE);

      JLabel lblTitle = new JLabel(item.title());
      lblTitle.setForeground(this.isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
      lblTitle.setFont(Constants.FONT_DEFAULT_20);
      lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitle.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.anchor = GridBagConstraints.NORTH;
      panel.add(lblTitle, gbc);

      JLabel lblTemp = new JLabel(item.temp() + "°C");
      lblTemp.setForeground(this.isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
      lblTemp.setFont(Constants.FONT_DEFAULT_30);
      lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
      lblTemp.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridy++;
      panel.add(lblTemp, gbc);

      JLabel lblFeels = new JLabel("Feels: " + item.feels() + "°C");
      lblFeels.setForeground(this.isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
      lblFeels.setFont(Constants.FONT_DEFAULT_15);
      lblFeels.setHorizontalAlignment(SwingConstants.CENTER);
      lblFeels.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridy++;
      panel.add(lblFeels, gbc);

      panel.setBounds(100 * i, 0, 100, this.getBounds().height);
      add(panel);
    }
  }

  private void removeAllComponents() {
    removeAll();  // Remove todos os componentes filhos
    revalidate(); // Atualiza o layout
    repaint();    // Re-renderiza o painel
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;

    Color background = this.isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};

    for (Component comp : backgroundComponents) comp.setBackground(background);

    setHourlyForecast(hourlyForecast);

    revalidate();
    repaint();
  }
}