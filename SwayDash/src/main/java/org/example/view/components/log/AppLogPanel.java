package org.example.view.components.log;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import org.example.Constants;
import org.example.model.components.ThemedPanel;
import org.example.model.log.LogItem;

public class AppLogPanel extends DraggablePanel implements ThemedPanel {

  private final JButton btnCloseLog = new JButton("❌");
  private final JButton btnClearLog = new JButton("(-) Clear all");

  private final JPanel panelLog = new JPanel();
  private final JScrollPane scrollPane = new JScrollPane(panelLog);
  private final List<LogItem> items = new ArrayList<>();

  public AppLogPanel(boolean isDarkMode, Rectangle screenBounds, boolean initializeVisible) {
    super(screenBounds);
    setTheme(isDarkMode);
    setBackground(Color.GRAY);
    setLayout(null);


    renderToolbar();
    renderAppLog();

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
    btnCloseLog.addActionListener(_ -> hideLog());
    add(btnCloseLog);

    //btnClearLog.setForeground(Constants.COLOR_DARK_GRAY_65);
    btnClearLog.setFont(Constants.FONT_DEFAULT_15);
    int width = btnClearLog.getPreferredSize().width;
    btnClearLog.setBounds(400 - width - 10, 35, width + 10, 25);
    btnClearLog.addActionListener(_ -> removeAllLogs());
    add(btnClearLog);
  }

  private void renderAppLog() {
    scrollPane.setBounds(0, 40 + 25, 400, 350 - 40 - 25);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    panelLog.removeAll();
    panelLog.setAlignmentY(Component.TOP_ALIGNMENT);
    panelLog.setLayout(new BoxLayout(panelLog, BoxLayout.Y_AXIS));
    panelLog.setAlignmentY(Component.TOP_ALIGNMENT);

    for (LogItem item : items) {
      JPanel itemPanel = createLogPanel(item);
      panelLog.add(itemPanel);
    }

    add(scrollPane);
  }

  private JPanel createLogPanel(LogItem logItem) {
    JPanel itemPanel = new JPanel(new BorderLayout());
    itemPanel.setPreferredSize(new Dimension(400, 35));
    itemPanel.setBackground(logItem.levelColor());
    itemPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {openFullMessage(logItem.getFullMessage());}
    });

    // Botão de fechar
    JButton btnClose = new JButton("❌");
    btnClose.setFocusPainted(false);
    btnClose.setBorderPainted(false);
    btnClose.setBackground(logItem.levelColor());
    btnClose.setPreferredSize(new Dimension(50, 35));
    btnClose.addActionListener(_ -> removeLogPanel(itemPanel, logItem));
    itemPanel.add(btnClose, BorderLayout.WEST);

    // Texto com corte se for muito longo
    String message = logItem.toString();
    JLabel lblMessage = new JLabel(shortenText(message, 54));
    lblMessage.setToolTipText(message);
    lblMessage.setFont(Constants.FONT_BOLD_15);
    lblMessage.setVerticalAlignment(SwingConstants.CENTER);
    lblMessage.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {openFullMessage(logItem.getFullMessage());}
    });

    JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    centerPanel.setBackground(logItem.levelColor());
    centerPanel.add(lblMessage);
    itemPanel.add(centerPanel, BorderLayout.CENTER);

    return itemPanel;
  }

  private void openFullMessage(String message) {
    JOptionPane.showMessageDialog(AppLogPanel.this, message, Constants.APP_TITLE + " - ERROR", JOptionPane.ERROR_MESSAGE);
  }

  // Função para cortar o texto e adicionar "..." no final
  private String shortenText(String text, int maxLength) {
    return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
  }


  private JPanel createLogPanel1(LogItem logItem) {
    JPanel itemPanel = new JPanel();
    itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    itemPanel.setBackground(logItem.levelColor());
    itemPanel.setPreferredSize(new Dimension(400, 35));

    // Criação do botão de remoção
    JButton btnRemoveLog = new JButton("❌");
    btnRemoveLog.setFont(Constants.FONT_EMOJI_10);
    btnRemoveLog.setPreferredSize(new Dimension(50, 35));
    btnRemoveLog.setFocusPainted(false);
    btnRemoveLog.setBorderPainted(false);
    btnRemoveLog.setBackground(logItem.levelColor());
    btnRemoveLog.addActionListener(_ -> removeLogPanel(itemPanel, logItem));
    itemPanel.add(btnRemoveLog);

    // Criação do JLabel para a mensagem
    JLabel lblMessage = new JLabel(logItem.toString());
    lblMessage.setFont(Constants.FONT_BOLD_15);
    itemPanel.add(lblMessage);

    return itemPanel;
  }

  public void addLog(LogItem item) {
    items.add(item);
    updateBtnClearAll();
    renderAppLog();
  }

  private void removeLogPanel(JPanel itemPanel, LogItem logItem) {
    items.remove(logItem);
    updateBtnClearAll();

    panelLog.remove(itemPanel);  // Remove o painel de log
    panelLog.revalidate();        // Revalida o layout
    panelLog.repaint();           // Redesenha o painel
    if (panelLog.getComponents().length == 0) hideLog();
  }

  private void removeAllLogs() {
    items.clear();
    updateBtnClearAll();
    panelLog.removeAll();
    panelLog.revalidate();
    panelLog.repaint();
    hideLog();
  }

  private void updateBtnClearAll() {
    String qtd = items.isEmpty() ? "-" : items.size() + "";
    btnClearLog.setText(String.format("(%s) Clear all", qtd));
    int width = btnClearLog.getPreferredSize().width;
    int height = btnClearLog.getPreferredSize().height;
    btnClearLog.setPreferredSize(new Dimension(width+10, height));
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

  private void hideLog() {
    setVisible(false);
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
}
