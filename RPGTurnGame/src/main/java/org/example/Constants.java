package org.example;

import java.awt.*;

public class Constants {
  public static final String APP_TITLE = "RPG Turn Game";
  public static final Dimension APP_SIZE = new Dimension(800, 600);

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
