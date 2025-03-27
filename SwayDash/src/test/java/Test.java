import org.example.dao.WeatherAPI;

import java.util.Arrays;

@SuppressWarnings("CallToPrintStackTrace")
public class Test {

  public Test() {

    try {

      String[] weathers = {WeatherAPI.TOCA_WEATHER, WeatherAPI.SPBR_WEATHER, WeatherAPI.NUGL_WEATHER};

      for (String api : Arrays.stream(weathers).toList().subList(0, 1)) {
        WeatherAPI weather = new WeatherAPI(api);
        System.out.println("--------------------------------------");
        System.out.println(weather);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @SuppressWarnings("InstantiationOfUtilityClass")
  public static void main(String[] args) {
    new Test();
  }
}
