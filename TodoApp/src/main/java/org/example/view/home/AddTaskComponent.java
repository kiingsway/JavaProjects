package org.example.view.home;

import org.example.model.Task;
import org.example.view.components.DateTimePicker;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AddTaskComponent extends JPanel {

  private final JTextField txtTaskTitle = new JTextField();
  private final JComboBox<String> cbxCategory = new JComboBox<>();

  private final JCheckBox chkDue = new JCheckBox();
  private final JCheckBox chkRemind = new JCheckBox();

  private final DateTimePicker txtDue = new DateTimePicker("dd/MM/yyyy HH:mm:ss");
  private final DateTimePicker txtRemind = new DateTimePicker("dd/MM/yyyy HH:mm:ss");

  public AddTaskComponent(Runnable updateData) {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    add(renderTitleInput());
    add(renderInfoInput());

    registerListeners();
  }

  private JPanel renderTitleInput() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    txtTaskTitle.setPreferredSize(new Dimension(400, 30));
    panel.add(txtTaskTitle);
    return panel;
  }

  private JPanel renderInfoInput() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    String[] categories = {"(no category)"};
    cbxCategory.setModel(new DefaultComboBoxModel<>(categories));
    txtDue.setPreferredSize(new Dimension(100, 25));
    txtRemind.setPreferredSize(new Dimension(100, 25));

    panel.add(cbxCategory);
    panel.add(chkDue);
    panel.add(txtDue);
    panel.add(chkRemind);
    panel.add(txtRemind);

    return panel;
  }

  private void registerListeners() {
    chkDue.setSelected(false);
    chkRemind.setSelected(false);

    updateDueReminderStatus();

    chkDue.addActionListener(_ -> updateDueReminderStatus());
    chkRemind.addActionListener(_ -> updateDueReminderStatus());

    txtTaskTitle.addActionListener(_ -> updateDueReminderStatus());
  }

  private void updateDueReminderStatus() {
    txtDue.setEnabled(chkDue.isSelected());
    txtDue.setFocusable(chkDue.isSelected());

    txtRemind.setEnabled(chkRemind.isSelected());
    txtRemind.setFocusable(chkRemind.isSelected());
  }

  private void sendData() {
    String title = txtTaskTitle.getText().trim();
    String category = Objects.toString(cbxCategory.getSelectedItem(), "").trim();
    String due = txtDue.getValue().toString().trim();
    String remind = txtRemind.getValue().toString().trim();

    Task task = new Task(0,false, title, "", category, due, remind, null, false);
  }
}
