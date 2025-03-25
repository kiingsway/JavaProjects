package org.example;

import org.example.controller.AboutController;
import org.example.controller.HomeController;
import org.example.model.Constants;
import org.example.view.AboutPage;
import org.example.view.home.HomePage;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

  private final JPanel tabPanel = new JPanel(new CardLayout());

  public App() {
    setTitle(Constants.APP_TITLE);
    setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    renderMenu();
    renderContent();

    setVisible(true);
  }

  private void renderMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem menuCloseApp = new JMenuItem("Exit Application");
    menuCloseApp.addActionListener(_ -> closeApp());
    fileMenu.add(menuCloseApp);

    JMenu categoriesMenu = new JMenu("Categories");
    JMenuItem menuAddCat = new JMenuItem("Create Category");
    JCheckBoxMenuItem menuCatAll = new JCheckBoxMenuItem("All Categories");
    menuCatAll.setSelected(true);
    categoriesMenu.add(menuAddCat);
    categoriesMenu.addSeparator();
    categoriesMenu.add(menuCatAll);

    JMenu helpMenu = new JMenu("Help");
    JMenuItem menuAbout = new JMenuItem("About");
    menuAbout.addActionListener(_ -> showAbout());
    helpMenu.add(menuAbout);

    menuBar.add(fileMenu);
    menuBar.add(categoriesMenu);
    menuBar.add(helpMenu);

    setJMenuBar(menuBar);
  }

  private void renderContent() {
    HomePage homePage = new HomePage();
    AboutPage aboutPage = new AboutPage();

    new HomeController(homePage);
    new AboutController(aboutPage, this::showHome);

    tabPanel.add(homePage, "home");
    tabPanel.add(aboutPage, "about");

    add(tabPanel, BorderLayout.CENTER);
  }

  private void showHome() {
    ((CardLayout) tabPanel.getLayout()).show(tabPanel, "home");
  }

  private void showAbout() {
    ((CardLayout) tabPanel.getLayout()).show(tabPanel, "about");
  }

  private void closeApp() {
    String tit = Constants.APP_TITLE;
    String msg = "Are you sure you want to exit?";
    int response = JOptionPane.showConfirmDialog(this, msg, tit, JOptionPane.YES_NO_OPTION);
    if (response == JOptionPane.YES_OPTION) {
      this.dispose();
      System.exit(0);
    }
  }
}