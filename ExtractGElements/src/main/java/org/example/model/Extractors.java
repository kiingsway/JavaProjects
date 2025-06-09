package org.example.model;

import org.example.Constants;
import org.example.model.records.UpdateProcess;
import org.example.model.records.UpdateStatus;
import org.example.view.components.ProductListPanel;
import org.openqa.selenium.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URI;

public class Extractors {

    private static final String host = "https://valuebuds.com";
    private static final String urlArgs = "?filter.p.m.display.province=Ontario&filter.v.availability=1&filter.v.option.location=70358794407";
    public static final String url_pr = host + "/collections/pre-rolls" + urlArgs;
    public static final String url_df = host + "/collections/dried-flower" + urlArgs;

    public static void getCollections(String url, ProductListPanel productTable, WebDriver d, Runnable onFinished) {

        Matcher matcher = Pattern.compile("/collections/([^/?#]+)").matcher(url);
        String categoryName = matcher.find() ? matcher.group(1) : "unknown";

        // Primeira mensagem de status (direto na thread da chamada)
        productTable.setStatus("Initializing " + categoryName + " extraction...");

        SwingWorker<Void, UpdateProcess> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() {
                WebDriver driver = d != null ? d : Constants.GET_NEW_WEBDRIVER();
                int totalPages = Extractors.getTotalPages(driver, url);
                boolean hasNextPage;
                int page = 1;

                do {
                    int finalPage = page;
                    String statusMsg = String.format("Extracting %s - Page: %d of %d...", categoryName, finalPage, totalPages);
                    publish(new UpdateProcess(statusMsg, null));

                    driver.get(url + "&page=" + page);
                    clickEnterSite(driver);

                    List<GProduct> products;
                    try {
                        products = getProductsJSON(driver);
                    } catch (Exception e) {
                        products = getProductsElements(driver);
                    }

                    publish(new UpdateProcess(null, products));

                    hasNextPage = hasNextPage(driver);
                    page++;
                } while (hasNextPage);

                publish(new UpdateProcess("", null)); // limpa o status
                if (d == null) driver.quit();
                return null;
            }

            @Override
            protected void process(List<UpdateProcess> updates) {
                for (UpdateProcess update : updates) {
                    if (update.message() != null)
                        productTable.setStatus(update.message());

                    if (update.products() != null)
                        productTable.addProducts(update.products());
                }
            }

            @Override
            protected void done() {
                if (onFinished != null) onFinished.run();
            }
        };

        worker.execute();
    }

    private static List<GProduct> getProductsJSON(WebDriver driver) {
        String html = driver.getPageSource();
        Pattern pattern = Pattern.compile("webPixelsManagerAPI\\.publish\\(\"collection_viewed\",\\s*(\\{.*?})\\s*\\);", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(Objects.requireNonNullElse(html, ""));

        if (matcher.find()) {
            String json = matcher.group(1);
            return parseProducts(json);
        } else {
            String htmlSafe = Objects.requireNonNullElse(html, "");
            String url = driver.getCurrentUrl();
            String title = "ERROR - JSON Products not found";

            boolean hasHtml = !htmlSafe.isBlank();
            boolean hasUrl = url != null && !url.isBlank();

            String message = getMessage(hasHtml, hasUrl);

            int resp = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                try {
                    if (hasHtml) {
                        Toolkit.getDefaultToolkit()
                                .getSystemClipboard()
                                .setContents(new StringSelection(htmlSafe), null);
                    }

                    if (hasUrl) Desktop.getDesktop().browse(new URI(url));

                } catch (Exception e) {
                    Constants.SHOW_ERROR_DIALOG(e);
                }
            }

            return null;
        }
    }

    private static String getMessage(boolean hasHtml, boolean hasUrl) {
        StringBuilder messageBuilder = new StringBuilder("JSON from products not found!\n");

        if (hasHtml && hasUrl) {
            messageBuilder.append("Clicking 'Yes' will copy the HTML and open the link.\n");
        } else if (hasHtml) {
            messageBuilder.append("Clicking 'Yes' will copy the HTML, but no link will be opened.\n");
        } else if (hasUrl) {
            messageBuilder.append("Clicking 'Yes' will open the link, but HTML is not available.\n");
        } else {
            messageBuilder.append("No HTML or URL available.\n");
        }

        messageBuilder.append("Clicking 'No' closes this message. Continue?");
        return messageBuilder.toString();
    }

    private static List<GProduct> getProductsElements(WebDriver driver) {
        return new ArrayList<>();
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
        } catch (Exception e) {
            Constants.SHOW_ERROR_DIALOG(e);
            throw new RuntimeException(e);
        }
    }

    public static int getTotalPages(WebDriver driver, String url) {
        driver.get(url);
        // Encontra todos os links de páginas na navegação (sem excluir a página atual)
        List<WebElement> pageLinks = driver.findElements(By.cssSelector("nav[role='navigation'] a[aria-label^='Page'], nav[role='navigation'] li[aria-current='page']"));

        int maxPage = 0;

        // Itera sobre os links encontrados e pega o número de página
        for (WebElement link : pageLinks) {
            String pageText = link.getText();

            try {
                // Converte o texto da página para número inteiro
                int pageNumber = Integer.parseInt(pageText);
                maxPage = Math.max(maxPage, pageNumber);  // Atualiza o máximo de páginas
            } catch (NumberFormatException _) {
            }
        }

        return maxPage;
    }

    public static void getProductsInfo(ProductListPanel productTable) {
        List<GProduct> products = productTable.getProducts();
        if (products == null || products.isEmpty()) return;

        new SwingWorker<Void, UpdateStatus>() {
            @Override
            protected Void doInBackground() {
                WebDriver driver = Constants.GET_NEW_WEBDRIVER();
                int total = products.size();
                int current = 1;

                for (GProduct product : products) {
                    Extractors.getProductInfo(product, driver);

                    String msg = String.format("Processing %d of %d...", current, total);
                    publish(new UpdateStatus(msg, product));
                    current++;
                }

                driver.quit();
                return null;
            }

            @Override
            protected void process(List<UpdateStatus> chunks) {
                UpdateStatus latest = chunks.getLast();
                productTable.setStatus(latest.message());
                productTable.updateSingleProduct(latest.product());
            }

            @Override
            protected void done() {
                productTable.setStatus("");
            }
        }.execute();

    }

    public static void getProductInfo(GProduct product, WebDriver driver) {
        driver.get(product.url());

        try {
            provinceAndStoreSelect(driver);

            product.description(driver.findElement(By.cssSelector("meta[name='description']")).getDomAttribute("content"));
            product.cultivator(driver.findElement(By.xpath("//li[span[text()='Cultivator:']]/span[2]")).getText());
            product.effect(driver.findElement(By.xpath("//li[span[text()='Dominant effect:']]/span[2]")).getText());
            product.thcMin(driver.findElement(By.cssSelector("#thc_range span:nth-child(2)")).getText());
            product.thcMax(driver.findElement(By.cssSelector("#thc_range span:nth-child(4)")).getText());
            product.thcUnit(driver.findElement(By.cssSelector("#thc_range span:nth-child(5)")).getText());
            product.cbdMin(driver.findElement(By.cssSelector("#cbd_range span:nth-child(2)")).getText());
            product.cbdMax(driver.findElement(By.cssSelector("#cbd_range span:nth-child(4)")).getText());
            product.cbdUnit(driver.findElement(By.cssSelector("#cbd_range span:nth-child(5)")).getText());
        } catch (NoSuchElementException e) {
            String message = e.getMessage();

            if (message == null) Constants.SHOW_ERROR_DIALOG(e);
            if (message == null || message.contains("meta[name='description']")) return;

            Pattern pattern = Pattern.compile("selector\":\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(message);
            String selector = matcher.find() ? matcher.group(1) : "UNKNOWN_SELECTOR";
            System.out.printf("NoSuchElementException: %s%n", selector);
        } catch (Exception e) {
            Constants.SHOW_ERROR_DIALOG(e);
        }
    }

    private static void provinceAndStoreSelect(WebDriver driver) {
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
