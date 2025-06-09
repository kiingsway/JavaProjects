package org.example.view.home;

import javax.swing.*;

public class HomeMenuBar extends JMenuBar {

    private static final JMenuItem extractPR = new JMenuItem("Pre-R");
    private static final JMenuItem extractDF = new JMenuItem("Dried-F");
    private static final JMenuItem extractAll = new JMenuItem("All Extractors");
    private static final JMenuItem extractGetAllInfo = new JMenuItem("Get all products info");

    private static final JMenuItem sortCBAsc = new JMenuItem("Low to High");
    private static final JMenuItem sortCBDsc = new JMenuItem("High to Low");
    private static final JMenuItem sortPriceAsc = new JMenuItem("Low to High");
    private static final JMenuItem sortPriceDsc = new JMenuItem("High to Low");
    private static final JMenuItem sortSizeAsc = new JMenuItem("Lightest first");
    private static final JMenuItem sortSizeDsc = new JMenuItem("Heaviest first");
    private static final JMenuItem sortTitleAsc = new JMenuItem("A to Z");
    private static final JMenuItem sortTitleDsc = new JMenuItem("Z to A");

    private static final JMenuItem fileResetChrome = new JMenuItem("Reset Chrome Settings");
    private static final JMenuItem fileQuit = new JMenuItem("Quit");

    public HomeMenuBar() {
        // Menu: File
        JMenu file = new JMenu("File");
        file.add(fileResetChrome);
        file.addSeparator();
        file.add(fileQuit);

        // Menu: Extract
        JMenu extract = new JMenu("Extract");
        extract.add(extractAll);
        extract.addSeparator();
        extract.add(extractPR);
        extract.add(extractDF);
        extract.addSeparator();
        extract.add(extractGetAllInfo);

        // Menu: Sort
        JMenu sort = new JMenu("Sort");
        JMenu sortTitle = new JMenu("Title");
        JMenu sortPrice = new JMenu("Price");
        JMenu sortSize = new JMenu("Size");
        JMenu sortCB = new JMenu("Cost Benefit");

        // Menu: Sort - Title
        sortTitle.add(sortTitleAsc);
        sortTitle.add(sortTitleDsc);
        sort.add(sortTitle);

        // Menu: Sort - Price
        sortPrice.add(sortPriceAsc);
        sortPrice.add(sortPriceDsc);
        sort.add(sortPrice);

        // Menu: Sort - Size
        sortSize.add(sortSizeAsc);
        sortSize.add(sortSizeDsc);
        sort.add(sortSize);

        // Menu: Sort - Cost-Benefit
        sortCB.add(sortCBAsc);
        sortCB.add(sortCBDsc);
        sort.add(sortCB);

        // Adding to main menu
        add(file);
        add(extract);
        add(sort);
    }

    public JMenuItem extractPR() {return extractPR;}

    public JMenuItem extractDF() {return extractDF;}

    public JMenuItem extractAll() {return extractAll;}

    public JMenuItem extractGetAllInfo() {return extractGetAllInfo;}

    public JMenuItem sortCBAsc() {return sortCBAsc;}

    public JMenuItem sortCBDsc() {return sortCBDsc;}

    public JMenuItem sortPriceAsc() {return sortPriceAsc;}

    public JMenuItem sortPriceDsc() {return sortPriceDsc;}

    public JMenuItem sortSizeAsc() {return sortSizeAsc;}

    public JMenuItem sortSizeDsc() {return sortSizeDsc;}

    public JMenuItem sortTitleAsc() {return sortTitleAsc;}

    public JMenuItem sortTitleDsc() {return sortTitleDsc;}

    public JMenuItem fileQuit() {return fileQuit;}

    public JMenuItem fileResetChrome() {return fileResetChrome;}

}
