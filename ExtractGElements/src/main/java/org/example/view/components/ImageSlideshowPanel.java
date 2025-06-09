package org.example.view.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.List;

public class ImageSlideshowPanel extends JPanel {

    private static final int PANEL_WIDTH = 400;  // <-- Edite aqui o tamanho do painel
    private static final int PANEL_HEIGHT = 300;

    private final List<String> imageUrls;
    private int currentIndex = 0;

    private final JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel indexLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton prevButton = new JButton("< Prev");
    private final JButton nextButton = new JButton("Next >");

    public ImageSlideshowPanel(List<String> imageUrls) {
        this.imageUrls = imageUrls;

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT + 40));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        imageLabel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        add(imageLabel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(prevButton);
        controlPanel.add(indexLabel);
        controlPanel.add(nextButton);
        add(controlPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(this::handlePrev);
        nextButton.addActionListener(this::handleNext);

        updateImage();
    }

    private void handlePrev(ActionEvent e) {
        if (currentIndex > 0) {
            currentIndex--;
            updateImage();
        }
    }

    private void handleNext(ActionEvent e) {
        if (currentIndex < imageUrls.size() - 1) {
            currentIndex++;
            updateImage();
        }
    }

    private void updateImage() {
        if (imageUrls.isEmpty()) {
            imageLabel.setText("No images available");
            indexLabel.setText("");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
            return;
        }

        try {
            Image img = ImageIO.read(URI.create(imageUrls.get(currentIndex)).toURL());
            if (img != null) {
                Image scaled = img.getScaledInstance(PANEL_WIDTH, PANEL_HEIGHT, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText("");
            }
        } catch (Exception ex) {
            imageLabel.setIcon(null);
            imageLabel.setText("Failed to load image");
        }

        indexLabel.setText((currentIndex + 1) + " / " + imageUrls.size());
        prevButton.setEnabled(currentIndex > 0);
        nextButton.setEnabled(currentIndex < imageUrls.size() - 1);
    }
}
