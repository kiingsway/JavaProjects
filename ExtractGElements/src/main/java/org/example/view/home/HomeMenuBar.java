package org.example.view.home;

import javax.swing.*;

public class HomeMenuBar extends JMenuBar {

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

    public HomeMenuBar() {
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

        add(menuFile);
        add(menuExtract);
        add(menuSort);
    }
}
