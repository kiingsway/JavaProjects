import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Test extends JFrame {

  private static final String[] DAYS = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
  private final List<Task> tasks;
  private final Map<String, List<Integer>> dayTasks;
  private final Map<Integer, Color> taskColors = new HashMap<>();

  public Test(List<Task> tasks, Map<String, List<Integer>> dayTasks) {
    this.tasks = tasks;
    this.dayTasks = dayTasks;

    setTitle("Gráfico de Tarefas Semanais");
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JFreeChart stackedBarChart = ChartFactory.createStackedBarChart(
            "Tempo de Tarefas por Dia",
            "Dias da Semana",
            "Tempo (minutos)",
            createDataset()
    );

    CategoryPlot plot = stackedBarChart.getCategoryPlot();
    StackedBarRenderer renderer = new StackedBarRenderer();
    plot.setRenderer(renderer);

    // Define cores únicas para cada tarefa
    assignTaskColors(renderer);

    // Adiciona rótulos dentro das barras
    renderer.setDefaultItemLabelGenerator(new CustomLabelGenerator());
    renderer.setDefaultItemLabelsVisible(true);

    ChartPanel chartPanel = new ChartPanel(stackedBarChart);
    chartPanel.setPreferredSize(new Dimension(900, 600));
    setContentPane(chartPanel);
  }

  private DefaultCategoryDataset createDataset() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    Map<Integer, Integer> taskTimeMap = new HashMap<>();

    // Mapeia ID da tarefa para tempo
    for (Task task : tasks) {
      taskTimeMap.put(task.id(), task.time());
    }

    // Adiciona cada tarefa individualmente dentro da barra do dia
    for (String day : DAYS) {
      List<Integer> taskIds = dayTasks.get(day);
      if (taskIds != null) {
        for (int taskId : taskIds) {
          int taskTime = taskTimeMap.getOrDefault(taskId, 0);
          dataset.addValue(taskTime, getTaskNameById(taskId), day);
        }
      }
    }
    return dataset;
  }

  private void assignTaskColors(StackedBarRenderer renderer) {
    Random rand = new Random();
    for (Task task : tasks) {
      taskColors.putIfAbsent(task.id(), new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
      renderer.setSeriesPaint(tasks.indexOf(task), taskColors.get(task.id()));
    }
  }

  private String getTaskNameById(int id) {
    return tasks.stream().filter(t -> t.id() == id).findFirst().map(Task::name).orElse("Desconhecido");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

      List<Task> tasks = List.of(//
              new Task(1, "Blender", 30), //
              new Task(2, "Comida", 60), //
              new Task(3, "Comunicação", 30), //
              new Task(4, "Corrida", 30), //
              new Task(5, "Cortar unhas", 15), //
              new Task(6, "Cortar cabelo", 45), //
              new Task(7, "DJ", 30), //
              new Task(8, "Escovar e fio dental", 10), //
              new Task(9, "Barba", 20), //
              new Task(10, "Finanças", 30), //
              new Task(11, "ILAC", 30), //
              new Task(12, "Lavar cabelo", 30), //
              new Task(13, "Limpar quarto", 60), //
              new Task(14, "Livro", 30), //
              new Task(15, "Coreano", 30), //
              new Task(16, "Francês", 30), //
              new Task(17, "Música", 30), //
              new Task(18, "DEV", 30) //
      );

      Map<String, List<Integer>> dayTasks = Map.of(//
              "Domingo", List.of(2, 5, 7, 8, 10, 12,13, 14, 16, 18), //
              "Segunda", List.of(3, 4, 7, 8, 11, 14, 16, 17, 18), //
              "Terça", List.of(1, 3, 4, 7, 8, 11, 14, 16,17), //
              "Quarta", List.of(1, 3, 4, 8, 10, 11, 12, 14, 16, 18), //
              "Quinta", List.of(1, 3, 4, 8, 11, 14, 16, 17, 18), //
              "Sexta", List.of(1, 3,4, 7, 8, 11, 14, 16, 17), //
              "Sábado", List.of(2,6, 7, 8, 9, 14, 16, 17, 18) //
      );

      new Test(tasks, dayTasks).setVisible(true);
    });
  }
}

record Task(int id, String name, Integer time) {}

class CustomLabelGenerator implements CategoryItemLabelGenerator {
  @Override
  public String generateRowLabel(CategoryDataset dataset, int row) {
    return "";
  }

  @Override
  public String generateColumnLabel(CategoryDataset dataset, int column) {
    return "";
  }

  @Override
  public String generateLabel(CategoryDataset dataset, int row, int column) {
    String taskName = (String) dataset.getRowKey(row);
    Number value = dataset.getValue(row, column);
    return taskName + " (" + value.intValue() + "m)";
  }
}