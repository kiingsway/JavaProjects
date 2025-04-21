package org.example.view.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.Constants;
import org.example.controller.database.DBBatchAddController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;

public class DBBatchAddView extends JFrame {

  private final JTextArea txaJson = new JTextArea();
  private final GridBagConstraints gbc = new GridBagConstraints();

  private final JButton btnSave = new JButton("Send");
  private final JButton btnCancel = new JButton("Cancel");

  private final JPanel infoPanel = new JPanel(new GridBagLayout());
  private final GridBagConstraints gbcInfo = new GridBagConstraints();

  JPanel statusPanel = new JPanel(new GridBagLayout());
  GridBagConstraints gbcStatus = new GridBagConstraints();

  public DBBatchAddView() {
    setTitle("Translate Game - Database: Batch Add");
    setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
    setLocationRelativeTo(null);
    setResizable(false);
    setLayout(new GridBagLayout());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    gbc.gridy = 0;
    gbc.gridx = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;

    gbcInfo.gridy = 0;
    gbcInfo.gridx = 0;
    gbcInfo.weightx = 1.0;
    gbcInfo.weighty = 1.0;
    gbcInfo.fill = GridBagConstraints.BOTH;

    renderInputForm();
    renderStatusForm();
    add(infoPanel, gbc);
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.NONE;
    renderAction();

    setVisible(true);
  }

  private void renderInputForm() {
    txaJson.setLineWrap(true);
    txaJson.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(txaJson);
    scrollPane.setPreferredSize(new Dimension(300, 0));

    gbcInfo.anchor = GridBagConstraints.NORTHWEST;
    gbcInfo.insets = new Insets(20, 20, 0, 0);
    gbcInfo.gridheight = GridBagConstraints.REMAINDER;
    infoPanel.add(scrollPane, gbcInfo);
  }

  private void renderStatusForm() {
    statusPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

    gbcInfo.gridx++;
    gbcInfo.anchor = GridBagConstraints.NORTHEAST;
    gbcInfo.insets = new Insets(20, 20, 0, 20);
    infoPanel.add(statusPanel, gbcInfo);
  }

  private void renderAction() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    btnSave.setPreferredSize(new Dimension(100, btnSave.getPreferredSize().height));
    btnCancel.setPreferredSize(new Dimension(100, btnSave.getPreferredSize().height));

    c.gridx = 0;
    c.insets.right = 10;
    panel.add(btnSave, c);
    c.gridx++;
    c.insets.right = 0;
    panel.add(btnCancel, c);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.SOUTHEAST;
    gbc.insets = new Insets(20, 20, 20, 20);
    gbc.gridwidth = 2;
    add(panel, gbc);
  }

  public void setStatusContent(String status, JsonArray jsonArray) {
    statusPanel.removeAll();
    if (status != null) {
      JLabel lblStatus = new JLabel(status);
      gbcStatus.gridy = 0;
      gbcStatus.gridx = 0;
      statusPanel.add(lblStatus, gbcStatus);
    } else {
      renderTreeView(jsonArray);
    }
    revalidate();
    repaint();
  }

  private void renderTreeView(JsonArray jsonArray) {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

    for (JsonElement element : jsonArray) root.add(createTreeNode(element, "Item"));

    // Criando a árvore
    JTree tree = new JTree(root);
    JScrollPane scrollPane = new JScrollPane(tree);

    tree.setRootVisible(false);

    for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);

    gbcStatus.gridy = 0;
    gbcStatus.gridx = 0;
    gbcStatus.weightx = 1.0;
    gbcStatus.weighty = 1.0;
    gbcStatus.fill = GridBagConstraints.BOTH;
    statusPanel.add(scrollPane, gbcStatus);

    statusPanel.revalidate();
    statusPanel.repaint();
  }

  private MutableTreeNode createTreeNode(JsonElement element, String key) {
    if (element.isJsonPrimitive()) {
      if (element.getAsJsonPrimitive().isString())
        return new DefaultMutableTreeNode("\"" + element.getAsString() + "\"");
      return new DefaultMutableTreeNode(element.getAsString());
    } else if (element.isJsonObject()) {
      JsonObject jsonObject = element.getAsJsonObject();
      if (jsonObject.entrySet().isEmpty()) {
        return new DefaultMutableTreeNode("{}");
      }
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);
      for (var entry : jsonObject.entrySet()) {
        node.add(createTreeNode(entry.getValue(), entry.getKey()));
      }
      return node;
    } else if (element.isJsonArray()) {
      JsonArray jsonArray = element.getAsJsonArray();
      if (jsonArray.isEmpty()) {
        return new DefaultMutableTreeNode("[]");
      }
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);
      for (int i = 0; i < jsonArray.size(); i++) {
        node.add(createTreeNode(jsonArray.get(i), "[" + i + "]"));
      }
      return node;
    } else if (element.isJsonNull()) {
      return new DefaultMutableTreeNode("null");
    }
    return new DefaultMutableTreeNode(key + ": Desconhecido");
  }

  public final JTextArea txaJson() {
    return txaJson;
  }

  public final JButton btnSave() {
    return btnSave;
  }

  public final JButton btnCancel() {
    return btnCancel;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      DBBatchAddView view = new DBBatchAddView();
      new DBBatchAddController(view, null);
      view.setVisible(true);
    });
  }
}