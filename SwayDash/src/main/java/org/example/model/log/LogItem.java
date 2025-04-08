package org.example.model.log;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogItem {
  private final LogItemLevel level;
  private final String component;
  private final Date date;
  private final String message;
  private final String exceptionClass;

  @SuppressWarnings("CallToPrintStackTrace")
  public LogItem(LogItemLevel level, String component, Exception exception) {
    this.level = level;
    this.component = component;
    this.message = exception.getMessage();
    this.exceptionClass = exception.getClass().getName();
    this.date = new Date();

    exception.printStackTrace();
  }

  public Color levelColor() {return level.getColor();}

  private String time() {return new SimpleDateFormat("HH:mm:ss").format(date);}

  public String getFullMessage() {
    return String.format("[%s] %s - %s\n\n%s", time(), component, exceptionClass, message);
  }

  @Override
  public String toString() {return String.format("[%s - %s] %s", time(), component, message);}
}