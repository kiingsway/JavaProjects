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
    return String.format("{title='%s', temp=%s°C, feels=%s°C, prep='%s', imgUrl='%s'}", //
                         title != null ? title : "N/A", //
                         temp != null ? temp : "N/A", //
                         feels != null ? feels : "N/A", //
                         prep != null ? prep : "N/A", //
                         imgUrl != null ? imgUrl : "N/A"); //
  }
}
