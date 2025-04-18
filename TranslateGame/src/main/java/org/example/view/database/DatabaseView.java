package org.example.view.database;

import org.example.Constants;

import javax.swing.*;
import java.awt.*;

public class DatabaseView extends JFrame {

    private final JButton btnCreateItem = new JButton("Create item");
    private final JButton btnBatchAdd = new JButton("Batch Addition");

    private final JMenuItem menuCreateItem = new JMenuItem("Create Translate Item");
    private final JMenuItem menuBatchAdd = new JMenuItem("Batch Addition");
    private final JMenuItem menuRestoreItem = new JMenuItem("Restore Items");

    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel itemsPanel = new JPanel();

    public DatabaseView() {
        setTitle("Translate Game - Database");
        setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        renderMenu();
        renderToolbarPanel();
        renderItemsList();
    }

    private void renderMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(menuCreateItem);
        fileMenu.add(menuBatchAdd);
        fileMenu.add(menuRestoreItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void renderToolbarPanel() {
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelButtons.setPreferredSize(new Dimension(800, 80));

        panelButtons.add(btnCreateItem);
        panelButtons.add(btnBatchAdd);

        gbc.insets = new Insets(5, 0, 10, 0);
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(panelButtons, gbc);
    }

    private void renderItemsList() {

        itemsPanel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setViewportView(itemsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        add(scrollPane, gbc);
    }

    public JButton getBtnCreateItem() {
        return this.btnCreateItem;
    }

    public JButton getBtnBatchAdd() {
        return this.btnBatchAdd;
    }

    public JPanel itemsPanel() {
        return this.itemsPanel;
    }

    public JMenuItem menuCreateItem() {
        return this.menuCreateItem;
    }

    public JMenuItem menuBatchAdd() {
        return this.menuBatchAdd;
    }

    public JMenuItem menuRestoreItem() {
        return this.menuRestoreItem;
    }
}
