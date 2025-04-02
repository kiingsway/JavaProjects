package org.example.view.components;

import org.example.Constants;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import java.awt.Color;

public class IconLabelPanel extends JPanel {

  private final JLabel iconLabel = new JLabel();
  private final JLabel textLabel = new JLabel();
  private final JLabel text2Label = new JLabel();

  public IconLabelPanel() {
    setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
    setOpaque(false);

    iconLabel.setFont(Constants.FONT_EMOJI);
    textLabel.setFont(Constants.FONT_DEFAULT);
    text2Label.setFont(Constants.FONT_DEFAULT);

    add(iconLabel);
    add(textLabel);
    add(text2Label);
  }

  public void setTexts(String icon, String text, String text2) {
    setIcon(icon);
    setText(text);
    setText2(text2);
  }

  public void setText(String text) {
    textLabel.setText(text);
  }

  public void setText2(String text) {
    text2Label.setText("(" + text + ")");
  }

  public void setIcon(String iconText) {
    iconLabel.setText(iconText);
  }

  @Override
  public void setForeground(Color color) {
    SwingUtilities.invokeLater(() -> {
      iconLabel.setForeground(color);
      textLabel.setForeground(color);
      Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 166);
      text2Label.setForeground(newColor);
    });
  }
}