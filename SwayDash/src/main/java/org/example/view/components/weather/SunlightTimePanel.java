package org.example.view.components.weather;

import org.example.Constants;
import org.example.model.components.ThemedPanel;
import org.example.model.weather.SunlightTimeModel;

import javax.swing.*;
import java.awt.*;

public class SunlightTimePanel extends JPanel implements ThemedPanel {

  private final JLabel lblSunriseIcon = new JLabel("\uD83C\uDF04");
  private final JLabel lblSunrise = new JLabel("Sunrise");
  private final JLabel lblSunriseTime = new JLabel("-");

  private final JLabel lblSunsetIcon = new JLabel("\uD83C\uDF05");
  private final JLabel lblSunset = new JLabel("Sunset");
  private final JLabel lblSunsetTime = new JLabel("-");

  public SunlightTimePanel() {
    setLayout(new GridBagLayout());

    //renderSunlightTime();
  }

  private void renderSunlightTime() {
    GridBagConstraints gbc = new GridBagConstraints();

    lblSunriseIcon.setFont(Constants.FONT_EMOJI);
    lblSunsetIcon.setFont(Constants.FONT_EMOJI);
    lblSunrise.setFont(Constants.FONT_DEFAULT_15);
    lblSunriseTime.setFont(Constants.FONT_DEFAULT_20);
    lblSunset.setFont(Constants.FONT_DEFAULT_15);
    lblSunsetTime.setFont(Constants.FONT_DEFAULT_20);

    JLabel[] labels = new JLabel[]{lblSunriseIcon, lblSunrise, lblSunriseTime, lblSunsetIcon, lblSunset, lblSunsetTime};

    gbc.gridx = 0;
    gbc.gridy = 0;

    for (int i = 0; i < labels.length; i++) {
      JLabel label = labels[i];

      gbc.insets = new Insets(0, 0, i == 2 ? 15 : 0, 0);

      add(label, gbc);
      gbc.gridy++;
    }
  }


  public void setSunlightTime(SunlightTimeModel sunlightTime) {
    if (sunlightTime == null) return;
    lblSunriseTime.setText(sunlightTime.sunriseTime());
    lblSunsetTime.setText(sunlightTime.sunsetTime());
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    Color foreground = isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foregroundComponents = {lblSunriseIcon, lblSunsetIcon, lblSunrise, lblSunriseTime, lblSunset, lblSunsetTime};

    for (Component c : backgroundComponents) c.setBackground(background);
    for (Component c : foregroundComponents) c.setForeground(foreground);

    revalidate();
    repaint();
  }
}
