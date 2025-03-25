package org.example.controller;

import org.example.model.Constants;
import org.example.model.Task;
import org.example.view.TaskFormPage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TaskFormController {

  private final TaskFormPage view;
  private final Task task;

  public TaskFormController(TaskFormPage view, Task task) {
    this.view = view;
    this.task = task;

    boolean isEdit = task.id() != 0;

    view.setTitle(Constants.APP_TITLE + " - Task: \"" + task.title() + "\"");
    view.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        closeWindow();
      }
    });

    if (isEdit) {
      view.cbCompleted().setSelected(task.isCompleted());
      view.txtTitle().setText(task.title());
      view.btnFavorite().setSelected(task.isFavorite());
      view.txtDescription().setText(task.description());
    }
  }

  public boolean closeWindow() {
    if (task.isModified()) {
      String tit = Constants.APP_TITLE + " - Task: \"" + task.title() + "\"";
      String msg = "Deseja salvar as alterações da tarefa \"" + task.title() + "\"?";
      int response = JOptionPane.showConfirmDialog(view, msg, tit, JOptionPane.YES_NO_OPTION);
      if (response == JOptionPane.NO_OPTION) return false;
    }
    view.dispose();
    return true;
  }
}
