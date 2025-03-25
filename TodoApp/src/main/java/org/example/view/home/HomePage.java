package org.example.view.home;

import org.example.model.Constants;
import org.example.view.components.TaskCard;

import javax.swing.*;
import java.awt.*;

import static org.example.model.TaskGenerator.generateRandomTask;

public class HomePage extends JPanel {

  public HomePage() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel lblAllCategories = new JLabel("All categories");
    lblAllCategories.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblAllCategories.setFont(Constants.FONT_HEADER);
    add(lblAllCategories);

    JPanel cardsPanel = createCardsPanel();
    JScrollPane scrollPane = new JScrollPane(cardsPanel);

    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    add(scrollPane, BorderLayout.CENTER);

    JPanel inputPanel = new AddTaskComponent(null);
    add(inputPanel, BorderLayout.SOUTH);
  }

  private JPanel createCardsPanel() {
    JPanel cardsPanel = new JPanel();
    cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
    for (int i = 1; i <= 100; i++) cardsPanel.add(createCard());
    return cardsPanel;
  }

  private JPanel createCard() {
    return new TaskCard(generateRandomTask());
  }
}
