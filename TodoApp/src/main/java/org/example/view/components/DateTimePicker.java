package org.example.view.components;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimePicker extends JSpinner {

  private final SimpleDateFormat utcFormatter;

  public DateTimePicker(String dateFormatPattern) {
    SpinnerDateModel model = new SpinnerDateModel();
    setModel(model);

    // Define o editor com o formato desejado
    DateEditor editor = new DateEditor(this, dateFormatPattern);
    setEditor(editor);

    // Formatter para UTC (GMT 0)
    utcFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Formatter para o fuso local
    SimpleDateFormat localFormatter = new SimpleDateFormat(dateFormatPattern);
    localFormatter.setTimeZone(TimeZone.getDefault());

    removeIncrementButton();

    ((DefaultEditor) getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
  }

  private void removeIncrementButton() {
    JComponent editor = getEditor();
    if (editor instanceof DefaultEditor) {
      for (Component comp : getComponents()) if (comp instanceof JButton) remove(comp);
    }
  }

  public void setValue(String valueString) throws Exception {
    try {
      // Recebe a 'string' UTC e converte para o horário local
      Date utcDate = utcFormatter.parse(valueString);
      getModel().setValue(utcDate);
    } catch (ParseException e) {
      String msg = "Error setting value: " + valueString + ". Error offset: " + e.getErrorOffset();
      throw new Exception(msg);
    }
  }

  public String value() {
    // Retorna a data como String no formato GMT 0
    Date localDate = (Date) getModel().getValue();
    return utcFormatter.format(localDate); // Converte de local para GMT 0
  }
}
