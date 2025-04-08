package org.example.model.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

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

    String formattedDate = Optional.ofNullable(sunrise).map(dateFormat::format).orElse(null);
    String formattedSunriseTime = Optional.ofNullable(sunrise).map(timeFormat::format).orElse(null);
    String formattedSunsetTime = Optional.ofNullable(sunset).map(timeFormat::format).orElse(null);

    return String.format("(%s) %s - %s", formattedDate, formattedSunriseTime, formattedSunsetTime);
  }
}
