package org.example.view;

import org.example.model.Constants;

import javax.swing.*;
import java.awt.*;

public class TaskFormPage extends JFrame {

  private final GridBagConstraints gbc = new GridBagConstraints();

  private final JCheckBox cbCompleted = new JCheckBox("Completed");
  private final JTextField txtTitle = new JTextField();
  private final JCheckBox cbFavorite = new JCheckBox("Favorite");
  private final JToggleButton btnFavorite = new JToggleButton("★");
  private final JCheckBox cbCategory = new JCheckBox("Category");
  private final JTextField txtDueDate = new JTextField();
  private final JTextField txtRemindDate = new JTextField();
  private final JTextArea txtDescription = new JTextArea();

  public TaskFormPage() {
    setTitle(Constants.APP_TITLE);
    setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    renderTaskInfo();
    renderTaskDescription();
  }

  private void renderTaskInfo() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.gridy = 0;
    panel.add(cbCompleted, c);

    c.gridx = 1;
    txtTitle.setPreferredSize(new Dimension(200, 35));
    panel.add(txtTitle, c);

    c.gridx = 2;
    panel.add(btnFavorite, c);

    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 2;

    /*for (int i = 0; i < 5; i++) {
      JPanel subTaskPanel = new JPanel(new BorderLayout(5, 5));
      JCheckBox cbSubCompleted = new JCheckBox();
      JTextField txtSubTitle = new JTextField();
      JButton btnSubMenu = new JButton("...");

      subTaskPanel.add(cbSubCompleted, BorderLayout.WEST);
      subTaskPanel.add(txtSubTitle, BorderLayout.CENTER);
      subTaskPanel.add(btnSubMenu, BorderLayout.EAST);
      panel.add(subTaskPanel, c);
      c.gridy++;
    }*/
    add(panel, gbc);
  }

  private void renderTaskDescription() {
    JPanel panel = new JPanel();
    txtDescription.setLineWrap(true);
    txtDescription.setWrapStyleWord(true);
    txtDescription.setPreferredSize(new Dimension(200, 35));
    panel.add(txtDescription);
    gbc.gridy++;
    add(panel, gbc);
  }

  public JCheckBox cbCompleted() {return cbCompleted;}

  public JTextField txtTitle() {return txtTitle;}

  public JToggleButton btnFavorite() {return btnFavorite;}

  public JTextArea txtDescription() {return txtDescription;}

}