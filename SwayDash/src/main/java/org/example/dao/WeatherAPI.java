package org.example.dao;

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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.example.Constants.SHOW_ERROR_DIALOG;
import static org.example.Constants.STRING_TO_INTEGER;


@SuppressWarnings("CallToPrintStackTrace")
public class WeatherAPI {

  public static final String TOCA_WEATHER = "ca/ontario/toronto";
  public static final String SPBR_WEATHER = "br/sao-paulo/sao-paulo";
  public static final String NUGL_WEATHER = "gl/sermersooq/nuuk";

  private static final SimpleDateFormat MDY_FORMAT = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
  private static final SimpleDateFormat YMD_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
  private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd h:mma", Locale.ENGLISH);

  private final String url;
  private String city;
  private String status;
  private Integer temp;
  private HighLowWeatherModel highLow;
  private Integer feelsLike;
  private HourlyForecastModel hourlyForecast;
  private SunsetSunriseModel sunsetSunrise;

  public WeatherAPI(String cityUrl) {
    url = "https://www.theweathernetwork.com/en/city/" + cityUrl + "/current";

    updateValues();

    Timer timer = new Timer(15000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    new Thread(() -> {
      try {
        useJsoup();
        useSelenium();
      } catch (Exception e) {
        e.printStackTrace();
        SHOW_ERROR_DIALOG(null, e);
      }
    }).start();
  }

  private void useJsoup() {
    try {
      Document doc = Jsoup.connect(url).get();
      this.city = getStringOfComponent(doc, "location-label");
      this.status = getStringOfComponent(doc, "weather-text");
      this.temp = getNumberOfComponent(doc, "temperature-text");
      this.highLow = getHighLow(doc);
      this.feelsLike = getNumberOfComponent(doc, "feels-like");
      this.hourlyForecast = getHourlyForecast(doc);
    } catch (NullPointerException | IOException e) {
      SHOW_ERROR_DIALOG(null, e);
    }
  }

  private void useSelenium() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // sem abrir o navegador

    WebDriver driver = new ChromeDriver(options);
    try {
      driver.get(url);

      // Esperar que o elemento de sunset/sunrise esteja presente na página
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid=detailed-obs-tile]")));

      sunsetSunrise = getSunsetSunrise(driver);
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

  private static HighLowWeatherModel getHighLow(Document doc) {
    Element elemHighLow = doc.selectFirst("[data-testid=high-low]");

    if (elemHighLow == null) return null;
    String[] numbers = elemHighLow.text().replaceAll("[^0-9\\- ]", "").trim().split("\\s+");
    if (numbers.length < 2) return null;

    try {
      Integer high = STRING_TO_INTEGER(numbers[0]);
      Integer low = STRING_TO_INTEGER(numbers[1]);
      return new HighLowWeatherModel(high, low);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static SunsetSunriseModel getSunsetSunrise(WebDriver driver) {
    try {
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

      // Retornar um objeto SunsetSunriseModel com as datas
      return new SunsetSunriseModel(sunrise, sunset);

    } catch (Exception e) {
      e.printStackTrace();
      return null; // Caso algo dê errado
    }
  }

  private static HourlyForecastModel getHourlyForecast(Document doc) {
    List<HourlyForecastItem> items = new ArrayList<>();
    Element element = doc.selectFirst("[aria-labelledby=hourly-widget-link] > div");
    if (element == null) return null;

    if (element.children().isEmpty()) return null;
    for (Element hourlyItems : element.children()) {
      Elements elHourly = hourlyItems.children();
      String title = elHourly.getFirst().text().trim();
      String imgUrl = elHourly.get(1).children().getFirst().attr("src");
      Integer temp = STRING_TO_INTEGER(elHourly.get(2).text());
      Integer feels = STRING_TO_INTEGER(elHourly.get(3).text());
      String prep = elHourly.get(4).text().trim();
      HourlyForecastItem item = new HourlyForecastItem(title, imgUrl, temp, feels, prep);
      items.add(item);
    }

    return new HourlyForecastModel(items);
  }

  public String city() {return city;}

  public String status() {return status;}

  public Integer temp() {return temp;}

  public HighLowWeatherModel highLow() {return highLow;}

  public Integer feelsLike() {return feelsLike;}

  public HourlyForecastModel hourlyForecast() {return hourlyForecast;}

  public SunsetSunriseModel sunsetSunrise() {return sunsetSunrise;}
}
