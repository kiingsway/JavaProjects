package org.example.view.components;

import org.example.model.SystemInfo;

import javax.swing.*;

public class HDLabel extends JLabel {
  private final SystemInfo sys;

  public HDLabel() {
    this.sys = new SystemInfo();
    updateValues();
    Timer timer = new Timer(5000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    setText("\uD83D\uDDA5 " + sys.getHDSpace("C").free()); // Usa o mesmo objeto
  }
}
