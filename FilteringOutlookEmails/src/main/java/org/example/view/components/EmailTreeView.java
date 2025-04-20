package org.example.view.components;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;

import org.example.Constants;
import org.example.model.interfaces.Message;
import org.example.model.interfaces.TreeItem;

public class EmailTreeView {
    private List<Message> messages = new ArrayList<>();
    private final DefaultMutableTreeNode rootNode;
    private final DefaultTreeModel treeModel;
    private final JTree tree;

    public EmailTreeView() {
        rootNode = new DefaultMutableTreeNode("Inbox");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {onTreeClick(e);}
        });
    }

    private void onTreeClick(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            int row = tree.getClosestRowForLocation(e.getX(), e.getY());
            tree.setSelectionRow(row);

            Object selectedNode = tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                showContextMenu(e.getComponent(), e.getX(), e.getY(), selectedNode.toString());
            }
        }
    }

    public void setMessages(List<Message> messages) {
        messages.removeIf(Objects::isNull);
        this.messages = messages;
        updateTree();
    }


    private void updateTree() {
        rootNode.removeAllChildren();

        Map<String, List<Message>> groupedMessages = new HashMap<>();
        for (Message msg : messages) {
            groupedMessages.computeIfAbsent(msg.from(), _ -> new ArrayList<>()).add(msg);
        }

        List<Map.Entry<String, List<Message>>> sortedSenders = new ArrayList<>(groupedMessages.entrySet());
        sortedSenders.sort((a, b) -> b.getValue().size() - a.getValue().size());

        for (Map.Entry<String, List<Message>> entry : sortedSenders) {
            int messageCount = entry.getValue().size();
            String senderLabel = String.format("(%d) %s", messageCount, entry.getKey());

            DefaultMutableTreeNode senderNode = new DefaultMutableTreeNode(new TreeItem(senderLabel, entry.getKey()));

            for (Message msg : entry.getValue()) {
                String formattedDate = formatDate(msg.receivedDateTime());
                String subjectLine = String.format("[%s] - %s", formattedDate, msg.subject());
                DefaultMutableTreeNode subjectNode = new DefaultMutableTreeNode(new TreeItem(subjectLine, msg.id()));

                subjectNode.add(new DefaultMutableTreeNode(new TreeItem(msg.bodyPreview(), msg.id())));
                senderNode.add(subjectNode);
            }

            rootNode.add(senderNode);
        }

        treeModel.reload();
    }

    private String formatDate(String isoDateTime) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = isoFormat.parse(isoDateTime);
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            return displayFormat.format(date);
        } catch (ParseException e) {
            return isoDateTime;
        }
    }

    private void showContextMenu(Component component, int x, int y, String selectedText) {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem openEmailItem = new JMenuItem("Open Email");
        openEmailItem.addActionListener(e -> openEmail(selectedText));

        JMenuItem copyTextItem = new JMenuItem("Copy Text");
        copyTextItem.addActionListener(e -> copyText(selectedText));

        contextMenu.add(openEmailItem);
        contextMenu.add(copyTextItem);

        contextMenu.show(component, x, y);
    }

    private void openEmail(String content) {
        JOptionPane.showMessageDialog(null, "Opening email:\n" + content);
    }

    private void copyText(String text) {
        Constants.COPY_TO_CLIPBOARD(text);
        JOptionPane.showMessageDialog(null, "Text copied to clipboard!");
    }

    public JTree getTree() {return tree;}
}
