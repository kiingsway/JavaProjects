package org.example.dao;

import org.example.model.log.LogItem;
import org.example.model.log.LogItemLevel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;

import java.util.Optional;
import java.util.function.Consumer;

public class CurrencyAPI {

    private static float CADBRL = 0, USDBRL = 0, USDCAD = 0;

    private static final String BASE_URL = "https://wise.com/us/currency-converter";

    private static final String CADBRL_URL = BASE_URL + "/cad-to-brl-rate";
    private static final String USDBRL_URL = BASE_URL + "/usd-to-brl-rate";
    private static final String USDCAD_URL = BASE_URL + "/usd-to-cad-rate";
    private final Consumer<LogItem> addLog;

    public CurrencyAPI(Consumer<LogItem> addLog) {
        this.addLog = addLog;

        updateValues();
        Timer timer = new Timer(15000, e -> updateValues());
        timer.start();
    }

    private void updateValues() {
        new Thread(this::useJsoup).start();
    }

    private void useJsoup() {
        try {
            String[] urls = {
                    getUrl("cad", "brl"),
                    getUrl("usd", "brl"),
                    getUrl("usd", "cad")
            };

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
        } catch (Exception e) {
            addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), e));
        }
    }

    private String getUrl(String currency1, String currency2) {
        return String.format("%s/%s-to-%s-rate", BASE_URL, currency1.toLowerCase(), currency2.toLowerCase());
    }


    private static Float getFloatOfTargetInput(Document doc) {
        Element element = doc.selectFirst("#" + "target-input");
        if (element == null) return null;
        return Float.parseFloat(element.val().replace(",", "")) / 1000.0f;
    }

    public float CADBRL() {
        return CADBRL;
    }

    public float USDBRL() {
        return USDBRL;
    }

    public float USDCAD() {
        return USDCAD;
    }
}
