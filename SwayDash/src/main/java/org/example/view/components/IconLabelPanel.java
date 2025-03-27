package org.example.view.components;

import org.example.Constants;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Color;

public class IconLabelPanel extends JPanel {

  private static final Font FONT_ICON = Constants.FONT_EMOJI;
  private static final Font FONT_TEXT = Constants.FONT_DEFAULT;

  private final JLabel iconLabel = new JLabel();
  private final JLabel textLabel = new JLabel();
  private final JLabel text2Label = new JLabel();

  public IconLabelPanel() {
    setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
    setOpaque(false);

    iconLabel.setFont(FONT_ICON);
    textLabel.setFont(FONT_TEXT);
    text2Label.setFont(FONT_TEXT);

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