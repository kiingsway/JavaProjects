package org.example.model.weather;

import java.util.Optional;

public record TemperatureRangeModel(
        Integer high,
        Integer low
) {
  @Override
  public String toString() {

    String highStr = Optional.ofNullable(high).map(t -> t + "ºC").orElse(null);
    String lowStr = Optional.ofNullable(low).map(t -> t + "ºC").orElse(null);
    return String.format("high=\"%s\" | low=\"%s\"", highStr, lowStr);
  }
}
