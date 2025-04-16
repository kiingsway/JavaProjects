package org.example.view;

import org.example.Constants;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame {

    private final JPanel panelTasks = new JPanel();
    private final JButton btnAddTask = new JButton("Add Task");

    public AppView() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        panelTasks.setLayout(new BoxLayout(panelTasks, BoxLayout.Y_AXIS));
        panelTasks.setAlignmentY(Component.TOP_ALIGNMENT);
        panelTasks.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(panelTasks);
        add(scrollPane, BorderLayout.CENTER);
        add(btnAddTask, BorderLayout.SOUTH);
    }

    public JPanel panelTasks() {return panelTasks;}

    public JButton btnAddTask() {return btnAddTask;}
}
