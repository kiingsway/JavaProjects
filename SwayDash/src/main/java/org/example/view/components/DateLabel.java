package org.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateLabel extends JLabel {
  private static final SimpleDateFormat WEEK_FORMAT_FR = new SimpleDateFormat("EEEE", Locale.CANADA_FRENCH);
  private static final SimpleDateFormat WEEK_FORMAT_BR = new SimpleDateFormat("EEE", Locale.forLanguageTag("pt-BR"));
  private static final SimpleDateFormat DATE_FORMAT_FR = new SimpleDateFormat("dd MMMM yyyy", Locale.CANADA_FRENCH);

  public DateLabel() {
    Timer timer = new Timer(1000, e -> updateDate());
    timer.start();

    updateDate();
  }

  private void updateDate() {
    Date now = new Date(System.currentTimeMillis());
    String weekFR = WEEK_FORMAT_FR.format(now);
    String weekBR = WEEK_FORMAT_BR.format(now).replaceAll("\\.", "");
    String fullDate = DATE_FORMAT_FR.format(now);

    setText(String.format("%s (%s), %s", weekFR, weekBR, fullDate));
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(super.getPreferredSize().width + 10, super.getPreferredSize().height);
  }
}
