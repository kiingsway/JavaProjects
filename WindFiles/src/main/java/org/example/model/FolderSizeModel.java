package org.example.model;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class FolderSizeModel {

    private int totalFolders;

    public int getTotalFolders() {
        return totalFolders;
    }

    public List<Path> listFolders(Path root, int maxDepth) {
        List<Path> folders = new ArrayList<>();
        totalFolders = 0;
        try {
            listFoldersRecursively(root, maxDepth, 0, folders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folders;
    }

    private void listFoldersRecursively(Path dir, int maxDepth, int currentDepth, List<Path> folders) {
        if (currentDepth > maxDepth) return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            folders.add(dir);
            totalFolders++;

            for (Path entry : stream) {
                if (Files.isDirectory(entry) && Files.isReadable(entry)) {
                    listFoldersRecursively(entry, maxDepth, currentDepth + 1, folders);
                }
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.out.println("Sem acesso: " + dir);
            // apenas ignora e continua
        }
    }


    public long calculateFolderSize(Path folder, int maxDepth) {
        return calculateFolderSizeRecursively(folder, maxDepth, 0);
    }

    private long calculateFolderSizeRecursively(Path dir, int maxDepth, int currentDepth) {
        if (currentDepth > maxDepth) return 0;
        long size = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry) && Files.isReadable(entry)) {
                    size += calculateFolderSizeRecursively(entry, maxDepth, currentDepth + 1);
                } else if (Files.isRegularFile(entry)) {
                    size += Files.size(entry);
                }
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.out.println("Falha ao acessar: " + dir);
        }

        return size;
    }


    // Cria a árvore de pastas com tamanhos
    public DefaultMutableTreeNode buildTree(List<Path> folders, Path root, int maxDepth) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root.toString());

        for (Path folder : folders) {
            if (folder.equals(root)) continue;

            long size = calculateFolderSize(folder, maxDepth - root.relativize(folder).getNameCount());
            String nodeLabel = folder.getFileName() + " - " + formatSize(size);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeLabel);

            // Encontra o pai certo
            Path parent = folder.getParent();
            DefaultMutableTreeNode parentNode = findParentNode(rootNode, parent);
            if (parentNode != null) {
                parentNode.add(node);
            }
        }

        return rootNode;
    }

    // Busca recursivamente o nó pai correspondente
    private DefaultMutableTreeNode findParentNode(DefaultMutableTreeNode currentNode, Path target) {
        if (currentNode.toString().equals(target.toString())) {
            return currentNode;
        }
        for (int i = 0; i < currentNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentNode.getChildAt(i);
            DefaultMutableTreeNode result = findParentNode(child, target);
            if (result != null) return result;
        }
        return null;
    }

    // Formata o tamanho legivelmente
    private String formatSize(long size) {
        if (size >= 1_073_741_824) return String.format("%.2f GB", size / 1_073_741_824.0);
        if (size >= 1_048_576) return String.format("%.2f MB", size / 1_048_576.0);
        if (size >= 1024) return String.format("%.2f KB", size / 1024.0);
        return size + " B";
    }
}
