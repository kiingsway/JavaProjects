package org.example.view;

import org.example.controller.FolderSizeController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class FolderSizeViewer extends JFrame {

    private final JTextField depthField;
    private final JTree folderTree;
    private final JProgressBar progressBar;

    private final FolderSizeController controller = new FolderSizeController(this);

    public FolderSizeViewer() {
        super("Calculadora de Tamanho de Pastas - C:/");

        // Configuração da ‘interface’
        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Níveis de profundidade (1-5):"));

        depthField = new JTextField("2", 5);
        topPanel.add(depthField);

        JButton loadButton = new JButton("Load Folders");
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        // Árvore de pastas
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("C:/");
        folderTree = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(folderTree);
        add(scrollPane, BorderLayout.CENTER);

        // Barra de progresso
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        // Botão de ação
        loadButton.addActionListener(_ -> {
            try {
                System.out.println("Loading Folders...");
                int depth = Integer.parseInt(depthField.getText());
                if (depth < 1 || depth > 5) {
                    JOptionPane.showMessageDialog(this, "Escolha um valor entre 1 e 5.");
                    return;
                }
                controller.loadFolders(depth);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe um número válido.");
            }
        });

        setVisible(true);
    }

    // Métodos públicos para o controller acessar

    public void resetProgress() {
        progressBar.setValue(0);
        progressBar.setIndeterminate(true);
    }

    public void setTotalProgress(int total) {
        progressBar.setMaximum(total);
        progressBar.setIndeterminate(false);
    }

    public void updateProgress(int value) {
        progressBar.setValue(value);
    }

    public void updateTree(DefaultTreeModel model) {
        folderTree.setModel(model);
    }
}
