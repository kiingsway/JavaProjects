package org.example.view.components.weather;

import org.example.Constants;
import org.example.model.components.ThemedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

public class CityPanel extends JPanel implements ThemedPanel {

  private final JLabel lblCity = new JLabel();
  private final Runnable onClick;

  public CityPanel(Runnable onClick) {
    this.onClick = onClick;

    setLayout(null);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    setToolTipText("Click to change the city");

    renderCity();
  }

  private void renderCity() {
    lblCity.setFont(Constants.FONT_DEFAULT_30);
    lblCity.setHorizontalAlignment(SwingConstants.LEFT);
    lblCity.setBounds(0, 0, 200, 30);
    add(lblCity);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        onClick.run();
      }
    });
  }

  public void setCity(String city) {
    lblCity.setText(Optional.ofNullable(city).orElse("-"));
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    Color foreground65 = isDarkMode ? Constants.COLOR_LIGHT_GRAY_65 : Constants.COLOR_DARK_GRAY_65;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foreground65Components = {lblCity};

    for (Component c : backgroundComponents) c.setBackground(background);
    for (Component c : foreground65Components) c.setForeground(foreground65);

    revalidate();
    repaint();
  }
}
