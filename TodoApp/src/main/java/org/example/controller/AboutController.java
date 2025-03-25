package org.example.controller;

import org.example.view.AboutPage;

public class AboutController {
  private final AboutPage view;

  public AboutController(AboutPage view, Runnable goHome) {
    this.view = view;

    view.btnBack().addActionListener(_ -> goHome.run());
  }
}
