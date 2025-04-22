package org.example.controller;

import org.example.view.AboutPage;

public class AboutController {

    public AboutController(AboutPage view, Runnable goHome) {

        view.btnBack().addActionListener(_ -> goHome.run());
  }
}
