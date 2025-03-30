package org.example.dao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;

import java.io.IOException;
import java.util.Optional;

import static org.example.Constants.*;

@SuppressWarnings("CallToPrintStackTrace")
public class CurrencyAPI {

  private static float CADBRL = 0, USDBRL = 0, USDCAD = 0;

  private static final String CADBRL_URL = "https://wise.com/us/currency-converter/cad-to-brl-rate";
  private static final String USDBRL_URL = "https://wise.com/us/currency-converter/usd-to-brl-rate";
  private static final String USDCAD_URL = "https://wise.com/us/currency-converter/usd-to-cad-rate";

  public CurrencyAPI() {
    updateValues();
    Timer timer = new Timer(15000, _ -> updateValues());
    timer.start();
  }

  private void updateValues() {
    new Thread(() -> {
      try {
        useJsoup();
      } catch (Exception e) {
        e.printStackTrace();
        SHOW_ERROR_DIALOG(null, e);
      }
    }).start();
  }

  private void useJsoup() {
    try {
      Document doc = Jsoup.connect(CADBRL_URL).get();
      CADBRL = Optional.ofNullable(getFloatOfTargetInput(doc)).orElse(0.0f);
      doc = Jsoup.connect(USDBRL_URL).get();
      USDBRL = Optional.ofNullable(getFloatOfTargetInput(doc)).orElse(0.0f);
      doc = Jsoup.connect(USDCAD_URL).get();
      USDCAD = Optional.ofNullable(getFloatOfTargetInput(doc)).orElse(0.0f);
    } catch (NullPointerException | IOException e) {
      SHOW_ERROR_DIALOG(null, e);
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
