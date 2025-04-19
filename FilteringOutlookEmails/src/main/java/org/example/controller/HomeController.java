package org.example.controller;

import org.example.Constants;
import org.example.model.GraphUser;
import org.example.model.interfaces.Me;
import org.example.model.interfaces.Message;
import org.example.model.interfaces.TreeItem;
import org.example.view.Home;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.event.*;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeController {

    private String token = "";
    private GraphUser user = null;
    private final Home view;

    public HomeController(Home view) {
        this.view = view;

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApp();
            }
        });
        view.menuChangeToken().addActionListener(_ -> handleChangeToken());
        view.menuAttMessages().addActionListener(_ -> loadMessages());
        view.menuExitApp().addActionListener(_ -> closeApp());

        JTree tree = view.emailTreeView().getTree();
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {onTreeClicked(tree, e);}
        });


    }

    private void onTreeClicked(JTree tree, MouseEvent e) {
        int row = tree.getClosestRowForLocation(e.getX(), e.getY());
        Object selected = tree.getLastSelectedPathComponent();
        tree.setSelectionRow(row);

        if (selected instanceof DefaultMutableTreeNode node) {
            Object userObject = node.getUserObject();
            int level = node.getLevel();

            for (AbstractButton button : List.of(view.btnCopyText(), view.btnOpenEmail(), view.btnDeleteEmail())) {
                for (ActionListener al : button.getActionListeners()) button.removeActionListener(al);
            }

            if (level == 0) {
                view.btnCopyText().setEnabled(false);
                view.btnOpenEmail().setEnabled(false);
                view.btnDeleteEmail().setEnabled(false);
                return;
            }

            if (userObject instanceof TreeItem(String label, String id)) {

                String from = label.replaceFirst("^\\(\\d+\\)\\s*", "");

                view.btnCopyText().setEnabled(true);
                view.btnDeleteEmail().setEnabled(true);

                if (level == 1) {
                    view.btnOpenEmail().setEnabled(false);

                    view.btnCopyText().addActionListener(_ -> Constants.COPY_TO_CLIPBOARD(from));
                    view.btnDeleteEmail().setText("Delete e-mails");
                    return;
                }

                Optional<Message> foundMessage = user.messages().stream().filter(m -> m.id().equals(id)).findFirst();
                foundMessage.ifPresent(message -> {
                    view.btnOpenEmail().setEnabled(true);
                    view.btnDeleteEmail().setText("Delete e-mail");

                    view.btnOpenEmail().addActionListener(_ -> Constants.OPEN_LINK(message.webLink()));
                    view.btnCopyText().addActionListener(_ -> Constants.COPY_TO_CLIPBOARD(message.from()));
                    view.btnDeleteEmail().addActionListener(_ -> deleteMessages(List.of(message)));
                });
            }

        }
    }

    private void handleChangeToken() {
        String title = Constants.APP_TITLE + " - Handle Access Token (Microsoft Graph)";
        String newToken = JOptionPane.showInputDialog(view, "Insert the access token", title, JOptionPane.QUESTION_MESSAGE);
        if (newToken.isEmpty()) return;
        token = newToken;
        loadMessages();
    }

    private void loadMessages() {
        if (token.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No Access Token. Please enter a valid access token.");
            return;
        }
        view.setUserNameText("Loading...");
        SwingUtilities.invokeLater(() -> {
            if (user == null) user = new GraphUser(token);
            else user.setToken(token);
            try {
                Me me = user.getMe();
                view.setUserNameText(me.name());
                view.setUserMailText(me.mail());

                user.getMessagesAsync(() -> handleLoadedMessages(false), () -> handleLoadedMessages(true), Constants.MAX_EMAILS_GET);

            } catch (Exception e) {
                Constants.SHOW_ERROR_DIALOG(view, e);
                view.setUserNameText("ERROR - " + e.getMessage());
                view.setUserMailText("");
            }
        });
    }

    private void handleLoadedMessages(boolean isFullyLoaded) {
        view.emailTreeView().setMessages(user.messages());

        String status = String.format("⌛ %s total messages. Loading more...", user.messages().size());
        if (isFullyLoaded) {
            status = String.format("✅ %s total messages.", user.messages().size());
            view.menuAttMessages().setEnabled(true);
        }
        view.setStatusText(status);
    }

    private void deleteMessages(List<Message> messages) {
        int response = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this message?", "Delete Messages", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            List<String> errors = new ArrayList<>();

            messages.forEach(message -> {
                try {
                    user.deleteMessage(message);
                    handleLoadedMessages(true);
                } catch (Exception ex) {
                    errors.add("Error deleting: \"" + message.subject() + "\": " + ex.getMessage());
                }
            });

            if (!errors.isEmpty()) {
                Exception error = new Exception(String.join("\n", errors));
                Constants.SHOW_ERROR_DIALOG(view, error);
            }
        }
    }

    private void closeApp() {
        String title = Constants.APP_TITLE;
        String message = "Are you sure you want to exit the application?";
        int response = JOptionPane.showConfirmDialog(view, message, title, JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) System.exit(0);
    }
}
