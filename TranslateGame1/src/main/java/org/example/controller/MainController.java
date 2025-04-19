package org.example.controller;

import org.example.dao.TranslateItemDAO;
import org.example.model.GameSettings;
import org.example.model.interfaces.SimpleWindowListener;
import org.example.view.MainView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

import static org.example.model.TranslateItemModel.*;
import static org.example.view.MainView.settings;
import static org.example.view.ViewConstants.*;

public class MainController {

  private final MainView view;

  public MainController(MainView view) {
    this.view = view;

    JButton btnDatabase = view.btnDatabase();
    JButton btnPlayGame = view.btnPlayGame();
    JButton btnCloseGame = view.btnCloseGame();

    btnDatabase.addActionListener(_ -> goToDatabase());
    btnPlayGame.addActionListener(_ -> goToGame());
    btnCloseGame.addActionListener(_ -> closeApp());

    updateSettingsInput();

    view.spQuestions().setValue(settings.totalQuestions());
    view.cbCategory().setSelectedItem(settings.category());
    view.cbDifficult().setSelectedItem(settings.difficult());

    view.addWindowListener((SimpleWindowListener) _ -> closeApp());
  }

  private void updateSettingsInput() {
    try {
      JComboBox<String> cbCategory = view.cbCategory();
      JComboBox<String> cbDifficult = view.cbDifficult();

      cbCategory.setModel(new DefaultComboBoxModel<>(getTranslationCategories()));
      cbDifficult.setModel(new DefaultComboBoxModel<>(getTranslationDifficulties()));
    } catch (SQLException e) {
      SHOW_ERROR_DIALOG(view, e);
    }
  }

  private void goToDatabase() {
    GO_DATABASE(view, this::updateSettingsInput);
  }

  private void goToGame() {
    try {
      List<String[]> items = TranslateItemDAO.getTranslations();

      if (!items.isEmpty()) openGame();
      else {
        String title = "ERROR - No translations items";
        String msg = "No translations items found. Create a new one to play.";

        JOptionPane.showMessageDialog(view, msg, title, JOptionPane.WARNING_MESSAGE);
        goToDatabase();
      }
    } catch (SQLException e) {
      SHOW_ERROR_DIALOG(view, e);
    }
  }

  private void openGame() {
    try {
      int totalQuestions = (int) view.spQuestions().getValue();
      String category = (String) view.cbCategory().getSelectedItem();
      String difficult = (String) view.cbDifficult().getSelectedItem();

      GameSettings settings = new GameSettings(totalQuestions, category, difficult);
      GO_PLAY_GAME(view, settings);
    } catch (Exception e) {
      SHOW_ERROR_DIALOG(view, e);
    }
  }

  private void closeApp() {
    String msg = "Tem certeza que deseja fechar o jogo?";
    String title = "Close Game";
    int response = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_OPTION);

    if (response == JOptionPane.YES_OPTION) view.dispose();
  }

}