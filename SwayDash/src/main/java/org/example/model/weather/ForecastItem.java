package org.example.model.weather;

public record ForecastItem(
        String title,
        String imgUrl,
        Integer temp,
        Integer feels,
        String pop,
        String precipitation,
        String snow,
        Integer night
) {
  @Override
  public String toString() {
    return String.format("ForecastItem [title=%s, imgUrl=%s, temp=%d°C, feels=%d°C, pop=%s %%, precipitation=%s, snow=%s]",//
                         title, imgUrl, temp, feels, pop, precipitation, snow);
  }
}
