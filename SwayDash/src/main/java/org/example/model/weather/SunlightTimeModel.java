package org.example.model.weather;

import java.text.SimpleDateFormat;
import java.util.Date;

public record SunlightTimeModel(
        Date sunrise,
        Date sunset
) {

  public String sunriseTime() {
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    return timeFormat.format(sunrise);
  }

  public String sunsetTime() {
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    return timeFormat.format(sunset);
  }

  @Override
  public String toString() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    String formattedDate = dateFormat.format(sunrise);  // Formata a data para dd/MM/yy
    String formattedSunriseTime = timeFormat.format(sunrise);  // Formata a hora do nascer do sol
    String formattedSunsetTime = timeFormat.format(sunset);  // Formata a hora do pôr do sol

    return String.format("(%s) %s - %s", formattedDate, formattedSunriseTime, formattedSunsetTime);
  }
}
