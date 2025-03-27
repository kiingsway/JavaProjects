package org.example.model.weather;

import java.util.List;

public record HourlyForecastModel(
        List<HourlyForecastItem> items
) {
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (items == null || items.isEmpty()) return null;

    for (HourlyForecastItem item : items) {
      sb.append("-----------");
      sb.append(item);
    }

    sb.append("\n");
    return sb.toString();
  }
}

