package org.example.view.components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
  private final int radius;

  public RoundedPanel(int radius) {
    this.radius = radius;
    setOpaque(false); // Permite transparência para manter os cantos arredondados
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.setColor(getBackground());
    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

    g2d.dispose();
  }
}
