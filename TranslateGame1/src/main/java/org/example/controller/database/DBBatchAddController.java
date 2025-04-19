package org.example.controller.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.example.model.interfaces.SimpleWindowListener;
import org.example.view.database.DBBatchAddView;

import javax.swing.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static org.example.view.ViewConstants.*;

public class DBBatchAddController {

  private final DBBatchAddView view;
  private final Runnable updateTranslateList;

  public DBBatchAddController(DBBatchAddView view, Runnable updateTranslateList) {
    this.view = view;
    this.updateTranslateList = updateTranslateList;

    handleJsonStatus();
    view.txaJson().addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        handleJsonStatus();
      }
    });
    view.btnSave().addActionListener(_ -> handleJsonText());
    view.btnCancel().addActionListener(_ -> closeApp());
    view.addWindowListener((SimpleWindowListener) _ -> closeApp());
  }

  private void handleJsonStatus() {
    String text = view.txaJson().getText();
    String status = null;
    JsonArray jsonArray = null;

    if (text == null || text.length() <= 1) {
      status = "Waiting for input...";
      view.setStatusContent(status, jsonArray);
      return;
    }

    if (text.startsWith("{") && text.endsWith("}")) {
      status = "Array is expected";
      view.setStatusContent(status, jsonArray);
      return;
    }

    try {
      jsonArray = JsonParser.parseString(text).getAsJsonArray();
    } catch (Exception e) {
      status = e.getMessage();
    }

    view.setStatusContent(status, jsonArray);

  }

  private void handleJsonText() {
    String jsonString = view.txaJson().getText();
    if (!jsonString.isEmpty()) {
      try {
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        //        int insertedCount = insertTranslationAndGetCount(jsonArray);
        int insertedCount = jsonArray.size();
        if (insertedCount == 0) {
          JOptionPane.showMessageDialog(view, "Nenhum item inserido.");
        } else {
          JOptionPane.showMessageDialog(view, insertedCount + " item(s) inserido(s) com sucesso!");
          updateTranslateList.run();
          GO_DATABASE(view, updateTranslateList);
        }
      } catch (Exception er) {
        String msg = "Error processing JSON: " + er.getMessage();
        Exception e = new Exception(msg, er);
        SHOW_ERROR_DIALOG(view, e);
      }
    } else {
      JOptionPane.showMessageDialog(view, "Por favor, insira um JSON válido.");
    }
  }

  private void closeApp() {
    if (!view.txaJson().getText().isEmpty()) {
      String msg = "Tem certeza que deseja fechar?";
      int response = JOptionPane.showConfirmDialog(view, msg, APP_NAME, JOptionPane.YES_NO_OPTION);
      if (response == JOptionPane.NO_OPTION) return;
    }
    if (updateTranslateList == null) view.dispose();
    else GO_DATABASE(view, updateTranslateList);
  }
}
