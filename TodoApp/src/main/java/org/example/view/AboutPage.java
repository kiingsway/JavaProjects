package org.example.view;

import javax.swing.*;

public class AboutPage extends JPanel {

  private final JButton btnBack = new JButton("Back");

  public AboutPage() {

    JLabel lblAbout = new JLabel("About");

    add(lblAbout);
    add(btnBack);
  }

  public JButton btnBack() {
    return btnBack;
  }
}
