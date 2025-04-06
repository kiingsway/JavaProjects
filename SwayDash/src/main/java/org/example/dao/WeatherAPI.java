package org.example.dao;

import org.example.Constants;
import org.example.model.weather.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import javax.swing.*;
import javax.swing.Timer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static org.example.Constants.*;


@SuppressWarnings("CallToPrintStackTrace")
public class WeatherAPI {

  public static final String TOCA_WEATHER = "ca/ontario/toronto";
  public static final String SPBR_WEATHER = "br/sao-paulo/sao-paulo";
  public static final String NUGL_WEATHER = "gl/sermersooq/nuuk";
  public static final String NORU_WEATHER = "ru/krasnoyarskiy/norilsk";

  private static final SimpleDateFormat MDY_FORMAT = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
  private static final SimpleDateFormat YMD_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
  private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd h:mma", Locale.ENGLISH);

  private final WeatherModel weather = new WeatherModel();

  private Runnable onCityUpdate;

  public WeatherAPI(String cityUri) {
    weather.setUri(cityUri);

    updateValues();
    Timer timer = new Timer(15000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    new Thread(() -> {
      try {
        useJsoup();
        if (onCityUpdate != null) SwingUtilities.invokeLater(onCityUpdate);
        //useSelenium();
        // if (onCityUpdate != null) SwingUtilities.invokeLater(onCityUpdate);
      } catch (Exception e) {
        e.printStackTrace();
        SHOW_ERROR_DIALOG(null, e);
      }
    }).start();
  }

  private void useJsoup() {
    try {
      Document doc = Jsoup.connect(url()).get();
      weather.setCity(getStringOfComponent(doc, "location-label"));
      weather.setStatus(getStringOfComponent(doc, "weather-text"));
      weather.setTemperature(getNumberOfComponent(doc, "temperature-text"));
      weather.setTemperatureRange(getTemperatureRange(doc));
      weather.setFeelsLike(getNumberOfComponent(doc, "feels-like"));
      weather.setHourlyForecast(getHourlyForecast(doc));
    } catch (NullPointerException | IOException e) {
      SHOW_ERROR_DIALOG(null, e);
    }
  }

  private void useSelenium() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // sem abrir o navegador

    WebDriver driver = new ChromeDriver(options);
    try {
      Constants.PRINT("driver.get(url());", true);
      driver.get(url());

      Constants.PRINT("starting updateDailyForecast...", true);
      updateDailyForecast(driver);
      Constants.PRINT("starting updateSolarCycle...", true);
      updateSolarCycle(driver);
    } finally {
      driver.quit();
    }
  }

  private static String getStringOfComponent(Document doc, String component) {
    Element element = doc.selectFirst("[data-testid=" + component + "]");
    if (element == null) return null;
    String tempString = element.text().trim();
    return tempString.isEmpty() ? null : tempString;
  }

  private static Integer getNumberOfComponent(Document doc, String component) {
    Element element = doc.selectFirst("[data-testid=" + component + "]");
    if (element == null) return null;
    return STRING_TO_INTEGER(element.text());
  }

  private static TemperatureRangeModel getTemperatureRange(Document doc) {
    Element elmTempRange = doc.selectFirst("[data-testid=high-low]");

    if (elmTempRange == null) return null;
    String[] numbers = elmTempRange.text().replaceAll("[^0-9\\- ]", "").trim().split("\\s+");
    if (numbers.length < 2) return null;

    try {
      Integer high = STRING_TO_INTEGER(numbers[0]);
      Integer low = STRING_TO_INTEGER(numbers[1]);
      return new TemperatureRangeModel(high, low);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static ForecastModel getHourlyForecast(Document doc) {
    List<ForecastItem> items = new ArrayList<>();

    Element element = doc.selectFirst("[aria-labelledby=hourly-widget-link] > div");
    if (element == null || element.children().isEmpty()) return null;

    for (Element hourlyItems : element.children()) {
      Elements elHourly = hourlyItems.children();
      String title = elHourly.getFirst().text().trim();
      String imgUrl = elHourly.get(1).children().getFirst().attr("src");
      Integer temp = STRING_TO_INTEGER(elHourly.get(2).text());
      Integer feels = STRING_TO_INTEGER(elHourly.get(3).text());
      String pop = elHourly.get(4).text().trim();

      Element elmPrecipitation = element.selectFirst("[data-testid=period-precip-container] > div");
      String rain = null, snow = null;

      if (elmPrecipitation != null) {
        Elements elmPrecipitationChildren = elmPrecipitation.children();
        if (!elmPrecipitationChildren.isEmpty()) rain = elmPrecipitationChildren.getFirst().text().trim();
        if (elmPrecipitationChildren.size() > 1) snow = elmPrecipitationChildren.get(1).text().trim();
      }

      ForecastItem item = new ForecastItem(title, imgUrl, temp, feels, pop, rain, snow, null);
      items.add(item);
    }

    return new ForecastModel(items);
  }

  private void updateSolarCycle(WebDriver driver) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=detailed-obs-tile]")));

      WebElement element = driver.findElement(By.cssSelector("[data-testid=detailed-obs-tile]"));
      String info = element.getText();
      String dateText = info.split("\n")[0].replace("Sunset/Sunrise Info:", "").trim();
      String sunriseTimeText = info.split("\n")[2].trim();
      String sunsetTimeText = info.split("\n")[4].trim();

      Date date = MDY_FORMAT.parse(dateText);

      String sunriseDateString = YMD_FORMAT.format(date) + " " + sunriseTimeText;
      String sunsetDateString = YMD_FORMAT.format(date) + " " + sunsetTimeText;

      Date sunrise = DATETIME_FORMAT.parse(sunriseDateString);
      Date sunset = DATETIME_FORMAT.parse(sunsetDateString);

      weather.setSolarCycle(new SolarCycleModel(sunrise, sunset));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateDailyForecast(WebDriver driver) {
    List<ForecastItem> items = new ArrayList<>();
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[aria-labelledby='fourteen-days-widget-link']")));

      WebElement elements = driver.findElement(By.cssSelector("[aria-labelledby='fourteen-days-widget-link'] > div"));

      List<WebElement> elementsChildren = elements.findElements(By.xpath("./*"));

      for (WebElement elm : elementsChildren) {
        String title = elm.findElement(By.cssSelector("[data-testid='14day-day-label']")).getText();
        String imgUrl = elm.findElement(By.cssSelector("[data-testid='period-weather-icon'] > img")).getAttribute("src");

        String temperatureText = elm.findElement(By.cssSelector("[data-testid='period-temperature']")).getText();
        Integer temp = Optional.of(temperatureText).map(Constants::STRING_TO_INTEGER).orElse(null);

        String pop = elm.findElement(By.cssSelector("[data-testid='period-pop-value']")).getText();
        String precipitation = elm.findElement(By.cssSelector("[data-testid='period-precip-container']")).getText();

        String nightText = elm.findElement(By.cssSelector("[data-testid='period-post-temp-label']")).getText();
        Integer night = Optional.of(nightText).map(Constants::STRING_TO_INTEGER).orElse(null);

        ForecastItem item = new ForecastItem(title, imgUrl, temp, null, pop, precipitation, null, night);
        Constants.PRINT("item: " + item, true);
        items.add(item);
      }

      weather.setDailyForecast(new ForecastModel(items));

    } catch (Exception e) {
      Constants.PRINT("Error: " + e.getMessage());
      //e.printStackTrace();
    }
  }

  private static ForecastModel getDailyForecast(Document doc) {
    List<ForecastItem> items = new ArrayList<>();

    Element element = doc.selectFirst("#fourteen-days-widget-link > div > div");
    if (element == null || element.children().isEmpty()) return null;
    for (Element dailyDivs : element.children()) {
      Elements elements = dailyDivs.children();
    }

    return new ForecastModel(items);
  }

  public void setCity(String cityUri, Runnable onCityUpdate) {
    this.onCityUpdate = onCityUpdate;
    weather.setUri(cityUri);
    updateValues();
  }

  public String city() {return weather.city();}

  public String url() {return weather.url();}

  public String status() {return weather.status();}

  public Integer temperature() {return weather.temperature();}

  public TemperatureRangeModel tempRange() {return weather.temperatureRange();}

  public Integer feelsLike() {return weather.feelsLike();}

  public ForecastModel hourlyForecast() {return weather.hourlyForecast();}

  public ForecastModel dailyForecast() {return weather.dailyForecast();}

  public SolarCycleModel sunsetSunrise() {return weather.solarCycle();}

  @Override
  public String toString() {
    return "WeatherAPI {" +  //
            "\n  url='" + url() + '\'' +  //
            ",\n  city='" + city() + '\'' +  //
            ",\n  status='" + status() + '\'' +  //
            ",\n  temperature=" + temperature() + "°C" +  //
            ",\n  highLow=" + tempRange() +  //
            ",\n  feelsLike=" + feelsLike() + "°C" +  //
            ",\n  hourlyForecast=" + hourlyForecast() +  //
            ",\n  sunsetSunrise=" + sunsetSunrise() +  //
            "\n}";  //
  }
}
