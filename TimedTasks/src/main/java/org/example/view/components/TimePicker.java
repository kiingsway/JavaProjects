package org.example.view.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePicker extends JSpinner {

    private static final String timeFormatText = "HH:mm";
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatText);

    private final Timer debounceTimer;
    private ActionListener onDebouncedTimeChange;

    public TimePicker(String initialValue) {
        super(new SpinnerDateModel());
        debounceTimer = createDebounceTimer();
        TimePicker_Run(initialValue);
    }

    public TimePicker() {
        super(new SpinnerDateModel());
        debounceTimer = createDebounceTimer();
        TimePicker_Run(null);
    }

    private Timer createDebounceTimer() {
        Timer timer = new Timer(500, _ -> {
            if (onDebouncedTimeChange != null) onDebouncedTimeChange.actionPerformed(null);
        });
        timer.setRepeats(false);
        return timer;
    }

    public void TimePicker_Run(String value) {
        setEditor(new JSpinner.DateEditor(this, timeFormatText));
        removeIncrementButton();

        if (value != null) setValue(value);
        else setInitialTime();

        // Conecta debounce no campo de texto
        JFormattedTextField textField = getTextField();
        if (textField != null) {
            textField.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    restartDebounce();
                }

                public void removeUpdate(DocumentEvent e) {
                    restartDebounce();
                }

                public void changedUpdate(DocumentEvent e) {
                    restartDebounce();
                }
            });
        }
    }

    private void restartDebounce() {
        if (debounceTimer.isRunning()) debounceTimer.restart();
        else debounceTimer.start();
    }

    public void setOnDebouncedTimeChange(ActionListener listener) {
        this.onDebouncedTimeChange = listener;
    }

    private void setInitialTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date initialTime = calendar.getTime();
        setValue(initialTime);
    }

    private void removeIncrementButton() {
        JComponent editor = getEditor();
        if (editor instanceof DefaultEditor) {
            for (Component comp : getComponents()) {
                if (comp instanceof JButton) remove(comp);
            }
        }
    }

    public void setTime(String timeString) throws ParseException {
        Date utcDate = timeFormat.parse(timeString);
        setValue(utcDate);
        JFormattedTextField textField = getTextField();
        if (textField != null) textField.commitEdit();
    }

    public Date getDate() {
        return (Date) getValue();
    }

    public String getTime() {
        Date localDate = getDate();
        return timeFormat.format(localDate);
    }

    public static String sumTimes(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date1 = sdf.parse(time1);
        Date date2 = sdf.parse(time2);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar.add(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
        calendar.add(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));
        return sdf.format(calendar.getTime());
    }

    public JFormattedTextField getTextField() {
        JComponent editor = getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) editor).getTextField();
        }
        return null;
    }
}
