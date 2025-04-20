package org.example.controller;

import org.example.Constants;
import org.example.model.GraphUser;
import org.example.model.interfaces.Me;
import org.example.model.interfaces.Message;
import org.example.model.interfaces.TreeItem;
import org.example.view.Home;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.Constants.DIALOG_TITLE;

public class HomeController {

    private final Home view;
    private final JTree tree;

    private String token = "";
    private GraphUser user = null;

    public HomeController(Home view) {
        this.view = view;
        tree = view.emailTreeView().getTree();

        view.menuChangeToken().addActionListener(_ -> handleChangeToken());
        view.menuAttMessages().addActionListener(_ -> loadMessages());
        view.menuExitApp().addActionListener(_ -> closeApp());
        view.onUserNameClick(_ -> handleChangeToken());
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApp();
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {onTreeClicked(e.getPoint());}
        });
    }

    private void handleChangeToken() {
        String title = Constants.APP_TITLE + " - Handle Access Token (Microsoft Graph)";
        String newToken = JOptionPane.showInputDialog(view, "Insert the access token", title, JOptionPane.QUESTION_MESSAGE);
        if (newToken == null || newToken.isEmpty()) return;
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
                view.setUserNameText("ERROR");
                view.setUserMailText(e.getMessage());
            }
        });
    }

    private void handleLoadedMessages(boolean isFullyLoaded) {
        view.emailTreeView().setMessages(user.messages());

        String status = String.format("⌛ %s total messages. Loading...", user.messages().size());
        if (isFullyLoaded) {
            status = String.format("✅ %s total messages", user.messages().size());
            view.menuAttMessages().setEnabled(true);
        }
        view.setStatusText(status);
    }

    private void onTreeClicked(Point p) {
        int row = tree.getClosestRowForLocation(p.x, p.y);
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

                    List<Message> senderMessages = new ArrayList<>(user.messages().stream().filter(m -> m.from().equals(from)).toList());
                    senderMessages.removeIf(Objects::isNull);

                    view.btnDeleteEmail().addActionListener(_ -> deleteMessages(senderMessages));
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

    private void deleteMessages(List<Message> messages) {
        String msg = String.format(
                "Are you sure you want to delete %s message%s?",
                messages.size() == 1 ? "this" : messages.size(),
                messages.size() == 1 ? "" : "s"
        );
        int response = JOptionPane.showConfirmDialog(view, msg, DIALOG_TITLE("Delete Messages"), JOptionPane.YES_NO_OPTION);
        if (response != JOptionPane.YES_OPTION) return;

        final int totalThreads = 4;
        final int totalMessages = messages.size();
        final List<String> errors = new ArrayList<>();

        JDialog progressDialog = new JDialog(view, "Deleting Messages", true);
        JProgressBar progressBar = new JProgressBar(0, totalMessages);
        progressBar.setStringPainted(true);
        progressDialog.add(progressBar);
        progressDialog.setSize(400, 80);
        progressDialog.setLocationRelativeTo(view);

        List<SwingWorker<Void, Integer>> workers = new ArrayList<>();

        for (int threadIndex = 0; threadIndex < totalThreads; threadIndex++) {
            final int currentThread = threadIndex;

            SwingWorker<Void, Integer> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    for (int i = 0; i < totalMessages; i++) {
                        if (i % totalThreads != currentThread) continue; // só pega as que são dessa thread
                        if (isCancelled()) break;

                        Message message = messages.get(i);
                        try {
                            user.deleteMessage(message);
                            synchronized (user.messages()) {
                                user.messages().removeIf(m -> m.id().equals(message.id()));
                            }
                            publish(1); // incrementa o progresso
                            Thread.sleep(300); // delay entre exclusões
                        } catch (Exception ex) {
                            errors.add("Error deleting: \"" + message.subject() + "\": " + ex.getMessage());
                        }
                    }
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int totalProgress = progressBar.getValue();
                    for (Integer c : chunks) totalProgress += c;
                    progressBar.setValue(totalProgress);
                }

                @Override
                protected void done() {
                    if (workers.stream().allMatch(SwingWorker::isDone)) {
                        progressDialog.dispose();
                        handleLoadedMessages(true);
                        if (!errors.isEmpty()) {
                            Exception error = new Exception(String.join("\n", errors));
                            Constants.SHOW_ERROR_DIALOG(view, error);
                        }
                    }
                }
            };

            workers.add(worker);
        }

        // Quando clicar no 'X', cancela todas as threads
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        progressDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String cancelMsg = "Do you really want to cancel the deletion?";
                int confirm = JOptionPane.showConfirmDialog(progressDialog, cancelMsg, DIALOG_TITLE("Cancel Delete Messages"), JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    workers.forEach(worker -> worker.cancel(true));
                }
            }
        });

        // Inicia todas as threads
        workers.forEach(SwingWorker::execute);
        progressDialog.setVisible(true);
    }


    private void closeApp() {
        String message = "Are you sure you want to exit the application?";
        int response = JOptionPane.showConfirmDialog(view, message, DIALOG_TITLE("Exit App"), JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) System.exit(0);
    }
}
