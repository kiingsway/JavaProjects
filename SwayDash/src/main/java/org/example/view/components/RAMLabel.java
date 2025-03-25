package org.example.view.components;

import org.example.model.SystemInfo;

import javax.swing.*;

public class RAMLabel extends JLabel {
  private final SystemInfo sys;

  public RAMLabel() {
    this.sys = new SystemInfo();
    updateValues();
    Timer timer = new Timer(5000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {setText(sys.getFreeRAM());}
}
