package org.example;

import javax.swing.*;
import java.awt.*;

public class Constants {
    public static final String APP_TITLE = "Timed Tasks";
    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    public static void SHOW_ERROR_DIALOG(Exception e, Component view) {
        SHOW_ERROR_DIALOG_RUN(e, view);
    }

    public static void SHOW_ERROR_DIALOG(Exception e) {
        SHOW_ERROR_DIALOG_RUN(e, null);
    }

    private static void SHOW_ERROR_DIALOG_RUN(Exception e, Component view) {
        System.out.println(e.getMessage());
        String errorType = e.getClass().getSimpleName();
        String title = "ERROR - " + errorType;
        JOptionPane.showMessageDialog(view, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
}
