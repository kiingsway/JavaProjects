package org.example.view.components.log;

import java.util.Date;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import org.example.Constants;
import org.example.model.components.ThemedPanel;
import org.example.model.log.LogItem;
import org.example.model.log.LogItemLevel;

public class AppLogPanel extends DraggablePanel implements ThemedPanel {

  private final JButton btnCloseLog = new JButton("❌");
  private final JPanel panelLog = new JPanel();
  private final JScrollPane scrollPane = new JScrollPane(panelLog);
  private List<LogItem> items = new ArrayList<>();

  public AppLogPanel(boolean isDarkMode, Rectangle screenBounds) {
    super(screenBounds);
    setTheme(isDarkMode);
    setBackground(Color.GRAY);
    setLayout(null);

    // Populando com alguns logs de exemplo
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
    scrollPane.setBounds(0, 40, 400, 300 - 25); // Ajusta o tamanho do JScrollPane
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    panelLog.setLayout(new BoxLayout(panelLog, BoxLayout.Y_AXIS)); // Usando BoxLayout para uma lista simples
    panelLog.removeAll(); // Limpa a área de log

    // Adiciona os logs ao painel
    for (LogItem item : items) {
      JPanel itemPanel = createLogPanel(item);
      panelLog.add(itemPanel);
    }

    add(scrollPane); // Adiciona o JScrollPane no painel principal
  }

  private JPanel createLogPanel(LogItem item) {
    JPanel itemPanel = new JPanel();
    itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    itemPanel.setBackground(item.level().getColor());
    itemPanel.setPreferredSize(new Dimension(400, 30));

    JLabel lblMessage = new JLabel(item.message());
    lblMessage.setFont(Constants.FONT_BOLD_15);
    itemPanel.add(lblMessage);

    return itemPanel;
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
