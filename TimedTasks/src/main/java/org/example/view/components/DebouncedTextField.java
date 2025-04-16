package org.example.view.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;

public class DebouncedTextField extends JTextField {

    private final Timer debounceTimer;
    private ActionListener onDebouncedTextChange;

    public DebouncedTextField(String text, int delayMillis) {
        super(text);

        debounceTimer = new Timer(delayMillis, _ -> {
            if (onDebouncedTextChange != null) onDebouncedTextChange.actionPerformed(null);
        });
        debounceTimer.setRepeats(false); // Só dispara uma vez depois da pausa

        // Adiciona o listener para reiniciar o timer
        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { restartTimer(); }
            public void removeUpdate(DocumentEvent e) { restartTimer(); }
            public void changedUpdate(DocumentEvent e) { restartTimer(); }
        });
    }

    private void restartTimer() {
        if (debounceTimer.isRunning()) debounceTimer.restart();
        else debounceTimer.start();
    }

    public void setOnDebouncedTextChange(ActionListener listener) {
        this.onDebouncedTextChange = listener;
    }
}