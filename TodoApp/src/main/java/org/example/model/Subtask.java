package org.example.model;

public class Subtask {
  private final boolean completed;
  private final String title;

  public Subtask(boolean completed, String title) {
    this.completed = completed;
    this.title = title;
  }

  public boolean isCompleted() {
    return completed;
  }

  @Override
  public String toString() {
    return "Completed: " + completed + "\n" + "Title: " + title;
  }
}