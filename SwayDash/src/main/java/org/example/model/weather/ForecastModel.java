package org.example.model.weather;

import java.util.List;

public record ForecastModel(
        List<ForecastItem> items
) {
  @Override
  public String toString() {
    return "ForecastModel {\n  items=" + //
            (items != null && !items.isEmpty() ? items.stream() //
                    .map(ForecastItem::toString) //
                    .reduce((a, b) -> a + ",\n  " + b) //
                    .orElse("[]") //
                    : "[]") + //
            "\n}";
  }
}

