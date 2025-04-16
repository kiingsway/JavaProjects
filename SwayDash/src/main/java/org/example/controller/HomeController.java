package org.example.controller;

import org.example.model.components.ThemedPanel;
import org.example.Constants;
import org.example.view.HomeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeController implements ActionListener {

    private boolean isDarkMode;
    private int monitorIndex;
    private final HomeView view;

    public HomeController(HomeView view, int monitorIndex, boolean isDarkMode) {
        this.monitorIndex = monitorIndex;
        this.isDarkMode = isDarkMode;
        this.view = view;

        PowerManager.preventSleep();

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApp();
            }
        });

        handleListeners();
        setTheme(isDarkMode);
    }

    private void handleListeners() {
        view.btnTheme().addActionListener(this);
        view.btnChangeMonitor().addActionListener(this);
        view.btnAppLog().addActionListener(this);
        view.btnAbout().addActionListener(this);
        view.btnCloseApp().addActionListener(this);

        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                hideMenu();
                if (SwingUtilities.isRightMouseButton(e)) view.contextMenu().show(view, e.getX(), e.getY());
            }
        });
    }

    private void setTheme(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
        Color background = isDarkMode ? Color.BLACK : Color.WHITE;

        for (Component c : view.getThemeableComponents()) {
            c.setBackground(background);
            c.setForeground(foreground);
            if (c instanceof ThemedPanel) ((ThemedPanel) c).setTheme(isDarkMode);
        }

        view.revalidate();
        view.repaint();
    }

    private void changeMonitor() {
        GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (monitorIndex + 1 >= screens.length) monitorIndex = 0;
        else monitorIndex += 1;

        GraphicsDevice screen = screens[monitorIndex];
        DisplayMode dm = screen.getDisplayMode();
        Dimension size = new Dimension(dm.getWidth(), dm.getHeight());
        Rectangle bounds = screen.getDefaultConfiguration().getBounds();

        view.appLogPanel().setScreenSize(new Rectangle(size.width, size.height));

        view.setSize(size.width, size.height);
        view.setLocation(bounds.x, bounds.y);
        view.setExtendedState(JFrame.MAXIMIZED_BOTH);

        int w = view.getWidth(), h = view.getHeight();
        int x = w - 250, y = h - 210;

        view.sysInfoPanel().setBounds(x, y, 250, 200);
        view.currencyPanel().setBounds(50, y, 250, 200);

        view.revalidate();
        view.repaint();
    }

    private void showAbout() {
        String title = Constants.APP_TITLE + " - About";
        String message = """
                <html>
                Developed by <a href="https://github.com/kiingsway">kiingsway</a><br>
                Project: <a href="https://github.com/kiingsway/JavaProjects/tree/main/SwayDash">SwayDash</a>
                </html>""";
        JOptionPane.showMessageDialog(view, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void hideMenu() {
        if (!view.contextMenu().isVisible()) return;
        view.contextMenu().setVisible(false);

        view.revalidate();
        view.repaint();
    }

    private void closeApp() {
        String tit = Constants.APP_TITLE;
        String msg = "Are you sure you want to exit?";
        int resp = JOptionPane.showConfirmDialog(view, msg, tit, JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            PowerManager.allowSleep();
            view.dispose();
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hideMenu();

        Object source = e.getSource();

        if (source == view.btnTheme()) setTheme(!isDarkMode);
        else if (source == view.btnChangeMonitor()) changeMonitor();
        else if (source == view.btnAppLog()) view.appLogPanel().setVisible(!view.appLogPanel().isVisible());
        else if (source == view.btnAbout()) showAbout();
        else if (source == view.btnCloseApp()) closeApp();
    }
}
