package org.example.controller;

import org.example.Constants;
import org.example.model.ExtractorData;
import org.example.model.Extractors;
import org.example.model.GProduct;
import org.example.view.HomeView;
import org.openqa.selenium.WebDriver;

import javax.swing.*;
import java.util.List;

public class HomeController {

    private final HomeView view;

    public HomeController(HomeView view) {
        this.view = view;

        view.menuExtractPR().addActionListener(_ -> extractPR(null, null));
        view.menuExtractDF().addActionListener(_ -> extractDF(null, null));

        view.menuSortTitleAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::title, true));
        view.menuSortTitleDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::title, false));

        view.menuSortPriceAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::price, true));
        view.menuSortPriceDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::price, false));

        view.menuSortSizeAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::totalSize, true));
        view.menuSortSizeDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::totalSize, false));

        view.menuSortCBAsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::costBenefit, true));
        view.menuSortCBDsc().addActionListener(_ -> view.productTable().sortProducts(GProduct::costBenefit, false));

        view.menuExtractAll().addActionListener(_ -> {
            WebDriver driver = Constants.GET_NEW_WEBDRIVER();
            extractPR(driver, () -> extractDF(driver, driver::quit));
        });

        view.menuExtractGetAllInfo().addActionListener(_ -> extractProductsInfo());
    }

    private void extractProductsInfo() {
        List<GProduct> products = view.productTable().getProducts();
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(view, "There are no products to extract");
            return;
        }


        record UpdateStatus(String message, GProduct product) {
        }
        new SwingWorker<Void, UpdateStatus>() {
            @Override
            protected Void doInBackground() {
                WebDriver driver = Constants.GET_NEW_WEBDRIVER();
                int total = products.size();
                int current = 1;

                for (GProduct product : products) {
                    Extractors.getProductInfo(product, driver); // Atualiza os dados do objeto

                    String msg = String.format("Processing %d of %d...", current, total);
                    publish(new UpdateStatus(msg, product));
                    current++;
                }

                driver.quit();
                return null;
            }

            @Override
            protected void process(List<UpdateStatus> chunks) {
                UpdateStatus latest = chunks.getLast();
                view.lblStatus().setText(latest.message());
                view.productTable().updateSingleProduct(latest.product());
            }

            @Override
            protected void done() {
                view.lblStatus().setText("");
            }
        }.execute();

    }

    private void extractPR(WebDriver d, Runnable onFinished) {
        SwingWorker<Void, List<GProduct>> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                WebDriver driver = d != null ? d : Constants.GET_NEW_WEBDRIVER();
                boolean hasNextPage;
                int page = 1;

                do {
                    int finalPage = page;
                    SwingUtilities.invokeLater(() -> view.lblStatus().setText("Extracting PR - Page: " + finalPage + "..."));
                    ExtractorData data = Extractors.pr(driver, page);
                    publish(data.products());
                    hasNextPage = data.hasNextPage();
                    page++;
                } while (hasNextPage);

                SwingUtilities.invokeLater(() -> view.lblStatus().setText(""));
                if (d == null) driver.quit();
                return null;
            }

            @Override
            protected void process(List<List<GProduct>> chunks) {
                for (List<GProduct> products : chunks) {
                    view.productTable().addProducts(products);
                }
            }

            @Override
            protected void done() {
                if (onFinished != null) {
                    onFinished.run();
                }
            }
        };
        worker.execute();
    }

    private void extractDF(WebDriver d, Runnable onFinished) {
        SwingWorker<Void, List<GProduct>> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                WebDriver driver = d != null ? d : Constants.GET_NEW_WEBDRIVER();
                boolean hasNextPage;
                int page = 1;

                do {
                    int finalPage = page;
                    SwingUtilities.invokeLater(() -> view.lblStatus().setText("Extracting DF - Page: " + finalPage + "..."));
                    ExtractorData data = Extractors.df(driver, page);
                    publish(data.products());
                    hasNextPage = data.hasNextPage();
                    page++;
                } while (hasNextPage);

                SwingUtilities.invokeLater(() -> view.lblStatus().setText(""));
                if (d == null) driver.quit();
                return null;
            }

            @Override
            protected void process(List<List<GProduct>> chunks) {
                for (List<GProduct> products : chunks) {
                    view.productTable().addProducts(products);
                }
            }

            @Override
            protected void done() {
                if (onFinished != null) {
                    onFinished.run();
                }
            }
        };
        worker.execute();
    }
}
