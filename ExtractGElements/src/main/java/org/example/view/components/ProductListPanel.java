package org.example.view.components;

import org.example.Constants;
import org.example.controller.GProductController;
import org.example.model.GProduct;
import org.example.view.GProductView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductListPanel extends JPanel {

    private final List<GProduct> productList = new ArrayList<>();
    private int currentRow = 0;

    private final JTextField searchField = new JTextField(20);
    private final JLabel lblStatus = new JLabel(" ");
    private final JPanel productContainer = new JPanel();

    public ProductListPanel() {
        setLayout(new BorderLayout());

        // Painel superior com barra de ferramentas
        JPanel toolbarPanel = new JPanel(new BorderLayout());

        // Painel da esquerda (campo de busca)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(new JLabel("Search:"));
        leftPanel.add(searchField);

        // Painel da direita (status)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(lblStatus);

        // Adiciona os dois lados ao toolbarPanel
        toolbarPanel.add(leftPanel, BorderLayout.WEST);
        toolbarPanel.add(rightPanel, BorderLayout.EAST);

        add(toolbarPanel, BorderLayout.NORTH);

        // Container de produtos
        productContainer.setLayout(new BoxLayout(productContainer, BoxLayout.Y_AXIS));
        productContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(productContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Listener de busca
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {filterProducts();}

            public void removeUpdate(DocumentEvent e) {filterProducts();}

            public void changedUpdate(DocumentEvent e) {filterProducts();}
        });
    }

    public void setProducts(List<GProduct> products) {
        productList.clear();
        productList.addAll(products);
        displayProducts(products);
    }

    public void addProducts(List<GProduct> products) {
        Map<String, GProduct> newProductsById = products.stream()
                .collect(Collectors.toMap(GProduct::productId, p -> p));

        // Atualiza os produtos existentes na mesma posição
        for (int i = 0; i < productList.size(); i++) {
            GProduct existing = productList.get(i);
            String id = existing.productId();
            if (newProductsById.containsKey(id)) {
                productList.set(i, newProductsById.get(id)); // Substitui na mesma posição
                newProductsById.remove(id); // Remove já tratado
            }
        }

        // Adiciona os novos ao final
        productList.addAll(newProductsById.values());

        displayProducts(productList);
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
        // Atualiza o produto na lista de dados
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).productId().equals(updatedProduct.productId())) {
                productList.set(i, updatedProduct);
                break;
            }
        }

        // Atualiza o componente correspondente na UI
        Component[] components = productContainer.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component comp = components[i];
            if (comp instanceof JPanel panel) {
                Object id = panel.getClientProperty("productId");
                if (id != null && id.equals(updatedProduct.productId())) {
                    // Substitui o painel antigo pelo novo
                    productContainer.remove(i);
                    JPanel newPanel = createProductPanel(updatedProduct);
                    newPanel.putClientProperty("productId", updatedProduct.productId());
                    productContainer.add(newPanel, i);
                    productContainer.revalidate();
                    productContainer.repaint();
                    break;
                }
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
        panel.putClientProperty("productId", product.productId());
        panel.putClientProperty("product", product); // <-- Aqui está o novo uso

        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        gbc.gridx = 0;
        gbc.weightx = 2;
        JLabel lblTitle = new JLabel(product.title());
        lblTitle.setPreferredSize(new Dimension(Constants.APP_WIDTH - 345, 20));
        panel.add(lblTitle, gbc);

        // THC
        gbc.gridx++;
        gbc.weightx = 1;
        JLabel lblTHC = new JLabel("<html><body><small>" + product.thcPercentageMini() + "%</small></body></html>");
        lblTHC.setPreferredSize(new Dimension(35, 20));
        panel.add(lblTHC, gbc);

        // Total Size
        gbc.gridx++;
        JLabel lblTotalSize = new JLabel(String.format("<html><body><small>%sg</small></body></html>",
                product.totalSize().stripTrailingZeros().toPlainString()));
        lblTotalSize.setPreferredSize(new Dimension(35, 20));
        panel.add(lblTotalSize, gbc);

        // THC Cost Benefit
        gbc.gridx++;
        JLabel lblTHCCB = new JLabel(String.format("<html><body><small>$ %.2f/g/THC%%</small></body></html>",
                product.thcCostBenefit()));
        lblTHCCB.setPreferredSize(new Dimension(75, 20));
        panel.add(lblTHCCB, gbc);

        // Cost Benefit
        gbc.gridx++;
        JLabel lblCostBenefit = new JLabel(String.format("<html><body><small>$ %.2f/g</small></body></html>",
                product.costBenefit()));
        lblCostBenefit.setPreferredSize(new Dimension(50, 20));
        panel.add(lblCostBenefit, gbc);

        // Price
        gbc.gridx++;
        JLabel lblPrice = new JLabel(String.format("$ %.2f", product.price()));
        lblPrice.setPreferredSize(new Dimension(50, 20));
        panel.add(lblPrice, gbc);

        // Mouse interactions (refatorado)
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                GProduct clickedProduct = (GProduct) ((JPanel) e.getSource()).getClientProperty("product");
                GProductView view = new GProductView(clickedProduct);
                new GProductController(view);
                view.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(Color.WHITE);
            }
        });

        return panel;
    }

    public List<GProduct> getProducts() {return productList;}

    public void setStatus(String status) {lblStatus.setText(status == null || status.isEmpty() ? " " : status);}
}