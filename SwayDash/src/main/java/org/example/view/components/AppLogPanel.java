package org.example.view.components;

import org.example.Constants;
import org.example.model.components.ThemedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppLogPanel extends DraggablePanel implements ThemedPanel {

  private final JButton btnCloseLog = new JButton("❌");

  private boolean isDarkMode;

  public AppLogPanel(boolean isDarkMode, Rectangle screenBounds) {
    super(screenBounds);
    this.isDarkMode = isDarkMode;

    setTheme(isDarkMode);
    setBackground(Color.GRAY);

    setLayout(null);

    renderComponents();
  }

  private void renderComponents() {
    btnCloseLog.setFont(Constants.FONT_EMOJI);
    btnCloseLog.setFocusPainted(false);
    btnCloseLog.setBorderPainted(false);
    btnCloseLog.setBackground(Color.DARK_GRAY);
    btnCloseLog.setBounds(getWidth() - 10, 10, 60, 20);
    btnCloseLog.addActionListener(_ -> setVisible(false));
    add(btnCloseLog);
  }


  private void updateValues() {

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
    this.isDarkMode = isDarkMode;
    Color foreground = this.isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = this.isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY;

    Component[] backgroundComponents = {this, btnCloseLog};
    Component[] foregroundComponents = {btnCloseLog};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }
}

class DraggablePanel extends JPanel {

  private Rectangle screenBounds;
  private int mouseX, mouseY;

  public DraggablePanel(Rectangle screenBounds) {
    this.screenBounds = screenBounds;
    setOpaque(true);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
      }
    });

    addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        Rectangle screenBounds = getScreenBounds();

        int deltaX = e.getX() - mouseX;
        int deltaY = e.getY() - mouseY;

        Point location = getLocation();
        int x = location.x + deltaX;
        int y = location.y + deltaY;

        int maxX = screenBounds.width - getWidth();
        int maxY = screenBounds.height - getHeight();

        setLocation(Constants.CLAMP(x, 0, maxX), Constants.CLAMP(y, 0, maxY));
      }
    });
  }

  private Rectangle getScreenBounds() {return screenBounds;}

  public void setScreenBounds(Rectangle screenBounds) {this.screenBounds = screenBounds;}
}