package org.example;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Constants {

    public static final String APP_TITLE = "Filtering Outlook Emails";
    public static final Dimension APP_RESOLUTION = new Dimension(640, 480);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_DEFAULT = new Font("Segoe UI Emoji", Font.PLAIN, 12);
    public static final int MAX_EMAILS_GET = 100000;

    public static JSONObject GET_REQUEST(String endpoint, String token) throws Exception {
        URL url = new URI(endpoint).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            String msg = String.format("%s: %s", responseCode, conn.getResponseMessage());
            throw new Exception(msg);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();

        return new JSONObject(response.toString());
    }

    public static void DELETE_REQUEST(String endpoint, String token) throws Exception {
        URL url = new URI(endpoint).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int responseCode = conn.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            String msg = String.format("%s: %s", responseCode, conn.getResponseMessage());
            throw new Exception(msg);
        }

        // Se houver conteúdo (raro em DELETE), pode ler aqui
        conn.disconnect();
    }


    public static void OPEN_LINK(String url)  {
        try {
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(url));
            else throw new UnsupportedOperationException("Desktop não suportado.");
        } catch (Exception e) {
            SHOW_ERROR_DIALOG(null, e);
        }
    }

    public static void COPY_TO_CLIPBOARD(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void SHOW_ERROR_DIALOG(Component view, Exception e) {
        String errorType = e.getClass().getSimpleName();
        JOptionPane.showMessageDialog(view, e.getMessage(), "ERROR - " + errorType, JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
