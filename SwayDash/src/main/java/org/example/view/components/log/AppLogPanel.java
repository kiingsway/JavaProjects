package org.example.view.components.log;

import javax.swing.*;
import java.awt.*;

import org.example.Constants;
import org.example.model.components.ThemedPanel;

public class AppLogPanel extends DraggablePanel implements ThemedPanel {

  private final JButton btnCloseLog = new JButton("❌");
  private final JButton btnClearLog = new JButton("(-) Clear all");

  private final JPanel panelLog = new JPanel();
  private final JScrollPane scrollPane = new JScrollPane(panelLog);


  public AppLogPanel(boolean isDarkMode, Rectangle screenBounds, boolean initializeVisible) {
    super(screenBounds);
    setTheme(isDarkMode);
    setBackground(Color.GRAY);
    setLayout(null);

    renderToolbar();
    renderPanelLog();

    setVisible(initializeVisible);
  }

  private void renderToolbar() {
    JButton[] buttons = {btnCloseLog, btnClearLog};

    for (JButton button : buttons) {
      button.setFocusPainted(false);
      button.setBorderPainted(false);
      button.setBackground(Color.DARK_GRAY);
    }

    btnCloseLog.setFont(Constants.FONT_EMOJI);
    btnCloseLog.setBounds(400 - 55, 10, 60, 25);
    add(btnCloseLog);

    btnClearLog.setFont(Constants.FONT_DEFAULT_15);
    int width = btnClearLog.getPreferredSize().width;
    btnClearLog.setBounds(400 - width - 10, 35, width + 10, 25);
    add(btnClearLog);
  }

  private void renderPanelLog() {
    scrollPane.setBounds(0, 40 + 25, 400, 350 - 40 - 25);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    panelLog.setAlignmentY(Component.TOP_ALIGNMENT);
    panelLog.setLayout(new BoxLayout(panelLog, BoxLayout.Y_AXIS));
    panelLog.setAlignmentY(Component.TOP_ALIGNMENT);

    add(scrollPane);
  }


  public void setScreenSize(Rectangle screenBounds) {
    setScreenBounds(screenBounds);
    setPanelLocation(screenBounds);
  }

  private void setPanelLocation(Rectangle screenBounds) {
    int width = getWidth(), height = getHeight();
    Point location = getLocation();

    int x = Math.max(location.x, 0);
    int y = Math.max(location.y, 0);
    x = Math.min(x, screenBounds.width - width);
    y = Math.min(y, screenBounds.height - height);

    setLocation(x, y);
  }


  @Override
  public void setTheme(boolean isDarkMode) {
    Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY;

    Component[] backgroundComponents = {this, btnCloseLog, btnClearLog, panelLog};
    Component[] foregroundComponents = {btnCloseLog, btnClearLog};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }

  public JButton btnClearLog() {return btnClearLog;}

  public JButton btnCloseLog() {return btnCloseLog;}

  public JPanel panelLog() {return panelLog;}

}
