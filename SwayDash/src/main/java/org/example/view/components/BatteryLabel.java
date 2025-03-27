package org.example.view.components;

import org.example.model.SystemInfo;

import javax.swing.*;

public class BatteryLabel extends JLabel {
  private final SystemInfo sys = new SystemInfo();

  public BatteryLabel() {
    updateValues();
    Timer timer = new Timer(2000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    setText(sys.getBatteryPercentage(true));
  }
}
