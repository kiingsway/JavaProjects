package org.example.model;

import org.example.controller.TaskFormController;
import org.example.view.TaskFormPage;

public class AppWindow {

  static TaskFormController openedTaskForm;

  public static void OPEN_TASK_FORM(Task task) {
    if (openedTaskForm != null) {
      if (!openedTaskForm.closeWindow()) return;
    }
    TaskFormPage view = new TaskFormPage();
    openedTaskForm = new TaskFormController(view, task);
    view.setVisible(true);
  }

}
