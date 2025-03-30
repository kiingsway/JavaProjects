package org.example.view.components;

import org.example.Constants;
import org.example.dao.CurrencyAPI;

import javax.swing.*;
import java.awt.*;

public class CurrencyPanel extends JPanel {

  private boolean isDarkMode;

  private static final JLabel lblCADBRL = new JLabel("");
  private static final JLabel lblUSDBRL = new JLabel("USD$ 0,99 = R$ 6,99");
  private static final JLabel lblUSDCAD = new JLabel("USD$ 0,99 = CAD$ 1,99");

  CurrencyAPI currencyAPI = new CurrencyAPI();

  public CurrencyPanel(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;
    setTheme(isDarkMode);

    setLayout(null);

    lblCADBRL.setFont(Constants.FONT_DEFAULT);
    lblUSDBRL.setFont(Constants.FONT_DEFAULT);
    lblUSDCAD.setFont(Constants.FONT_DEFAULT);

    lblCADBRL.setBounds(0, 0, 350, 35);
    lblUSDBRL.setBounds(0, 35, 350, 35);
    lblUSDCAD.setBounds(0, 70, 350, 35);

    add(lblCADBRL);
    add(lblUSDBRL);
    add(lblUSDCAD);

    updateValues();
    Timer timer = new Timer(1000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    lblCADBRL.setText("CAD$ 1,00 = R$ " + String.format("%.2f", currencyAPI.CADBRL()));
    lblUSDBRL.setText("USD$ 1,00 = R$ " + String.format("%.2f", currencyAPI.USDBRL()));
    lblUSDCAD.setText("USD$ 1,00 = CAD$ " + String.format("%.2f", currencyAPI.USDCAD()));
  }

  public void setTheme(boolean isDarkMode) {
    this.isDarkMode = isDarkMode;

    Color foreground = this.isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
    Color background = this.isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foregroundComponents = {lblCADBRL, lblUSDBRL, lblUSDCAD};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }
}
