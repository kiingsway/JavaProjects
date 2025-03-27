package org.example.view.components;

import org.example.Constants;

import javax.swing.*;
import java.awt.*;

public class IconLabelPanel extends JPanel {

  private static final Font FONT_ICON = Constants.FONT_EMOJI;
  private static final Font FONT_TEXT = Constants.FONT_DEFAULT;

  private final JLabel iconLabel = new JLabel();
  private final JLabel textLabel = new JLabel();

  public IconLabelPanel() {
    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    setOpaque(false); // Deixa o fundo transparente

    iconLabel.setFont(FONT_ICON);
    textLabel.setFont(FONT_TEXT);

    add(iconLabel);
    add(textLabel);
  }

  public void setIconText(String icon, String text) {
    setIcon(icon);
    setText(text);
  }

  public void setText(String text) {
    textLabel.setText(text);
  }

  public void setIcon(String iconText) {
    iconLabel.setText(iconText);
  }

  @Override
  public void setForeground(Color color) {
    System.out.println("iconLabel: " + iconLabel);
    iconLabel.setForeground(color);
    textLabel.setForeground(color);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Ícone + Texto");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 100);
    frame.setLayout(new FlowLayout());

    IconLabelPanel labelPanel = new IconLabelPanel();
    labelPanel.setIconText("✅", "Digite algo");
    frame.add(labelPanel);

    // Testando mudanças
    new Timer(2000, e -> {
      labelPanel.setIconText("❌", "Novo Texto");
    }).start();

    frame.setVisible(true);
  }
}
