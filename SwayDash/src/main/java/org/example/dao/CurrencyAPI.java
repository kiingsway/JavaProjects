package org.example.dao;

import org.example.model.log.LogItem;
import org.example.model.log.LogItemLevel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import static org.example.Constants.*;

@SuppressWarnings("CallToPrintStackTrace")
public class CurrencyAPI {

  private static float CADBRL = 0, USDBRL = 0, USDCAD = 0;

  private static final String CADBRL_URL = "https://wise.com/us/currency-converter/cad-to-brl-rate";
  private static final String USDBRL_URL = "https://wise.com/us/currency-converter/usd-to-brl-rate";
  private static final String USDCAD_URL = "https://wise.com/us/currency-converter/usd-to-cad-rate";
  private final Consumer<LogItem> addLog;

  public CurrencyAPI(Consumer<LogItem> addLog) {
    this.addLog = addLog;

    updateValues();
    Timer timer = new Timer(15000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    new Thread(() -> {
      try {
        useJsoup();
      } catch (Exception e) {
        addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), e));
      }
    }).start();
  }

  private void useJsoup() throws IOException {
    String[] urls = {CADBRL_URL, USDBRL_URL, USDCAD_URL};

    for (String url : urls) {
      Document doc = Jsoup.connect(url).get();
      float value = Optional.ofNullable(getFloatOfTargetInput(doc)).orElse(0.0f);

      switch (url) {
        case CADBRL_URL:
          CADBRL = value;
          break;
        case USDBRL_URL:
          USDBRL = value;
          break;
        case USDCAD_URL:
          USDCAD = value;
          break;
        default:
          throw new RuntimeException("Unknown URL: " + url);
      }
    }
  }


  private static Float getFloatOfTargetInput(Document doc) {
    Element element = doc.selectFirst("#" + "target-input");
    if (element == null) return null;
    return Float.parseFloat(element.val().replace(",", "")) / 1000.0f;
  }

  public float CADBRL() {return CADBRL;}

  public float USDBRL() {return USDBRL;}

  public float USDCAD() {return USDCAD;}
}
