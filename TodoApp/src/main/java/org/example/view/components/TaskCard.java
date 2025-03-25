package org.example.view.components;

import org.example.model.AppWindow;
import org.example.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class TaskCard extends JPanel {

  private final Task task;
  private final GridBagConstraints gbc = new GridBagConstraints();
  private final RoundedPanel taskButtonPanel = new RoundedPanel(10);
  private final JCheckBox cbCompleted = new JCheckBox();
  private final JToggleButton btnFavorite = new JToggleButton("★");

  public TaskCard(Task task) {
    this.task = task;

    setLayout(new GridBagLayout());
    setPreferredSize(new Dimension(getPreferredSize().width, 50));
    setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    renderTaskButton();
    handleListeners();
  }

  private void renderTaskButton() {
    taskButtonPanel.setLayout(new GridBagLayout());
    taskButtonPanel.setBackground(Color.DARK_GRAY);
    taskButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    GridBagConstraints innerGbc = new GridBagConstraints();
    innerGbc.anchor = GridBagConstraints.WEST;
    innerGbc.insets = new Insets(0, 5, 0, 5);

    // Checkbox de conclusão
    cbCompleted.setSelected(task.isCompleted());
    innerGbc.gridx = 0;
    innerGbc.gridy = 0;
    innerGbc.weightx = 0;
    innerGbc.gridheight = 2;
    taskButtonPanel.add(cbCompleted, innerGbc);
    innerGbc.gridheight = 1;

    // Título da tarefa
    JLabel titleLabel = new JLabel(task.title());
    titleLabel.setForeground(Color.WHITE);
    innerGbc.gridx = 1;
    innerGbc.gridy = 0;
    innerGbc.weightx = 1;
    innerGbc.fill = GridBagConstraints.HORIZONTAL;
    taskButtonPanel.add(titleLabel, innerGbc);

    // Status abaixo do título
    String hasDescription = !task.description().isEmpty() ? "Anotação" : "";
    String[] statuses = {hasDescription, task.getSubtaskStatus(), task.friendlyDueDate(), task.friendlyRemindDate()};
    statuses = Arrays.stream(statuses).filter(s -> s != null && !s.isEmpty()).toArray(String[]::new);
    String status = String.join(" · ", statuses);

    JLabel statusLabel = new JLabel(status.replaceAll("\\.", ""));
    statusLabel.setForeground(Color.LIGHT_GRAY);
    statusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
    innerGbc.gridx = 1;
    innerGbc.gridy = 1;
    innerGbc.weightx = 1;
    taskButtonPanel.add(statusLabel, innerGbc);

    // Botão de favoritar
    btnFavorite.setSelected(task.isFavorite());
    innerGbc.gridx = 2;
    innerGbc.gridy = 0;
    innerGbc.gridheight = 2;
    innerGbc.weightx = 0;
    innerGbc.anchor = GridBagConstraints.EAST;
    taskButtonPanel.add(btnFavorite, innerGbc);

    gbc.insets = new Insets(0, 10, 0, 10);
    add(taskButtonPanel, gbc);
  }

  private void handleListeners() {
    taskButtonPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        AppWindow.OPEN_TASK_FORM(task);
      }
    });

    cbCompleted.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        evt.consume();
      }
    });
    btnFavorite.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        evt.consume();
      }
    });
  }
}
