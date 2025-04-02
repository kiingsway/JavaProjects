package org.example.view.components;

import org.example.Constants;
import org.example.model.components.ThemedPanel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockPanel extends JPanel implements ThemedPanel {

  private boolean isDarkMode;

  private static final SimpleDateFormat CLOCK_FORMAT = new SimpleDateFormat("HH:mm:ss");
  private static final SimpleDateFormat WEEK_FORMAT_FR = new SimpleDateFormat("EEEE", Locale.CANADA_FRENCH);
  private static final SimpleDateFormat WEEK_FORMAT_BR = new SimpleDateFormat("EEE", Locale.forLanguageTag("pt-BR"));
  private static final SimpleDateFormat DATE_FORMAT_FR = new SimpleDateFormat("dd MMMM yyyy", Locale.CANADA_FRENCH);

  private static final JLabel lblClock = new JLabel();
  private static final JLabel lblDate = new JLabel();

  public ClockPanel(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    setTheme(isDarkMode);

    setLayout(null);

    lblClock.setFont(Constants.FONT_CLOCK);
    lblDate.setFont(Constants.FONT_DATE);

    lblClock.setBounds(0, 0, 350, 55);
    lblDate.setBounds(0, 60, 350, 20);

    add(lblClock);
    add(lblDate);

    updateValues();
    Timer timer = new Timer(1000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    try {
      //if (error != null) setError(null);
      Date now = new Date(System.currentTimeMillis());

      String weekFR = WEEK_FORMAT_FR.format(now);
      String weekBR = WEEK_FORMAT_BR.format(now).replaceAll("\\.", "");
      String fullDate = DATE_FORMAT_FR.format(now);

      lblClock.setText(CLOCK_FORMAT.format(now));
      lblDate.setText(String.format("%s (%s), %s", weekFR, weekBR, fullDate));

      //throw new Exception("Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate) + "Teste - " + String.format("%s (%s), %s", weekFR, weekBR, fullDate));
    } catch (Exception e) {
      //setError(e);
    }
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    Color foreground = this.isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = this.isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foregroundComponents = {lblClock, lblDate};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }
}
