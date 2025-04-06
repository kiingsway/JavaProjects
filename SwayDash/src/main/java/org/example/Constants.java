package org.example;

import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Constants {

  public static final String APP_TITLE = "Sway Dash";

  private static final String FONTS_PATH = "src/main/resources/Rajdhani-";
  private static final String RAJDHANI_BOLD_FILE = FONTS_PATH + "Bold.ttf";
  private static final String RAJDHANI_MEDIUM_FILE = FONTS_PATH + "Medium.ttf";

  public static final Font FONT_DEFAULT_30 = loadFont(RAJDHANI_MEDIUM_FILE, 30f);
  public static final Font FONT_DEFAULT_20 = loadFont(RAJDHANI_MEDIUM_FILE, 20f);
  public static final Font FONT_DEFAULT_15 = loadFont(RAJDHANI_MEDIUM_FILE, 15f);
  public static final Font FONT_BOLD_15 = loadFont(RAJDHANI_BOLD_FILE, 15f);

  public static final Font FONT_CLOCK = loadFont(RAJDHANI_BOLD_FILE, 80f);
  public static final Font FONT_DATE = loadFont(RAJDHANI_MEDIUM_FILE, 25f);

  public static final Font FONT_DEFAULT = loadFont(RAJDHANI_MEDIUM_FILE, 25f);
  public static final Font FONT_EMOJI = new Font("Segoe UI Emoji", Font.BOLD, 17);

  public static final Font FONT_WEATHER = loadFont(RAJDHANI_BOLD_FILE, 60f);
  public static final Font FONT_ACTION = new Font("Segoe UI Emoji", Font.BOLD, 30);
  public static final Font FONT_ERROR = new Font("Verdana", Font.PLAIN, 16);

  public static final Color COLOR_DARK_GRAY_65 = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 166);
  public static final Color COLOR_LIGHT_GRAY_65 = new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 166);

  public static final Color COLOR_DARK_GRAY_80 = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 204);
  public static final Color COLOR_LIGHT_GRAY_80 = new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 204);

  private static Font loadFont(String path, float size) {
    try {
      File fontFile = new File(path);
      Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
      GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
      return font;
    } catch (IOException | FontFormatException e) {
      Constants.PRINT("Erro ao carregar a fonte '" + path + "': " + e.getMessage());
      return new Font("Arial", Font.PLAIN, (int) size); // Fallback para Arial
    }
  }

  public static int CLAMP(int val, int min, int max) {
    return Math.max(min, Math.min(val, max));
  }

  public static Integer STRING_TO_INTEGER(String string) {
    if (string.isEmpty()) return null;
    String numberText = string.replaceAll("[^0-9]", "");
    if (numberText.isEmpty()) return null;
    return Integer.parseInt(string.replaceAll("[^0-9-]", ""));
  }

  public static void OPEN_WEBSITE(String url) {
    try {
      URI uri = new URI(url);
      if (Desktop.isDesktopSupported()) {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
          desktop.browse(uri);
        }
      }
    } catch (IOException | URISyntaxException e) {
      SHOW_ERROR_DIALOG(null, e);
    }
  }

  public static void COPY_TO_CLIPBOARD(String text) {
    StringSelection stringSelection = new StringSelection(text);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    try {
      clipboard.setContents(stringSelection, null);
    } catch (Exception _) {}
  }

  public static void SHOW_ERROR_DIALOG(Component view, Exception e) {
    String errorType = e.getClass().getSimpleName();
    JOptionPane.showMessageDialog(view, e.getMessage(), "ERROR - " + errorType, JOptionPane.ERROR_MESSAGE);
  }

  public static void PRINT(String text) {
    PRINTRun(text, false);
  }

  public static void PRINT(String text, boolean printTime) {
    PRINTRun(text, printTime);
  }

  public static void PRINTRun(String text, boolean printTime) {
    String before = "";
    if (printTime)
      before = "[" + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "] ";

    System.out.println(before + text);
  }
}
