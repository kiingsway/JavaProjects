package org.example.view;

import org.example.Constants;
import org.example.view.components.ProductListPanel;
import org.example.view.home.HomeMenuBar;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

    private static final HomeMenuBar homeMenuBar = new HomeMenuBar();
    private static final ProductListPanel productTable = new ProductListPanel();

    public HomeView() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(productTable, BorderLayout.CENTER);

        setJMenuBar(homeMenuBar);
    }

    public HomeMenuBar homeMenuBar() {return homeMenuBar;}

    public ProductListPanel productTable() {return productTable;}
}
