package org.example.model.weather;

public class WeatherModel {

  private String uri;
  private String url;
  private String city;
  private String status;
  private Integer temperature;
  private TemperatureRangeModel temperatureRange;
  private Integer feelsLike;
  private ForecastModel hourlyForecast;
  private ForecastModel dailyForecast;
  private SolarCycleModel solarCycle;

  public WeatherModel() {
    this.uri = null;
    this.url = null;
    this.city = null;
    this.status = null;
    this.temperature = null;
    this.temperatureRange = null;
    this.feelsLike = null;
    this.hourlyForecast = null;
    this.dailyForecast = null;
    this.solarCycle = null;
  }

  public WeatherModel(String uri, String city, String status, Integer temperature, TemperatureRangeModel temperatureRange, Integer feelsLike, ForecastModel hourlyForecast, ForecastModel dailyForecast, SolarCycleModel solarCycle) {
    this.uri = uri;
    this.url = "https://www.theweathernetwork.com/en/city/" + uri + "/current";
    this.city = city;
    this.status = status;
    this.temperature = temperature;
    this.temperatureRange = temperatureRange;
    this.feelsLike = feelsLike;
    this.hourlyForecast = hourlyForecast;
    this.dailyForecast = dailyForecast;
    this.solarCycle = solarCycle;
  }

  public void setUri(String uri) {
    this.uri = uri;
    this.url = "https://www.theweathernetwork.com/en/city/" + uri + "/current";
  }

  public String url() {return url;}

  public String city() {return city;}

  public void setCity(String city) {this.city = city;}

  public String status() {return status;}

  public void setStatus(String status) {this.status = status;}

  public Integer temperature() {return temperature;}

  public void setTemperature(Integer temperature) {this.temperature = temperature;}

  public TemperatureRangeModel temperatureRange() {return temperatureRange;}

  public void setTemperatureRange(TemperatureRangeModel temperatureRange) {this.temperatureRange = temperatureRange;}

  public Integer feelsLike() {return feelsLike;}

  public void setFeelsLike(Integer feelsLike) {this.feelsLike = feelsLike;}

  public ForecastModel hourlyForecast() {return hourlyForecast;}

  public void setHourlyForecast(ForecastModel forecast) {this.hourlyForecast = forecast;}

  public ForecastModel dailyForecast() {return dailyForecast;}

  public void setDailyForecast(ForecastModel forecast) {this.dailyForecast = forecast;}

  public SolarCycleModel solarCycle() {return solarCycle;}

  public void setSolarCycle(SolarCycleModel solarCycle) {this.solarCycle = solarCycle;}

  @Override
  public String toString() {
    return "WeatherData {\n" + //
            "  uri='" + uri + "',\n" + //
            "  city='" + city + "',\n" + //
            "  status='" + status + "',\n" + //
            "  temperature=" + temperature + "°C,\n" + //
            "  feelsLike=" + feelsLike + "°C,\n" + //
            "  tempRange=" + temperatureRange + ",\n" + //
            "  sunsetSunrise=" + solarCycle + ",\n" + //
            "  hourlyForecast=" + hourlyForecast + ",\n" + //
            "  dailyForecast=" + dailyForecast + "\n" + //
            '}';
  }
}