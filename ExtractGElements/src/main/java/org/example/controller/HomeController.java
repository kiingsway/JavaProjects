package org.example.controller;

import org.example.Constants;
import org.example.model.Extractors;
import org.example.model.GProduct;
import org.example.view.HomeView;
import org.example.view.components.ChromeCleanerUI;
import org.openqa.selenium.WebDriver;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.example.model.Extractors.*;

public class HomeController {

    private final HomeView view;

    public HomeController(HomeView view) {
        this.view = view;

        view.homeMenuBar().fileResetChrome().addActionListener(_ -> {
            ChromeCleanerUI cleanerUI = new ChromeCleanerUI();
            cleanerUI.setVisible(true);
        });

        view.homeMenuBar().fileQuit().addActionListener(_ -> onClose());
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        view.homeMenuBar().extractPR().addActionListener(_ -> getCollections(url_pr, view.productTable(), null, null));
        view.homeMenuBar().extractDF().addActionListener(_ -> getCollections(url_df, view.productTable(), null, null));
        view.homeMenuBar().extractAll().addActionListener(_ -> extractAllCollections());
        view.homeMenuBar().extractGetAllInfo().addActionListener(_ -> extractProductsInfo());

        view.homeMenuBar().sortTitleAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::title, true));
        view.homeMenuBar().sortTitleDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::title, false));

        view.homeMenuBar().sortPriceAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::price, true));
        view.homeMenuBar().sortPriceDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::price, false));

        view.homeMenuBar().sortSizeAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::totalSize, true));
        view.homeMenuBar().sortSizeDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::totalSize, false));

        view.homeMenuBar().sortCBAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::costBenefit, true));
        view.homeMenuBar().sortCBDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::costBenefit, false));
    }

    private void extractAllCollections() {
        WebDriver d = Constants.GET_NEW_WEBDRIVER();
        Runnable onFinish = () -> getCollections(url_df, view.productTable(), d, d::quit);
        getCollections(url_pr, view.productTable(), d, onFinish);
    }

    private void extractProductsInfo() {
        Extractors.getProductsInfo(view.productTable());
    }

    private void onClose() {
        String msg = "Are you sure you want to exit?";
        int resp = JOptionPane.showConfirmDialog(view, msg, Constants.APP_TITLE, JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            view.dispose();
            System.exit(0);
        }
    }
}
