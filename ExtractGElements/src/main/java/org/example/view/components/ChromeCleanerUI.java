package org.example.view.components;

import org.example.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class ChromeCleanerUI extends JFrame {

    private static final String PROFILE_PATH = "/path/to/your/chrome/profile"; // Substitua pelo seu caminho real

    private final JPanel panel;
    private final JProgressBar progressBar;
    private final Map<String, Path> filesToDelete = new HashMap<>();
    private final List<JCheckBox> checkboxes = new ArrayList<>();
    private long totalDeletedSize = 0;

    public ChromeCleanerUI() {
        setTitle("Chrome Cleaner");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        add(new JScrollPane(panel), BorderLayout.CENTER);

        JButton btnClean = createBtnClean();

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        add(btnClean, BorderLayout.SOUTH);
        add(progressBar, BorderLayout.NORTH);

        loadItems();
    }

    private JButton createBtnClean() {
        JButton cleanButton = new JButton("Clean Selected Items");
        cleanButton.addActionListener(_ -> {
            String title = Constants.APP_TITLE + " - Chrome Cleaner";
            String message = "Essa ação matará todos os processos de automação do Chrome para poder inicializar a exclusão dos arquivos que serão selecionados. Deseja continuar?";
            int resp = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                killChromeDrivers();
                new CleanActionListener();
            }
        });
        return cleanButton;
    }

    private void loadItems() {
        // Tamanho de cada item a ser deletado
        filesToDelete.put("Cache", Paths.get(PROFILE_PATH, "Default", "Cache"));
        filesToDelete.put("History", Paths.get(PROFILE_PATH, "Default", "History"));
        filesToDelete.put("Cookies", Paths.get(PROFILE_PATH, "Default", "Cookies"));
        filesToDelete.put("Top Sites", Paths.get(PROFILE_PATH, "Default", "Top Sites"));
        filesToDelete.put("Visited Links", Paths.get(PROFILE_PATH, "Default", "Visited Links"));
        filesToDelete.put("Favicons", Paths.get(PROFILE_PATH, "Default", "Favicons"));
        filesToDelete.put("Media History", Paths.get(PROFILE_PATH, "Default", "Media History"));
        filesToDelete.put("Service Worker", Paths.get(PROFILE_PATH, "Default", "Service Worker"));

        // Adicionar checkboxes à interface
        for (Map.Entry<String, Path> entry : filesToDelete.entrySet()) {
            String itemName = entry.getKey();
            Path itemPath = entry.getValue();

            if (Files.exists(itemPath)) {
                long size = getSize(itemPath);
                String readableSize = formatSize(size);
                JCheckBox checkBox = new JCheckBox(itemName + " (" + readableSize + ")");
                checkboxes.add(checkBox);
                panel.add(checkBox);
            }
        }

        // Garantir que o painel seja revalidado e reexibido
        panel.revalidate();
        panel.repaint();
    }


    private String formatSize(long size) {
        if (size < 1024) return "<1 KB";

        double sizeInKB = size / 1024.0;
        if (sizeInKB < 1024) {
            return String.format("%.1f KB", sizeInKB);
        }

        double sizeInMB = sizeInKB / 1024.0;
        if (sizeInMB < 1024) {
            if (sizeInMB > 800) return String.format("%.1f MB", sizeInMB / 10);
            return String.format("%.0f MB", sizeInMB);
        }

        double sizeInGB = sizeInMB / 1024.0;
        return String.format("%.1f GB", sizeInGB);
    }

    private long getSize(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (Stream<Path> stream = Files.walk(path)) {
                    return stream.filter(Files::isRegularFile).mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            System.err.println("Failed to get size for: " + p);
                            return 0L;
                        }
                    }).sum();
                }
            } else {
                return Files.size(path);
            }
        } catch (IOException e) {
            Constants.SHOW_ERROR_DIALOG(e);
        }
        return 0;
    }


    private class CleanActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            new Thread(() -> {
                long totalSizeToDelete = 0;
                for (int i = 0; i < checkboxes.size(); i++) {
                    JCheckBox checkBox = checkboxes.get(i);
                    if (checkBox.isSelected()) {
                        String itemName = checkBox.getText().split(" ")[0];
                        Path itemPath = filesToDelete.get(itemName);

                        long itemSize = getSize(itemPath);
                        totalSizeToDelete += itemSize;

                        deleteItem(itemPath);
                    }

                    // Atualizar a barra de progresso
                    int progress = (int) ((i + 1) / (double) checkboxes.size() * 100);
                    progressBar.setValue(progress);
                }

                totalDeletedSize = totalSizeToDelete;
                JOptionPane.showMessageDialog(null,
                        "Cleaning completed! " + formatSize(totalDeletedSize) + " of space was freed.");
            }).start();
        }

        private void deleteItem(Path path) {
            try {
                if (Files.isDirectory(path)) {
                    try (Stream<Path> stream = Files.walk(path)) {
                        stream.sorted(Comparator.reverseOrder()).forEach(p -> {
                            File file = p.toFile();
                            if (!file.delete()) {
                                Constants.SHOW_ERROR_DIALOG(new Exception("Failed to delete: " + file.getAbsolutePath()));
                            }
                        });
                    }
                } else {
                    Files.deleteIfExists(path);
                }
            } catch (IOException e) {
                Constants.SHOW_ERROR_DIALOG(e);
            }
        }

    }

    public void killChromeDrivers() {
        try {
            // Para sistemas baseados em Unix (Linux/Mac)
            String os = System.getProperty("os.name").toLowerCase();
            String command;

            if (os.contains("win")) {
                // Windows
                command = "tasklist /FI \"IMAGENAME eq chromedriver.exe\"";
            } else {
                // Linux/Mac
                command = "ps aux | grep chromedriver | grep -v grep";
            }

            Process process = new ProcessBuilder(command).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                // Verifica se o processo é realmente um "chromedriver" executado pelo Java
                if (line.contains("chromedriver")) {
                    // Identificar o PID (ID do processo) para matá-lo
                    String pid = extractPID(line);
                    if (pid != null) {
                        killProcess(pid, os);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            Constants.SHOW_ERROR_DIALOG(e);
        }
    }

    /**
     * Extrai o PID (ID do processo) do comando de saída, baseado no sistema operacional.
     *
     * @param line Linha de comando de execução do processo.
     * @return PID do processo ou null caso não consiga identificar.
     */
    private String extractPID(String line) {
        String[] tokens = line.split("\\s+");
        return tokens.length > 1 ? tokens[1] : null;
    }

    /**
     * Mata o processo dado seu PID.
     *
     * @param pid ID do processo a ser morto.
     * @param os  Sistema operacional.
     */
    private void killProcess(String pid, String os) {
        try {
            ProcessBuilder builder;
            if (os.toLowerCase().contains("win")) {
                // Windows
                builder = new ProcessBuilder("taskkill", "/PID", pid, "/F"); // "/F" força a finalização
            } else {
                // Linux/Mac
                builder = new ProcessBuilder("kill", "-9", pid);
            }

            Process process = builder.start();

            int exitCode = process.waitFor(); // Espera o processo terminar
            if (exitCode == 0) {
                System.out.println("Killed process: chromedriver with PID " + pid);
            } else {
                System.err.println("Failed to kill process. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            Constants.SHOW_ERROR_DIALOG(e);
            Thread.currentThread().interrupt(); // boa prática se capturou InterruptedException
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChromeCleanerUI cleanerUI = new ChromeCleanerUI();
            cleanerUI.setVisible(true);
        });
    }
}
