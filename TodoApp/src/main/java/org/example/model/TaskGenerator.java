package org.example.model;

import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskGenerator {
  private static final Random RANDOM = new Random();
  private static final String[] TITLES = {"Buy groceries", "Finish project", "Call mom", "Read a book", "Workout"};
  private static final String[] DESCRIPTIONS = {"Urgent", "Can wait", "Important", "Optional", "For later"};
  private static final String[] CATEGORIES = {"Work", "Personal", "Health", "Finance", "Learning"};
  private static final String[] SUBTASK_TITLES = {"Step 1", "Step 2", "Review", "Submit", "Follow-up"};
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static Task generateRandomTask() {
    int id = RANDOM.nextInt(100) + 1;
    boolean completed = RANDOM.nextBoolean();
    String title = TITLES[RANDOM.nextInt(TITLES.length)];
    String description = DESCRIPTIONS[RANDOM.nextInt(DESCRIPTIONS.length)];
    String category = CATEGORIES[RANDOM.nextInt(CATEGORIES.length)];
    String dueDate = LocalDate.now().plusDays(RANDOM.nextInt(30)).format(DATE_FORMAT); // Até 30 dias no futuro
    String remindDate = LocalDate.now().plusDays(RANDOM.nextInt(15)).format(DATE_FORMAT); // Até 15 dias no futuro
    boolean favorite = RANDOM.nextBoolean();

    Subtask[] subtasks = generateRandomSubtasks();

    return new Task(id, completed, title, description, category, dueDate, remindDate, subtasks, favorite);
  }

  private static Subtask[] generateRandomSubtasks() {
    int count = RANDOM.nextInt(4); // Gera até 3 subtarefas (0 a 3)
    Subtask[] subtasks = new Subtask[count];

    for (int i = 0; i < count; i++) {
      boolean subCompleted = RANDOM.nextBoolean();
      String subTitle = SUBTASK_TITLES[RANDOM.nextInt(SUBTASK_TITLES.length)];
      subtasks[i] = new Subtask(subCompleted, subTitle);
    }

    return subtasks;
  }
}
