package org.example.view;

import org.example.Constants;
import org.example.model.Hero;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

  private final Hero hero = new Hero("Quay", 1, 100, 100, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  private final Hero villain = new Hero("Test", 1, 100, 100, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0);

  private final JPanel heroPanel = new JPanel();
  private final JPanel villainPanel = new JPanel();

  public HomeView() {
    setLayout(new BorderLayout(5, 5));
    setTitle(Constants.APP_TITLE);
    setSize(Constants.APP_SIZE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    renderHeroesPanel();
    renderHeroesInfo();
  }

  private void renderHeroesPanel() {
    heroPanel.setLayout(null);
    villainPanel.setLayout(null);

    heroPanel.setPreferredSize(new Dimension(getWidth(), (getHeight() / 2) - 25));
    villainPanel.setPreferredSize(new Dimension(getWidth(), (getHeight() / 2) - 25));

    heroPanel.setBorder(BorderFactory.createTitledBorder("Hero"));
    villainPanel.setBorder(BorderFactory.createTitledBorder("Villain"));

    add(villainPanel, BorderLayout.NORTH);
    add(heroPanel, BorderLayout.SOUTH);
  }

  private void renderHeroesInfo() {
    JPanel[] heroPanels = new JPanel[]{heroPanel, villainPanel};
    Hero[] heroes = new Hero[]{hero, villain};

    for (int i = 0; i < heroPanels.length; i++) {
      JPanel panel = heroPanels[i];
      Hero hero = heroes[i];

      JLabel lblName = new JLabel(hero.getName());
      JLabel lblLevel = new JLabel("Level: " + hero.getLevel());
      JLabel lblHP = new JLabel(hero.getHPText());
      JLabel lblMP = new JLabel(hero.getMPText());

      int y = 25;
      lblName.setBounds(15, y, lblName.getPreferredSize().width, lblName.getPreferredSize().height);
      y += lblName.getPreferredSize().height;
      lblLevel.setBounds(15, y, lblLevel.getPreferredSize().width, lblLevel.getPreferredSize().height);
      y += lblLevel.getPreferredSize().height;
      lblHP.setBounds(15, y, lblHP.getPreferredSize().width, lblHP.getPreferredSize().height);
      y += lblHP.getPreferredSize().height;
      lblMP.setBounds(15, y, lblMP.getPreferredSize().width, lblMP.getPreferredSize().height);
      //y += lblMP.getPreferredSize().height;

      panel.add(lblName);
      panel.add(lblLevel);
      panel.add(lblHP);
      panel.add(lblMP);
    }
  }
}
