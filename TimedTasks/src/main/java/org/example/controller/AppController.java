package org.example.controller;

import org.example.Constants;
import org.example.model.Task;
import org.example.model.TasksList;
import org.example.view.AppView;
import org.example.view.components.DebouncedTextField;
import org.example.view.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class AppController {

    private final AppView view;
    private final TasksList tasksList;

    public AppController(AppView view) throws SQLException {
        this.view = view;

        view.btnAddTask().addActionListener(this::createTask);

        tasksList = new TasksList();
        refreshTaskPanels();
    }

    public void createTask(ActionEvent ev) {
        try {
            tasksList.add(new Task());
            refreshTaskPanels();
        } catch (SQLException e) {
            Constants.SHOW_ERROR_DIALOG(e, view);
        }
    }

    private void refreshTaskPanels() {
        view.panelTasks().removeAll();

        for (Task task : tasksList.tasks()) {
            JPanel taskPanel = createTaskPanel(task);
            view.panelTasks().add(taskPanel);
        }

        view.revalidate();
        view.repaint();
    }


    private JPanel createTaskPanel(Task task) {
        System.out.println("createTaskPanel - Task ID: " + task.id());
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS)); // layout horizontal

        DebouncedTextField  txtTitle = new DebouncedTextField(task.title(), 500);
        TimePicker txtETA = new TimePicker(task.ETATime());
        TimePicker txtStart = new TimePicker(task.startTime());
        TimePicker txtEnd = new TimePicker(task.endTime());

        JButton btnDown = createButton("\\/", _ -> moveTaskPanel(taskPanel, 1));
        JButton btnUp = createButton("/\\", _ -> moveTaskPanel(taskPanel, -1));
        JButton btnDel = createButton("X", _ -> removeTask(task));

        txtTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        txtETA.setMaximumSize(new Dimension(txtETA.getPreferredSize().width, 25));
        txtStart.setMaximumSize(new Dimension(txtStart.getPreferredSize().width, 25));
        txtEnd.setMaximumSize(new Dimension(txtEnd.getPreferredSize().width, 25));

        txtTitle.setOnDebouncedTextChange(_ -> setDebouncedValue(task, () -> task.setTitle(txtTitle.getText())));
        txtETA.setOnDebouncedTimeChange(_ -> setDebouncedValue(task, () -> task.setETATime(txtETA.getDate())));
        txtStart.setOnDebouncedTimeChange(_ -> setDebouncedValue(task, () -> task.setStartTime(txtStart.getDate())));
        txtEnd.setOnDebouncedTimeChange(_ -> setDebouncedValue(task, () -> task.setEndTime(txtEnd.getDate())));

        taskPanel.add(btnDown);
        taskPanel.add(btnUp);
        taskPanel.add(txtTitle);
        taskPanel.add(txtETA);
        taskPanel.add(txtStart);
        taskPanel.add(txtEnd);
        taskPanel.add(btnDel);

        return taskPanel;
    }

    private JButton createButton(String label, java.awt.event.ActionListener action) {
        JButton button = new JButton(label);
        button.addActionListener(action);
        return button;
    }

    private void setDebouncedValue(Task task, Runnable updateAction) {
        try {
            updateAction.run();
            tasksList.update(task); // Atualiza a lista de tarefas após a ação
        } catch (Exception ex) {
            Constants.SHOW_ERROR_DIALOG(ex, view); // Exibe erro se ocorrer
        }
    }


    private void moveTaskPanel(JPanel taskPanel, int direction) {
        JOptionPane.showMessageDialog(view, "Implementing... direction: " + direction);
    /*List<JPanel> list = view.taskPanelsList();
    int index = list.indexOf(taskPanel);
    int newIndex = index + direction;

    if (newIndex >= 0 && newIndex < list.size()) {
      list.set(index, list.set(newIndex, taskPanel));
      view.refreshTaskPanels();
      updateTimePickers(Math.min(index, newIndex));
    }*/
    }

    private void removeTask(Task task) {
        try {
            tasksList.delete(task);
            refreshTaskPanels();
        } catch (SQLException e) {
            Constants.SHOW_ERROR_DIALOG(e, view);
        }
    }
}
