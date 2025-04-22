package org.example.view;

import org.example.Constants;
import org.example.Main;
import org.example.controller.components.log.AppLogController;
import org.example.view.components.*;
import org.example.view.components.log.AppLogPanel;
import org.example.view.components.system.SystemInfoPanel;
import org.example.view.components.weather.WeatherPanel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HomeView extends JFrame {

    private final JButton btnTheme = new JButton("🎨");
    private final JButton btnChangeMonitor = new JButton("️🖥️");
    private final JButton btnAppLog = new JButton("\uD83D\uDDC2️");
    private final JButton btnAbout = new JButton("ℹ");
    private final JButton btnCloseApp = new JButton("❌");

    private final AppLogPanel appLogPanel;
    private final ClockPanel clockPanel;
    private final WeatherPanel weatherPanel;
    private final SystemInfoPanel sysInfoPanel;
    private final CurrencyPanel currencyPanel;

    private final JPopupMenu contextMenu = new JPopupMenu();

    public HomeView(int initialMonitorIndex) {
        setTitle(Constants.APP_TITLE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setLayout(null);

        URL iconURL = Main.class.getClassLoader().getResource("icon.png");
        if (iconURL == null) System.out.println("App icon not found");
        else {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        initialMonitorIndex = Math.min(initialMonitorIndex, screens.length - 1);
        Rectangle screenBounds = screens[initialMonitorIndex].getDefaultConfiguration().getBounds();
        setBounds(screenBounds);

        appLogPanel = new AppLogPanel(true, screenBounds, false);
        AppLogController appLogController = new AppLogController(appLogPanel);

        clockPanel = new ClockPanel(true, appLogController::addLog);
        weatherPanel = new WeatherPanel(true, appLogController::addLog);
        sysInfoPanel = new SystemInfoPanel(true, appLogController::addLog);
        currencyPanel = new CurrencyPanel(true, appLogController::addLog);

        renderContextMenu();
        renderPanels();
    }

    private void renderPanels() {
        int w = getWidth(), h = getHeight();
        int x = w - 250, y = h - 210;

        appLogPanel.setBounds(w - 450, 50, 400, 350);
        add(appLogPanel);

        currencyPanel.setBounds(50, y, 250, 200);
        add(currencyPanel);

        clockPanel.setBounds(50, 50, 350, 115);
        add(clockPanel);

        weatherPanel.setBounds(50, 200, 400, 300);
        add(weatherPanel);

        sysInfoPanel.setBounds(x, y, 250, 200);
        add(sysInfoPanel);
    }

    private void renderContextMenu() {
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Color.GRAY, 1));
        UIManager.put("PopupMenu.background", new Color(0, 0, 0, 0));

        JButton[] buttons = {btnTheme, btnChangeMonitor, btnAppLog, btnAbout, btnCloseApp};

        int width = 0, height = 0;
        for (JButton btn : buttons) {
            btn.setFont(Constants.FONT_ACTION);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setSize(new Dimension(80, 80));

            height += btn.getPreferredSize().height;
            width = Math.max(width, btn.getPreferredSize().width);
            contextMenu.add(btn);
        }

        contextMenu.setPreferredSize(new Dimension(width + 2, height + 2));
    }

    public JButton btnTheme() {
        return btnTheme;
    }

    public JButton btnAbout() {
        return btnAbout;
    }

    public JButton btnChangeMonitor() {
        return btnChangeMonitor;
    }

    public JButton btnAppLog() {
        return btnAppLog;
    }

    public JButton btnCloseApp() {
        return btnCloseApp;
    }

    public CurrencyPanel currencyPanel() {
        return currencyPanel;
    }

    public AppLogPanel appLogPanel() {
        return appLogPanel;
    }

    public SystemInfoPanel sysInfoPanel() {
        return sysInfoPanel;
    }

    public JPopupMenu contextMenu() {
        return contextMenu;
    }

    public Component[] getThemeableComponents() {
        return new Container[]{
                getContentPane(),

                btnTheme,
                btnChangeMonitor,
                btnAppLog,
                btnAbout,
                btnCloseApp,

                clockPanel,
                weatherPanel,
                currencyPanel,
                appLogPanel,
                sysInfoPanel
        };
    }
}