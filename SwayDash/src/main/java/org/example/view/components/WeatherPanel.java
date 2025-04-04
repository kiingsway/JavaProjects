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

public class WeatherPanel extends JPanel implements ThemedPanel {

  private final WeatherAPI weather = new WeatherAPI(WeatherAPI.TOCA_WEATHER);
  private final JLabel lblTemperature = new JLabel();
  private final JLabel lblHigh = new JLabel();
  private final JLabel lblLow = new JLabel();
  private final JLabel lblStatus = new JLabel();
  private final JLabel lblFeelsLike = new JLabel();
  private final JLabel lblCity = new JLabel();

  private final JPanel tempHighLowPanel = new JPanel();
  private final JPanel statusFeelsPanel = new JPanel();

  private final HForecastPanel hForecastPanel = new HForecastPanel();

  public WeatherPanel(boolean isDarkMode) {
    setLayout(null);

    renderTempHighLow();
    renderStatusFeels();

    lblCity.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblCity.setFont(Constants.FONT_DEFAULT_30);
    lblCity.setHorizontalAlignment(SwingConstants.LEFT);
    lblCity.setBounds(0, 130, 150, 30);

    hForecastPanel.setLayout(null);
    hForecastPanel.setBounds(0, 170, 400, 100);

    setTheme(isDarkMode);

    add(lblStatus);
    add(lblFeelsLike);
    add(lblCity);
    add(hForecastPanel);

    updateValues();
    Timer timer = new Timer(5000, _ -> updateValues());
    timer.start();

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        //
      }

      @Override
      public void mouseExited(MouseEvent e) {
        //
      }
    });
  }

  private void renderTempHighLow() {
    tempHighLowPanel.setLayout(null);
    tempHighLowPanel.setBounds(0, 0, 200, 70);
    tempHighLowPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    tempHighLowPanel.setToolTipText("Click to open The Weather Network");

    lblTemperature.setFont(Constants.FONT_WEATHER);
    lblTemperature.setHorizontalAlignment(SwingConstants.LEFT);
    lblTemperature.setBounds(0, 0, 150, 70);

    lblHigh.setFont(Constants.FONT_DATE);
    lblHigh.setHorizontalAlignment(SwingConstants.LEFT);
    lblHigh.setBounds(150, 5, 100, 35);

    lblLow.setFont(Constants.FONT_DATE);
    lblLow.setHorizontalAlignment(SwingConstants.LEFT);
    lblLow.setBounds(150, 32, 100, 35);

    tempHighLowPanel.add(lblTemperature);
    tempHighLowPanel.add(lblHigh);
    tempHighLowPanel.add(lblLow);

    tempHighLowPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Constants.OPEN_WEBSITE(weather.url());
      }
    });

    add(tempHighLowPanel);
  }

  private void renderStatusFeels() {
    statusFeelsPanel.setLayout(null);
    statusFeelsPanel.setBounds(0, 70, 300, 60);

    //lblStatus.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
    lblStatus.setFont(Constants.FONT_DEFAULT_30);
    lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
    lblStatus.setBounds(0, 0, 300, 30);

    //lblFeelsLike.setForeground(isDarkMode ? Constants.COLOR_LIGHT_GRAY_80 : Constants.COLOR_DARK_GRAY_80);
    lblFeelsLike.setFont(Constants.FONT_DEFAULT_20);
    lblFeelsLike.setHorizontalAlignment(SwingConstants.LEFT);
    lblFeelsLike.setBounds(0, 35, 150, 30);

    statusFeelsPanel.add(lblStatus);
    statusFeelsPanel.add(lblFeelsLike);

    add(statusFeelsPanel);
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
    Color foreground = isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this, tempHighLowPanel};
    Component[] foregroundComponents = {lblTemperature, lblHigh, lblLow, lblStatus, lblFeelsLike, lblCity};
    ThemedPanel[] themedComponents = {hForecastPanel};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);
    for (ThemedPanel comp : themedComponents) comp.setTheme(isDarkMode);

    revalidate();
    repaint();
  }
}

class HForecastPanel extends JPanel implements ThemedPanel {

  // List<Component> backgroundComponents = new ArrayList<>(Arrays.asList(this, hourlyForecastPanel1, hourlyForecastPanel2));
  private final List<JPanel> hfPanels = new ArrayList<>(Arrays.asList(new JPanel(), new JPanel(), new JPanel(), new JPanel()));
  private final List<JLabel> titleLabels = new ArrayList<>(Arrays.asList(new JLabel(), new JLabel(), new JLabel(), new JLabel()));
  private final List<JLabel> tempLabels = new ArrayList<>(Arrays.asList(new JLabel(), new JLabel(), new JLabel(), new JLabel()));
  private final List<JLabel> feelsLabels = new ArrayList<>(Arrays.asList(new JLabel(), new JLabel(), new JLabel(), new JLabel()));

  public HForecastPanel() {}

  public void setHourlyForecast(HourlyForecastModel hourlyForecast) {
    if (hourlyForecast == null) return;

    //removeAllComponents();

    for (int i = 0; i < hourlyForecast.items().size() - 1; i++) {
      HourlyForecastItem item = hourlyForecast.items().get(i);
      JPanel panel = hfPanels.get(i);
      panel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      //hfPanels.add(panel, i);
      // panel.setBackground(isDarkMode ? Color.BLACK : Color.WHITE);

      JLabel lblTitle = titleLabels.get(i);
      lblTitle.setText(item.title());
      //JLabel lblTitle = new JLabel(item.title());
      //lblTitle.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
      lblTitle.setFont(Constants.FONT_DEFAULT_20);
      lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
      lblTitle.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.anchor = GridBagConstraints.NORTH;
      panel.add(lblTitle, gbc);

      JLabel lblTemp = tempLabels.get(i);
      lblTemp.setText(item.temp() + "°C");
      //JLabel lblTemp = new JLabel(item.temp() + "°C");
      // lblTemp.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
      lblTemp.setFont(Constants.FONT_DEFAULT_30);
      lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
      lblTemp.setVerticalAlignment(SwingConstants.TOP);
      gbc.gridy++;
      panel.add(lblTemp, gbc);

      JLabel lblFeels = feelsLabels.get(i);
      lblFeels.setText("Feels: " + item.feels() + "°C");
      // JLabel lblFeels = new JLabel("Feels: " + item.feels() + "°C");
      // lblFeels.setForeground(isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY);
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
    removeAll();
    revalidate();
    repaint();
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

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