package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.math.BigDecimal;
import java.net.URI;

import javax.swing.*;

@SuppressWarnings("CallToPrintStackTrace")
public class Constants {
    public static final String APP_TITLE = "Extract G Elements";
    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    public static WebDriver GET_NEW_WEBDRIVER() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments(String.format("user-data-dir=%s/chrome-user-data", System.getProperty("user.dir")));
        return new ChromeDriver(options);
    }

    public static BigDecimal STRING_TO_BIGDECIMAL(String input) {
        if (input == null || input.isEmpty()) return BigDecimal.ZERO;

        // Remove tudo, exceto dígitos, ponto, vírgula e um sinal de menos no início
        String cleaned = input.trim().replaceAll("[^\\d,.-]", "");

        // Garante que só um "-" possa estar no início
        cleaned = cleaned.replaceAll("(?<!^)-", "");

        // Se usar vírgula como decimal, converte para ponto
        if (cleaned.contains(",") && !cleaned.contains(".")) {
            cleaned = cleaned.replace(',', '.');
        }

        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public static void OPEN_LINK(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            ex.printStackTrace();
            Constants.SHOW_ERROR_DIALOG(ex);
        }
    }

    public static void COPY_TEXT(String text) {
        StringSelection selText = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selText, null);
        System.out.println("Text copied to clipboard");
    }

    public static void SHOW_ERROR_DIALOG(Exception e) {
        System.out.println(e.getMessage());
        String errorType = e.getClass().getSimpleName();
        String title = "ERROR - " + errorType;
        JOptionPane.showMessageDialog(null, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
}
