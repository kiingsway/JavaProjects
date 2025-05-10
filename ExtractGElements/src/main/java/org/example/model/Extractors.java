package org.example.model;

import org.example.Constants;
import org.openqa.selenium.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractors {

    private static final String host = "https://valuebuds.com";

    public static ExtractorData pr(WebDriver driver, int page) {
        String url = host + "/collections/pre-rolls?filter.p.m.display.province=Ontario&filter.v.availability=1&filter.v.option.location=70358794407&page=" + page;
        return extractData(driver, url);
    }

    public static ExtractorData df(WebDriver driver, int page) {
        String url = host + "/collections/dried-flower?filter.p.m.display.province=Ontario&filter.v.availability=1&filter.v.option.location=70358794407&page=" + page;
        return extractData(driver, url);
    }

    private static ExtractorData extractData(WebDriver driver, String url) {
        driver.get(url);
        clickEnterSite(driver);
        return new ExtractorData(getProductsJSON(driver.getPageSource()), hasNextPage(driver));
    }

    private static List<GProduct> getProductsJSON(String html) {
        Pattern pattern = Pattern.compile("webPixelsManagerAPI\\.publish\\(\"collection_viewed\",\\s*(\\{.*?})\\s*\\);", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            String json = matcher.group(1);
            return parseProducts(json);
        } else {
            System.out.println("JSON não encontrado.");
            return null;
        }
    }

    public static List<GProduct> parseProducts(String jsonString) {
        List<GProduct> products = new ArrayList<>();

        JSONObject json = new JSONObject(jsonString);
        JSONObject collection = json.getJSONObject("collection");
        String type = collection.getString("title");

        JSONArray productVariants = collection.getJSONArray("productVariants");

        for (int i = 0; i < productVariants.length(); i++) {
            JSONObject variant = productVariants.getJSONObject(i);
            BigDecimal price = variant.getJSONObject("price").getBigDecimal("amount");
            JSONObject product = variant.getJSONObject("product");

            String title = product.getString("title");
            String brand = product.getString("vendor");
            String productId = product.getString("id");
            String url = product.getString("url");
            url = url.startsWith("http") ? url : host + url;

            GProduct gProduct = new GProduct(title, type, price, brand, productId, url);
            products.add(gProduct);
        }

        return products;
    }

    private static void clickEnterSite(WebDriver driver) {
        String xpath = "//button[normalize-space(text())='Enter Site']";
        List<WebElement> buttons = driver.findElements(By.xpath(xpath));
        if (!buttons.isEmpty()) buttons.getFirst().click();
    }

    private static boolean hasNextPage(WebDriver driver) {
        try {
            WebElement nextPageElement = driver.findElement(By.cssSelector("[aria-label='Next page']"));
            return nextPageElement.getTagName().equalsIgnoreCase("a");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void getProductInfo(GProduct product, WebDriver driver) {
        driver.get(product.url());

        try {
            provinceNstoreSelect(driver);

            product.description(driver.findElement(By.cssSelector("meta[name='description']")).getDomAttribute("content"));
            product.cultivator(driver.findElement(By.xpath("//li[span[text()='Cultivator:']]/span[2]")).getText());
            product.effect(driver.findElement(By.xpath("//li[span[text()='Dominant effect:']]/span[2]")).getText());
            product.thcMin(driver.findElement(By.cssSelector("#thc_range span:nth-child(2)")).getText());
            product.thcMax(driver.findElement(By.cssSelector("#thc_range span:nth-child(4)")).getText());
            product.thcUnit(driver.findElement(By.cssSelector("#thc_range span:nth-child(5)")).getText());
            product.cbdMin(driver.findElement(By.cssSelector("#cbd_range span:nth-child(2)")).getText());
            product.cbdMax(driver.findElement(By.cssSelector("#cbd_range span:nth-child(4)")).getText());
            product.cbdUnit(driver.findElement(By.cssSelector("#cbd_range span:nth-child(5)")).getText());
        } catch (NoSuchElementException _) {
        } catch (Exception e) {
            e.printStackTrace();
            Constants.SHOW_ERROR_DIALOG(e);
        }
    }

    private static void provinceNstoreSelect(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        boolean hasProvinceToSelect = !driver.findElements(By.id("province_select")).isEmpty();
        boolean hasStoreToSelect = !driver.findElements(By.cssSelector("#store_list li")).isEmpty();

        if (hasProvinceToSelect) {
            // Espera o select aparecer e escolhe "Ontario"
            WebElement provinceSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("province_select")));
            Select select = new Select(provinceSelect);
            select.selectByValue("Ontario");

            // Espera e clica no botão "Enter Site"
            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[x-show=\"selectedProvince !== null\"] > button")));
            enterButton.click();

            // Espera o botão desaparecer (ou outra condição adequada)
            wait.until(ExpectedConditions.invisibilityOf(enterButton));
        }

        if (hasStoreToSelect) {
            // Procura o li que contém "Bloor Street"
            List<WebElement> listItems = driver.findElements(By.cssSelector("#store_list li"));
            for (WebElement li : listItems) {
                WebElement nameDiv;
                try {
                    nameDiv = li.findElement(By.cssSelector("div.font-semibold.text-lg.leading-none.mb-1"));
                } catch (NoSuchElementException e) {
                    continue;
                }

                if (nameDiv.getText().contains("Bloor Street")) {
                    WebElement button = li.findElement(By.cssSelector("button[type='submit']"));

                    // Scroll até o botão
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

                    // Espera estar clicável
                    wait.until(ExpectedConditions.elementToBeClickable(button));

                    try {
                        button.click();
                    } catch (ElementClickInterceptedException e) {
                        // Fallback: clique via JavaScript
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                    }

                    break;
                }
            }
        }
    }
}
