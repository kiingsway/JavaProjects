package org.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockLabel extends JLabel {
  private static final String TIME_FORMAT = "HH:mm:ss";
  private static final SimpleDateFormat SDF = new SimpleDateFormat(TIME_FORMAT);

  public ClockLabel() {
    Timer timer = new Timer(1000, _ -> updateClock());
    timer.start();
    updateClock();
  }

  private void updateClock() {
    setText(SDF.format(new Date(System.currentTimeMillis())));
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(super.getPreferredSize().width + 30, super.getPreferredSize().height);
  }
}
