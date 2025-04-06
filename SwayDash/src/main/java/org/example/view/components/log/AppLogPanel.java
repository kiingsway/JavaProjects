package org.example.view.components.log;

import java.util.Date;
import java.util.List;

import org.example.Constants;
import org.example.model.components.ThemedPanel;
import org.example.model.log.LogItem;
import org.example.model.log.LogItemLevel;
import org.openqa.selenium.devtools.v133.log.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AppLogPanel extends DraggablePanel implements ThemedPanel {

  private final JButton btnCloseLog = new JButton("❌");
  private final JPanel panelLog = new JPanel(new GridBagLayout());

  private List<LogItem> items = new ArrayList<>();

  public AppLogPanel(boolean isDarkMode, Rectangle screenBounds) {
    super(screenBounds);
    setTheme(isDarkMode);
    setBackground(Color.GRAY);
    setLayout(null);

    for (int i = 1; i < 25; i++) {
      int levelRandom = new java.util.Random().nextInt(3);
      LogItem item = new LogItem(LogItemLevel.fromInt(levelRandom), "Title #" + i, "Message #" + i, new Date());

      items.add(item);
    }

    renderToolbar();
    renderAppLog();
  }

  private void renderToolbar() {
    btnCloseLog.setFont(Constants.FONT_EMOJI);
    btnCloseLog.setFocusPainted(false);
    btnCloseLog.setBorderPainted(false);
    btnCloseLog.setBackground(Color.DARK_GRAY);
    btnCloseLog.setBounds(400 - 55, 10, 60, 25);
    btnCloseLog.addActionListener(_ -> setVisible(false));
    add(btnCloseLog);
  }

  private void renderAppLog() {
    panelLog.setBounds(0, 40, 400, 300 - 25);
    GridBagConstraints panelLogGBC = new GridBagConstraints();

    panelLog.removeAll();

    panelLogGBC.gridx = 0;
    panelLogGBC.gridy = 0;
    panelLogGBC.anchor = GridBagConstraints.NORTHWEST;
    panelLogGBC.fill = GridBagConstraints.BOTH;


    for (LogItem item : items) {
      int width = 400, height = 35;
      Dimension panelDimension = new Dimension(width, height);
      Dimension levelDimension = new Dimension((int) (width * 0.20), height);
      Dimension titleDimension = new Dimension((int) (width * 0.30), height);
      Dimension messageDimension = new Dimension((int) (width * 0.50), height);

      JPanel itemPanel = new JPanel(new GridBagLayout());
      itemPanel.setBackground(item.level().getColor());
      itemPanel.setPreferredSize(panelDimension);

      GridBagConstraints itemPanelGBC = new GridBagConstraints();

      itemPanelGBC.gridx = 0;
      itemPanelGBC.gridy = 0;
      JLabel lblLevel = new JLabel(item.level().toString());
      lblLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
      lblLevel.setFont(Constants.FONT_BOLD_15);
      lblLevel.setPreferredSize(levelDimension);
      itemPanel.add(lblLevel, itemPanelGBC);

      JLabel lblTitle = new JLabel(item.title());
      lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
      lblTitle.setFont(Constants.FONT_BOLD_15);
      lblTitle.setPreferredSize(titleDimension);
      itemPanelGBC.gridx++;
      itemPanel.add(lblTitle, itemPanelGBC);

      JLabel lblMessage = new JLabel(item.message());
      lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
      lblMessage.setFont(Constants.FONT_BOLD_15);
      lblMessage.setPreferredSize(messageDimension);
      itemPanelGBC.gridx++;
      itemPanel.add(lblMessage, itemPanelGBC);

      panelLog.add(itemPanel, panelLogGBC);
      panelLogGBC.gridy++;
    }

    add(panelLog);
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

  public void setScreenSize(Rectangle screenBounds) {
    setScreenBounds(screenBounds);
    setPanelLocation(screenBounds);
  }

  @Override
  public void setTheme(boolean isDarkMode) {
    Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY;

    Component[] backgroundComponents = {this, btnCloseLog, panelLog};
    Component[] foregroundComponents = {btnCloseLog};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }
}