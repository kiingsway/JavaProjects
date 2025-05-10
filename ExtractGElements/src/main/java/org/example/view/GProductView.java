package org.example.view;

import org.example.Constants;
import org.example.model.GProduct;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.Objects;
import java.util.List;


public class GProductView extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    private final GProduct product;

    private static final JLabel lblTitle = new JLabel();
    private static final JLabel lblPrice = new JLabel();
    private static final JLabel lblDescription = new JLabel();
    private static final JLabel lblBrand = new JLabel();
    private static final JLabel lblTotalSize = new JLabel();
    private static final JLabel lblCultivator = new JLabel();
    private static final JLabel lblEffect = new JLabel();
    private static final JLabel lblTHCUnit = new JLabel();
    private static final JLabel lblCBDUnit = new JLabel();
    private static final JLabel lblTHC = new JLabel();
    private static final JLabel lblCBD = new JLabel();
    private static final JLabel lblCostBenefit = new JLabel();
    private static final JLabel lblProductId = new JLabel();

    private static final JButton btnLoadMoreInfo = new JButton("Load more...");

    private static final List<JLabel> labels = List.of(lblDescription, lblBrand, lblTotalSize, lblCultivator, lblEffect, lblTHCUnit, lblCBDUnit, lblTHC, lblCBD, lblCostBenefit, lblProductId);

    public GProductView(GProduct product) {
        this.product = product;

        setTitle("GProduct - " + product.title());
        setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        renderInfo();
    }

    private void renderInfo() {
        lblTitle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblTitle.setVerticalAlignment(SwingConstants.TOP);
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lblTitle.setBounds(15, 15, (Constants.APP_WIDTH - 50), 35);
        lblPrice.setBounds(30, 45, 200, 20);
        btnLoadMoreInfo.setBounds(30, 75, 120, 20);

        add(lblTitle);
        add(lblPrice);
        add(btnLoadMoreInfo);

        Font plain = new Font("Segoe UI", Font.PLAIN, 14);

        for (JLabel label : labels) label.setFont(plain);

        fillProductInfo();
    }

    public void fillProductInfo() {
        String totalSizeText = String.format("%.2f", product.totalSize()) + "g";
        if (product.nItems() != 1)
            totalSizeText += String.format(" (%d x %.2fg)", product.nItems(), product.itemSize());

        lblTitle.setText(product.title());
        lblPrice.setText("$ " + product.price());
        lblDescription.setText(product.description());
        lblBrand.setText("<html><body><b>Brand:</b> " + product.brand() + "</body></html>");
        lblTotalSize.setText("<html><body><b>Total size:</b> " + totalSizeText + "</body></html>");
        lblCultivator.setText("<html><body><b>Cultivator:</b> " + product.cultivator() + "</body></html>");
        lblEffect.setText("<html><body><b>Effect:</b> " + product.effect() + "</body></html>");

        String minMax = Objects.equals(product.thcMin(), product.thcMax()) ? String.valueOf(product.thcMin()) : product.thcMin() + " - " + product.thcMax();
        lblTHCUnit.setText("<html><body><b>THC (" + product.thcUnit() + "):</b> " + minMax + "</body></html>");

        minMax = Objects.equals(product.cbdMin(), product.cbdMax()) ? String.valueOf(product.cbdMin()) : product.cbdMin() + " - " + product.cbdMax();
        lblCBDUnit.setText("<html><body><b>CBD (" + product.cbdUnit() + "):</b> " + minMax + "</body></html>");

        lblTHC.setText("<html><body><b>THC (%):</b> " + product.thcPercentage() + "</body></html>");
        lblCBD.setText("<html><body><b>CBD (%):</b> " + product.cbdPercentage() + "</body></html>");

        lblCostBenefit.setText("<html><body><b>Cost Benefit:</b> $ " + product.costBenefit() + "/g</body></html>");

        lblProductId.setText("<html><body><b>Product ID:</b> " + product.productId() + "</body></html>");

        shapeProductInfo();
    }

    private void shapeProductInfo() {
        int y = 100;
        for (JLabel label : labels) {
            int width = Math.max(label.getPreferredSize().width, Constants.APP_WIDTH - 100);
            int height = Math.max(20, label.getPreferredSize().height);
            label.setBounds(30, y, width, height);
            label.setToolTipText(label.getText());
            add(label);
            y += label.getPreferredSize().height;
        }
    }

    public GProduct product() {return product;}

    public JLabel lblTitle() {return lblTitle;}

    public JButton btnLoadMoreInfo() {return btnLoadMoreInfo;}
}
