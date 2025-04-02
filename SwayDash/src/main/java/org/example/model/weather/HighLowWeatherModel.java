package org.example.model.weather;

public record HighLowWeatherModel(
        Integer high,
        Integer low
) {
  @Override
  public String toString() {return String.format("high=\"%sºC\" | low=\"%sºC\"", high, low);}
}
