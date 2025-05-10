package org.example.view;

import org.example.Constants;
import org.example.view.components.ProductListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HomeView extends JFrame {

    private static final JMenuItem menuExtractPR = new JMenuItem("Pre-R");
    private static final JMenuItem menuExtractDF = new JMenuItem("Dried-F");
    private static final JMenuItem menuExtractAll = new JMenuItem("All Extractors");
    private static final JMenuItem menuExtractGetAllInfo = new JMenuItem("Get all products info");

    private static final JMenuItem menuSortCBAsc = new JMenuItem("Low to High");
    private static final JMenuItem menuSortCBDsc = new JMenuItem("High to Low");

    private static final JMenuItem menuSortPriceAsc = new JMenuItem("Low to High");
    private static final JMenuItem menuSortPriceDsc = new JMenuItem("High to Low");

    private static final JMenuItem menuSortSizeAsc = new JMenuItem("Lightest first");
    private static final JMenuItem menuSortSizeDsc = new JMenuItem("Heaviest first");

    private static final JMenuItem menuSortTitleAsc = new JMenuItem("A to Z");
    private static final JMenuItem menuSortTitleDsc = new JMenuItem("Z to A");

    private static final ProductListPanel productTable = new ProductListPanel();
    private static final JLabel lblStatus = new JLabel();

    public HomeView() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(lblStatus, BorderLayout.NORTH);
        add(productTable, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        renderMenu();
    }

    private void renderMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menu: File
        JMenu menuFile = new JMenu("File");
        JMenuItem menuFileQuit = new JMenuItem("Quit");
        menuFileQuit.addActionListener(_ -> onClose());
        menuFile.add(menuFileQuit);

        // Menu: Extract
        JMenu menuExtract = new JMenu("Extract");
        menuExtract.add(menuExtractAll);
        menuExtract.addSeparator();
        menuExtract.add(menuExtractPR);
        menuExtract.add(menuExtractDF);
        menuExtract.addSeparator();
        menuExtract.add(menuExtractGetAllInfo);

        // Menu: Sort
        JMenu menuSort = new JMenu("Sort");
        JMenu menuSortTitle = new JMenu("Title");
        JMenu menuSortPrice = new JMenu("Price");
        JMenu menuSortSize = new JMenu("Size");
        JMenu menuSortCB = new JMenu("Cost Benefit");

        menuSortTitle.add(menuSortTitleAsc);
        menuSortTitle.add(menuSortTitleDsc);
        menuSort.add(menuSortTitle);

        menuSortPrice.add(menuSortPriceAsc);
        menuSortPrice.add(menuSortPriceDsc);
        menuSort.add(menuSortPrice);

        menuSortSize.add(menuSortSizeAsc);
        menuSortSize.add(menuSortSizeDsc);
        menuSort.add(menuSortSize);

        menuSortCB.add(menuSortCBAsc);
        menuSortCB.add(menuSortCBDsc);
        menuSort.add(menuSortCB);

        menuBar.add(menuFile);
        menuBar.add(menuExtract);
        menuBar.add(menuSort);

        setJMenuBar(menuBar);
    }

    private void onClose() {
        String msg = "Are you sure you want to exit?";
        int resp = JOptionPane.showConfirmDialog(this, msg, Constants.APP_TITLE, JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public JLabel lblStatus() {return lblStatus;}

    public JMenuItem menuExtractPR() {return menuExtractPR;}

    public JMenuItem menuExtractDF() {return menuExtractDF;}

    public JMenuItem menuExtractAll() {return menuExtractAll;}

    public JMenuItem menuExtractGetAllInfo() {return menuExtractGetAllInfo;}

    public JMenuItem menuSortCBAsc() {return menuSortCBAsc;}

    public JMenuItem menuSortCBDsc() {return menuSortCBDsc;}

    public JMenuItem menuSortPriceAsc() {return menuSortPriceAsc;}

    public JMenuItem menuSortPriceDsc() {return menuSortPriceDsc;}

    public JMenuItem menuSortSizeAsc() {return menuSortSizeAsc;}

    public JMenuItem menuSortSizeDsc() {return menuSortSizeDsc;}

    public JMenuItem menuSortTitleAsc() {return menuSortTitleAsc;}

    public JMenuItem menuSortTitleDsc() {return menuSortTitleDsc;}

    public ProductListPanel productTable() {return productTable;}

}
