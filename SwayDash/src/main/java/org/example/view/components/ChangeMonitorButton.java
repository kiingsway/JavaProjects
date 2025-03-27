package org.example.view.components;

import org.example.controller.HomeController;
import org.example.view.HomeView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ChangeMonitorButton extends JButton {

  private static JFrame view;

  public ChangeMonitorButton(String text, JFrame view) {
    super(text);
    ChangeMonitorButton.view = view;

    setToolTipText("Change monitor");

    handleOnClick();
  }

  private void handleOnClick() {
    int monitorListSize = getMonitorList().size();
    if (monitorListSize < 2) return;
    this.addActionListener(_ -> {
      view.dispose();
      SwingUtilities.invokeLater(() -> {
        int monitorIndex = getActualMonitor() + 1;
        if (monitorIndex >= monitorListSize) monitorIndex = 0;
        try {
          HomeView frame = new HomeView(monitorIndex);
          new HomeController(frame);
          frame.setVisible(true);
        } catch (IOException e) {
          JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      });
    });
  }

  public static List<GraphicsDevice> getMonitorList() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    return Arrays.stream(ge.getScreenDevices()).toList();
  }

  public static int getActualMonitor() {
    // Obtém o ambiente gráfico e os dispositivos de tela
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = ge.getScreenDevices();

    // Obtém a posição do JFrame
    Rectangle frameBounds = view.getBounds();

    // Verifica em qual monitor o JFrame está
    for (int i = 0; i < screens.length; i++) {
      // Obtém as bordas do monitor
      Rectangle screenBounds = screens[i].getDefaultConfiguration().getBounds();

      // Se a posição do JFrame está dentro das bordas do monitor
      if (screenBounds.contains(frameBounds)) return i;
    }
    return 0;
  }
}
