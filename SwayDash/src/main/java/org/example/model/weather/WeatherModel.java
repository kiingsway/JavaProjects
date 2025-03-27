package org.example.model.weather;

public record WeatherModel(
        String city,
        String status,
        Integer temp,
        HighLowWeatherModel highLow,
        Integer feelsLike,
        SunsetSunriseModel sunsetSunrise,
        HourlyForecastModel hourlyForecast
) {
  @Override
  public String toString() {
    return String.format("""
                                 City: %s
                                 Status: %s
                                 Temp: %dºC
                                 High Low: %s
                                 Feels like: %sºC
                                 Sunset Sunrise: %s
                                 Hourly Forecast: %s""", city, status, temp, highLow, feelsLike, sunsetSunrise, hourlyForecast);
  }
}
