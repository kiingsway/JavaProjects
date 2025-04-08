package org.example.view.components.weather;

import org.example.Constants;
import org.example.dao.WeatherAPI;
import org.example.model.components.ThemedPanel;
import org.example.model.weather.ForecastItem;
import org.example.model.weather.ForecastModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class ForecastPanel extends JPanel implements ThemedPanel {

  private static final boolean FORECAST_HOURLY = true;
  private static final boolean FORECAST_DAILY = false;
  private final boolean selectedForecast = FORECAST_HOURLY;

  private final JPanel[] hfPanels = new JPanel[4];
  private final JLabel[] titleLabels = new JLabel[4];
  private final JLabel[] tempLabels = new JLabel[4];
  private final JLabel[] feelsLabels = new JLabel[4];

  private ForecastModel hourlyForecast;
  private ForecastModel dailyForecast;

  public ForecastPanel() {
    setLayout(null);
    Arrays.setAll(hfPanels, _ -> new JPanel());
    Arrays.setAll(titleLabels, _ -> new JLabel());
    Arrays.setAll(tempLabels, _ -> new JLabel());
    Arrays.setAll(feelsLabels, _ -> new JLabel());

    /*setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    setToolTipText(String.format("Click to change to %s forecast", selectedForecast == FORECAST_HOURLY ? "daily" : "hourly"));

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        setToolTipText(String.format("Click to change to %s forecast", selectedForecast == FORECAST_HOURLY ? "daily" : "hourly"));
        Constants.PRINT(getForecastTitle(selectedForecast) + " -> " + getForecastTitle(!selectedForecast));
        selectedForecast = !selectedForecast;
        renderForecast();
      }
    });*/
  }

  public String getForecastTitle(boolean forecast) {
    return (forecast == FORECAST_HOURLY ? "hourly" : "daily").toLowerCase();
  }

  public void setForecasts(WeatherAPI weather) {
    this.hourlyForecast = weather.hourlyForecast();
    this.dailyForecast = weather.dailyForecast();
    renderForecast();
  }

  private void renderForecast() {
    ForecastModel forecast = selectedForecast == FORECAST_HOURLY ? this.hourlyForecast : this.dailyForecast;
    removeAll();
    if (forecast == null) return;

    for (int i = 0; i < forecast.items().size() - 1; i++) {
      ForecastItem item = forecast.items().get(i);

      JPanel panel = hfPanels[i];
      panel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      JLabel lblTitle = titleLabels[i];
      lblTitle.setText(item.title());
      lblTitle.setFont(Constants.FONT_DEFAULT_20);
      lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitle.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.anchor = GridBagConstraints.NORTH;
      panel.add(lblTitle, gbc);

      JLabel lblTemp = tempLabels[i];
      lblTemp.setText(item.temp() + "°C");
      lblTemp.setFont(Constants.FONT_DEFAULT_30);
      lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
      lblTemp.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridy++;
      panel.add(lblTemp, gbc);

      JLabel lblDescription = feelsLabels[i];
      String text = item.feels() != null ? "Feels: " + item.feels() : "Night: " + item.night();
      lblDescription.setText(text + "°C");
      lblDescription.setFont(Constants.FONT_DEFAULT_15);
      lblDescription.setHorizontalAlignment(SwingConstants.CENTER);
      lblDescription.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridy++;
      panel.add(lblDescription, gbc);

      panel.setBounds(100 * i, 10, 100, this.getBounds().height);
      add(panel);
    }

    this.revalidate();
    this.repaint();
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;
    Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;

    Stream<JLabel> concat1 = Stream.concat(Arrays.stream(titleLabels), Arrays.stream(tempLabels));

    JLabel[] allLabels = Stream.concat(concat1, Arrays.stream(feelsLabels)).toArray(JLabel[]::new);

    this.setBackground(background);
    for (JPanel panel : hfPanels) panel.setBackground(background);
    for (JLabel label : allLabels) label.setForeground(foreground);

    revalidate();
    repaint();
  }

}
