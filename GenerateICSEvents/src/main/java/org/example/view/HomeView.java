package org.example.view;

import org.example.Constants;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

    private final JPanel panelEvents = new JPanel();
    private final JButton btnAddEvent = new JButton("Add Event");

    public HomeView() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        panelEvents.setLayout(new BoxLayout(panelEvents, BoxLayout.Y_AXIS));
        panelEvents.setAlignmentY(Component.TOP_ALIGNMENT);
        panelEvents.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(panelEvents);
        add(scrollPane, BorderLayout.CENTER);
        add(btnAddEvent, BorderLayout.SOUTH);
    }

    public JPanel panelEvents() {return panelEvents;}

    public JButton btnAddEvent() {return btnAddEvent;}
}
