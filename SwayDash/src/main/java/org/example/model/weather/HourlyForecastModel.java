package org.example.model.weather;

import java.util.List;

public record HourlyForecastModel(
        List<HourlyForecastItem> items
) {
  @Override
  public String toString() {
    return "HourlyForecastModel {\n  items=" + //
            (items != null && !items.isEmpty() ? items.stream() //
                    .map(HourlyForecastItem::toString) //
                    .reduce((a, b) -> a + ",\n  " + b) //
                    .orElse("[]") //
                    : "[]") + //
            "\n}";
  }
}

