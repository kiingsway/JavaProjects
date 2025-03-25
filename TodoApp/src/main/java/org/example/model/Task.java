package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class Task {
  private final int id;
  private boolean completed;
  private String title;
  private String description;
  private String category;
  private String dueDate;
  private String remindDate;
  private Subtask[] subtasks;
  private boolean favorite;
  private final int originalHash;

  public Task(int id, boolean completed, String title, String description, String category, String dueDate, String remindDate, Subtask[] subtasks, boolean favorite) {
    this.id = id;
    this.completed = completed;
    this.title = title;
    this.description = description;
    this.category = category;
    this.dueDate = dueDate;
    this.remindDate = remindDate;
    this.subtasks = subtasks;
    this.favorite = favorite;
    this.originalHash = computeHash();
  }

  private int computeHash() {
    return Objects.hash(id, completed, title, description, category, dueDate, remindDate, Arrays.hashCode(subtasks), favorite);
  }

  public boolean isModified() {
    return originalHash != computeHash();
  }

  public int id() {return id;}

  public boolean isCompleted() {return completed;}

  public void setCompleted(boolean completed) {this.completed = completed;}

  public String title() {return title;}

  public void setTitle(String title) {this.title = title;}

  public String description() {return description;}

  public void setDescription(String description) {this.description = description;}

  public String category() {return category;}

  public void setCategory(String category) {this.category = category;}

  public String dueDate() {return dueDate;}

  public void setDueDate(String dueDate) {this.dueDate = dueDate;}

  public String remindDate() {return remindDate;}

  public void setRemindDate(String remindDate) {this.remindDate = remindDate;}

  public Subtask[] subtasks() {return subtasks;}

  public void setSubtasks(Subtask[] subtasks) {this.subtasks = subtasks;}

  public boolean isFavorite() {return favorite;}

  public void setFavorite(boolean favorite) {this.favorite = favorite;}

  public String getSubtaskStatus() {
    if (subtasks == null || subtasks.length == 0) return "";
    int completedCount = (int) Arrays.stream(subtasks).filter(Subtask::isCompleted).count();
    return completedCount + " de " + subtasks.length;
  }

  public String friendlyDueDate() {
    return getFriendlyDate(dueDate);
  }

  public String friendlyRemindDate() {
    return getFriendlyDate(remindDate);
  }

  private String getFriendlyDate(String dateStr) {
    if (dateStr == null || dateStr.isEmpty()) return "";

    LocalDate date = LocalDate.parse(dateStr);
    LocalDate today = LocalDate.now();

    long daysDiff = ChronoUnit.DAYS.between(today, date);

    if (daysDiff == 1) return "Amanhã";
    if (daysDiff == -1) return "Ontem";

    String format = "E, dd 'de' MMM";
    if (date.getYear() != today.getYear()) format = "E, dd 'de' MMM 'de' yyyy";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.forLanguageTag("pt-BR"));

    return date.format(formatter);
  }

  @Override
  public String toString() {
    return "Completed: " + completed + "\n" + "Title: " + title + "\n" + "Description: " + description + "\n" + "Category: " + category + "\n" + "Due Date: " + dueDate + "\n" + "Remind Date: " + remindDate + "\n" + "Subtasks: " + Arrays.toString(subtasks) + "\n" + "Favorite: " + favorite;
  }
}
