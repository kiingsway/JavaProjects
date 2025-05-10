package org.example.view.components;

import org.example.Constants;
import org.example.controller.GProductController;
import org.example.model.GProduct;
import org.example.view.GProductView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductListPanel extends JPanel {

    private final JPanel productContainer;
    private final List<GProduct> productList = new ArrayList<>();
    private final JTextField searchField = new JTextField(20);
    private int currentRow = 0;

    public ProductListPanel() {
        setLayout(new BorderLayout());

        // Campo de busca no topo
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        productContainer = new JPanel();
        productContainer.setLayout(new BoxLayout(productContainer, BoxLayout.Y_AXIS));

        productContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(productContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Escuta mudanças no campo de texto
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterProducts();
            }

            public void removeUpdate(DocumentEvent e) {
                filterProducts();
            }

            public void changedUpdate(DocumentEvent e) {
                filterProducts();
            }
        });
    }

    public void setProducts(List<GProduct> products) {
        productList.clear();
        productList.addAll(products);
        displayProducts(products);
    }

    public void addProducts(List<GProduct> products) {
        Map<String, GProduct> currentProductsById = productList.stream()
                .collect(Collectors.toMap(GProduct::productId, p -> p, (p1, _) -> p1));

        for (GProduct newProduct : products) {
            currentProductsById.put(newProduct.productId(), newProduct); // Substitui se já existir
        }

        productList.clear();
        productList.addAll(currentProductsById.values());
        displayProducts(productList);
    }

    public List<GProduct> getProducts() {
        return productList;
    }

    public <T extends Comparable<? super T>> void sortProducts(Function<GProduct, T> keyExtractor, boolean ascending) {
        Comparator<GProduct> comparator = Comparator.comparing(keyExtractor);
        if (!ascending) comparator = comparator.reversed();

        List<GProduct> sorted = productList.stream().sorted(comparator).collect(Collectors.toList());

        displayProducts(sorted);
    }

    /**
     * Atualiza um único produto na lista e na UI, se ele existir.
     */
    public void updateSingleProduct(GProduct updatedProduct) {
        for (int i = 0; i < productList.size(); i++) {
            GProduct current = productList.get(i);
            if (current.productId().equals(updatedProduct.productId())) {
                // Atualiza na lista de dados
                productList.set(i, updatedProduct);

                // Atualiza na UI
                Component[] components = productContainer.getComponents();
                if (i < components.length) {
                    Component comp = components[i];
                    if (comp instanceof JPanel) {
                        productContainer.remove(i);
                        productContainer.add(createProductPanel(updatedProduct), i);
                        productContainer.revalidate();
                        productContainer.repaint();

                    }
                }
                break;
            }
        }
    }

    private void displayProducts(List<GProduct> productsToDisplay) {
        productContainer.removeAll();
        currentRow = 0;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (GProduct product : productsToDisplay) {
            productContainer.add(createProductPanel(product));
        }

        addGlueRow(gbc);
        revalidate();
        repaint();
    }

    private void filterProducts() {
        String query = searchField.getText().toLowerCase();
        List<GProduct> filtered = productList.stream()
                .filter(p -> p.title().toLowerCase().contains(query))
                .collect(Collectors.toList());

        displayProducts(filtered);
    }

    private void addGlueRow(GridBagConstraints gbc) {
        gbc.weighty = 1;
        gbc.gridy = currentRow;
        productContainer.add(Box.createVerticalGlue(), gbc);
    }

    private JPanel createProductPanel(GProduct product) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        gbc.gridx = 0;
        gbc.weightx = 2;
        JLabel lblTitle = new JLabel(product.title());
        lblTitle.setPreferredSize(new Dimension(Constants.APP_WIDTH - 345, 20)); // largura fixa
        panel.add(lblTitle, gbc);

        // THC
        gbc.gridx++;
        gbc.weightx = 1;
        JLabel lblTHC = new JLabel("<html><body><small>" + product.thcPercentageMini() + "%</small></body></html>");
        lblTHC.setPreferredSize(new Dimension(35, 20));
        panel.add(lblTHC, gbc);

        // Total Size
        gbc.gridx++;
        gbc.weightx = 1;
        JLabel lblTotalSize = new JLabel(String.format("<html><body><small>%sg</small></body></html>", product.totalSize().stripTrailingZeros().toPlainString()));
        lblTotalSize.setPreferredSize(new Dimension(35, 20));
        panel.add(lblTotalSize, gbc);

        // THC Cost Benefit
        gbc.gridx++;
        gbc.weightx = 1;
        JLabel lblTHCCB = new JLabel(String.format("<html><body><small>$ %.2f/g/THC%%</small></body></html>", product.thcCostBenefit()));
        lblTHCCB.setPreferredSize(new Dimension(75, 20));
        panel.add(lblTHCCB, gbc);

        // Cost Benefit
        gbc.gridx++;
        gbc.weightx = 1;
        JLabel lblCostBenefit = new JLabel(String.format("<html><body><small>$ %.2f/g</small></body></html>", product.costBenefit()));
        lblCostBenefit.setPreferredSize(new Dimension(50, 20));
        panel.add(lblCostBenefit, gbc);

        // Price
        gbc.gridx++;
        gbc.weightx = 1;
        JLabel lblPrice = new JLabel(String.format("$ %.2f", product.price()));
        lblPrice.setPreferredSize(new Dimension(50, 20));
        panel.add(lblPrice, gbc);

        // Mouse interactions
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                GProductView view = new GProductView(product);
                new GProductController(view);
                view.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {panel.setBackground(Color.LIGHT_GRAY);}

            @Override
            public void mousePressed(MouseEvent e) {panel.setBackground(Color.GRAY);}

            @Override
            public void mouseExited(MouseEvent e) {panel.setBackground(Color.WHITE);}
        });

        return panel;
    }

    public void updateExistingProducts(List<GProduct> updatedProducts) {
        Map<String, GProduct> updatedMap = updatedProducts.stream()
                .collect(Collectors.toMap(GProduct::productId, Function.identity(), (p1, p2) -> p2));

        boolean anyUpdated = false;

        for (int i = 0; i < productList.size(); i++) {
            GProduct current = productList.get(i);
            GProduct updated = updatedMap.get(current.productId());

            if (updated != null && !updated.equals(current)) {
                productList.set(i, updated);
                anyUpdated = true;
            }
        }

        if (anyUpdated) {
            displayProducts(productList); // Atualiza UI apenas se algo mudou
        }
    }

}
