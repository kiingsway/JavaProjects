package org.example.model.weather;

public record HourlyForecastItem(
        String title,
        String imgUrl,
        Integer temp,
        Integer feels,
        String prep
) {
  @Override
  public String toString() {
    return String.format("""
                                 ------%s------
                                 Img: %s
                                 Temp: %s
                                 Feels: %s
                                 Prep: %s
                                 """, title, imgUrl, temp, feels, prep);
  }
}
