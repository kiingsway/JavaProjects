package org.example.controller;

import org.example.Constants;
import org.example.model.Extractors;
import org.example.view.GProductView;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GProductController {

    private final GProductView view;

    public GProductController(GProductView view) {
        this.view = view;

        setListeners();
    }

    private void setListeners() {
        // Button Load More Info
        Constants.REMOVE_ALL_LISTENERS(view.menuLoadMoreInfo());
        view.menuLoadMoreInfo().addActionListener(_ -> {
            System.out.println("Loading more info...");
            WebDriver driver = Constants.GET_NEW_WEBDRIVER();
            Extractors.getProductInfo(view.product(), driver);
            driver.quit();
            view.fillProductInfo();
        });

        // Label Title
        Constants.REMOVE_ALL_LISTENERS(view.lblTitle());

        view.lblTitle().setForeground(Color.BLACK); // cor padrão
        view.lblTitle().setCursor(new Cursor(Cursor.HAND_CURSOR)); // opcional: cursor de link
        view.lblTitle().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Constants.OPEN_LINK(view.product().url());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                view.lblTitle().setText("<html><u>" + view.lblTitle().getText() + "</u></html>");
                view.lblTitle().setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                String text = view.lblTitle().getText().replaceAll("(?i)<.*?>", ""); // remove tags HTML
                view.lblTitle().setText(text);
                view.lblTitle().setForeground(Color.BLACK);
            }
        });
    }
}
