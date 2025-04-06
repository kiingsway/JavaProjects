package org.example.view.components.log;

import org.example.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DraggablePanel extends JPanel {

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