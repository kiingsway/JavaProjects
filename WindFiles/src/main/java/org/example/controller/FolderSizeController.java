package org.example.controller;

import org.example.model.FolderSizeModel;
import org.example.view.FolderSizeViewer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FolderSizeController {

    private final FolderSizeViewer view;
    private final FolderSizeModel model;

    public FolderSizeController(FolderSizeViewer view) {
        this.view = view;
        this.model = new FolderSizeModel();
    }

    public void loadFolders(int maxDepth) {
        view.resetProgress();

        // Executa em segundo plano
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                Path rootPath = Paths.get("C:/");

                // Passo 1 — Lista todas as pastas até o nível
                List<Path> folders = model.listFolders(rootPath, maxDepth);
                int total = model.getTotalFolders();
                System.out.println("Total folders: " + total);

                if (total == 0) {
                    JOptionPane.showMessageDialog(view, "Nenhuma pasta encontrada.");
                    return null;
                }

                // Atualiza a barra de progresso
                SwingUtilities.invokeLater(() -> view.setTotalProgress(total));

                // Passo 2 — Monta a árvore com os tamanhos
                DefaultTreeModel treeModel = new DefaultTreeModel(
                        model.buildTree(folders, rootPath, maxDepth)
                );

                // Passo 3 — Atualiza a interface com a nova árvore
                SwingUtilities.invokeLater(() -> {
                    view.updateTree(treeModel);
                    view.updateProgress(total);
                });

                return null;
            }

            @Override
            protected void done() {
                // Finaliza a barra de progresso
                view.updateProgress(model.getTotalFolders());
            }
        };

        worker.execute();
    }
}
