package org.example.controller.components.log;

import org.example.Constants;
import org.example.model.log.LogItem;
import org.example.view.components.log.AppLogPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AppLogController {

  private final AppLogPanel view;

  private final List<LogItem> items = new ArrayList<>();

  public AppLogController(AppLogPanel view) {
    this.view = view;
    view.btnCloseLog().addActionListener(e -> hideLog());
    view.btnClearLog().addActionListener(e -> removeAllLogs());
  }

  public JPanel createLogPanel(LogItem logItem) {
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
    btnClose.addActionListener(e -> removeLogPanel(itemPanel, logItem));
    itemPanel.add(btnClose, BorderLayout.WEST);

    String message = logItem.toString();
    JLabel lblMessage = new JLabel(shortenText(message));
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

  public void addLog(LogItem item) {
    items.add(item);
    updateBtnClearAll();
    renderLogItems();
  }

  private void renderLogItems() {
    view.panelLog().removeAll();
    for (LogItem item : items) view.panelLog().add(createLogPanel(item));
  }

  private void updateBtnClearAll() {
    String qtd = items.isEmpty() ? "-" : items.size() + "";
    view.btnClearLog().setText(String.format("(%s) Clear all", qtd));
    int width = view.btnClearLog().getPreferredSize().width;
    int height = view.btnClearLog().getPreferredSize().height;
    view.btnClearLog().setPreferredSize(new Dimension(width + 10, height));
  }

  private void removeLogPanel(JPanel itemPanel, LogItem logItem) {
    items.remove(logItem);
    updateBtnClearAll();

    view.panelLog().remove(itemPanel);  // Remove o painel de log
    view.panelLog().revalidate();        // Revalida o layout
    view.panelLog().repaint();           // Redesenha o painel
    if (view.panelLog().getComponents().length == 0) hideLog();
  }

  private void removeAllLogs() {
    items.clear();
    updateBtnClearAll();
    view.panelLog().removeAll();
    view.panelLog().revalidate();
    view.panelLog().repaint();
    hideLog();
  }

  private void hideLog() {view.setVisible(false);}

  private void openFullMessage(String message) {
    JOptionPane.showMessageDialog(view, message, Constants.APP_TITLE + " - ERROR", JOptionPane.ERROR_MESSAGE);
  }

  private String shortenText(String text) {
    int maxLength = 54;
    return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
  }
}
