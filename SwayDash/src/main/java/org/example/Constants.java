package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Constants {

  private static final String FONTS_PATH = "src/main/resources/Rajdhani-";
  private static final String RAJDHANI_BOLD_FILE = FONTS_PATH + "Bold.ttf";
  private static final String RAJDHANI_REGULAR_FILE = FONTS_PATH + "Regular.ttf";

  public static final Font FONT_CLOCK = loadFont(RAJDHANI_BOLD_FILE, 80f);
  public static final Font FONT_DATE = loadFont(RAJDHANI_REGULAR_FILE, 30f);
  public static final Font FONT_ACTION = new Font("Segoe UI Emoji", Font.BOLD, 30);
  public static final Font FONT_ACTION_SM = new Font("Segoe UI Emoji", Font.BOLD, 20);

  private static Font loadFont(String path, float size) {
    try {
      File fontFile = new File(path);
      Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(size);
      GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
      return font;
    } catch (IOException | FontFormatException e) {
      System.out.println("Erro ao carregar a fonte '" + path + "': " + e.getMessage());
      return new Font("Arial", Font.PLAIN, (int) size); // Fallback para Arial
    }
  }
}
