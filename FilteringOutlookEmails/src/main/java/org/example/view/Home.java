package org.example.view;

import org.example.Constants;
import org.example.view.components.EmailTreeView;

import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {

    private final JLabel lblUserName = new JLabel("Insert token first...");
    private final JLabel lblUserEmail = new JLabel("");
    private final JLabel lblStatus = new JLabel("");

    private final JButton btnCopyText = new JButton("Copy from address");
    private final JButton btnOpenEmail = new JButton("Open in Outlook");
    private final JButton btnDeleteEmail = new JButton("Delete e-mails");

    private final JMenuItem menuChangeToken = new JMenuItem("Change Token");
    private final JMenuItem menuAttMessages = new JMenuItem("Update E-mails");
    private final JMenuItem menuExitApp = new JMenuItem("Exit");

    private final EmailTreeView emailTreeView = new EmailTreeView();

    public Home() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.APP_RESOLUTION);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        renderMenuApp();
        renderUserInfo();
        renderToolbar();
        renderMailTree();
    }

    private void renderMenuApp() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        menuAttMessages.setEnabled(false);

        fileMenu.add(menuChangeToken);
        fileMenu.add(menuAttMessages);
        fileMenu.addSeparator();
        fileMenu.add(menuExitApp);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void renderUserInfo() {
        lblUserName.setBounds(15, 15, 500, 20);
        lblUserEmail.setBounds(15, 35, 500, 20);
        lblStatus.setBounds(15, 55, 500, 20);

        lblUserName.setFont(Constants.FONT_TITLE);
        lblUserEmail.setFont(Constants.FONT_DEFAULT);
        lblStatus.setFont(Constants.FONT_DEFAULT);

        lblUserName.setForeground(Color.decode("#323232"));
        lblUserEmail.setForeground(Color.decode("#6A6A6A"));
        lblStatus.setForeground(Color.decode("#6A6A6A"));

        add(lblUserName);
        add(lblUserEmail);
        add(lblStatus);
    }

    private void renderToolbar() {
        JPanel panel = new JPanel();

        btnCopyText.setEnabled(false);
        btnOpenEmail.setEnabled(false);
        btnDeleteEmail.setEnabled(false);

        panel.add(btnCopyText);
        panel.add(btnOpenEmail);
        panel.add(btnDeleteEmail);
        panel.setBounds(15, 80, Constants.APP_RESOLUTION.width - 45, 35);
        add(panel);
    }

    private void renderMailTree() {
        JTree tree = emailTreeView.getTree();
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBounds(15, 120, Constants.APP_RESOLUTION.width - 45, 300);
        add(scrollPane);
    }

    public JMenuItem menuChangeToken() {return menuChangeToken;}

    public JMenuItem menuAttMessages() {return menuAttMessages;}

    public JMenuItem menuExitApp() {return menuExitApp;}

    public JButton btnCopyText() {return btnCopyText;}

    public JButton btnOpenEmail() {return btnOpenEmail;}

    public JButton btnDeleteEmail() {return btnDeleteEmail;}

    public EmailTreeView emailTreeView() {return emailTreeView;}

    public void setUserNameText(String value) {lblUserName.setText(value);}

    public void setUserMailText(String value) {lblUserEmail.setText(value);}

    public void setStatusText(String value) {lblStatus.setText(value);}
}
