package org.example.model;

import org.example.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GProduct {

    private final String productId;
    private String title;
    private String type;
    private final BigDecimal price;
    private String brand;
    private String url;
    private String description;
    private String cultivator;
    private String effect;
    private BigDecimal thcMin;
    private BigDecimal thcMax;
    private String thcUnit;
    private BigDecimal cbdMin;
    private BigDecimal cbdMax;
    private String cbdUnit;
    private int nItems;
    private BigDecimal itemSize;
    private BigDecimal totalSize;
    private BigDecimal costBenefit;

    // Construtor padrão
    public GProduct(String title, String type, BigDecimal price, String brand, String productId, String url) {
        this.title = title;
        this.type = type;
        this.price = price;
        this.brand = brand;
        this.productId = productId;
        this.url = url;
        this.thcMin = BigDecimal.ZERO;
        this.thcMax = BigDecimal.ZERO;
        this.cbdMin = BigDecimal.ZERO;
        this.cbdMax = BigDecimal.ZERO;

        extractWeightInfo();
    }

    public String title() {return title;}

    public void title(String title) {
        this.title = title;
        extractWeightInfo();
    }

    public String type() {return type;}

    public void type(String type) {this.type = type;}

    public BigDecimal price() {return price;}

    public String brand() {return brand;}

    public void brand(String brand) {this.brand = brand;}

    public String productId() {return productId;}

    public String url() {return url;}

    public void url(String url) {this.url = url;}

    public String description() {return description;}

    public void description(String description) {this.description = description;}

    public String cultivator() {return cultivator;}

    public void cultivator(String cultivator) {this.cultivator = cultivator;}

    public String effect() {return effect;}

    public void effect(String effect) {this.effect = effect;}

    public String thcPercentage() {
        BigDecimal thcMinPercentage = thcMin.divide(BigDecimal.TEN, 0, RoundingMode.HALF_UP);
        BigDecimal thcMaxPercentage = thcMax.divide(BigDecimal.TEN, 0, RoundingMode.HALF_UP);
        return thcMinPercentage.equals(thcMaxPercentage) ? thcMinPercentage + "%" : thcMinPercentage + "% - " + thcMaxPercentage + "%";
    }

    public BigDecimal thcPercentageMini() {
        BigDecimal average = thcMin.add(thcMax).divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP);
        return average.divide(BigDecimal.TEN, 0, RoundingMode.HALF_UP);
    }

    public BigDecimal thcCostBenefit() {
        if (price == null || price.compareTo(BigDecimal.ZERO) == 0 ||
                totalSize == null || totalSize.compareTo(BigDecimal.ZERO) == 0 ||
                thcPercentageMini() == null) {
            return BigDecimal.ZERO;
        }

        try {
            BigDecimal pricePerGram = price.divide(totalSize, 4, RoundingMode.HALF_UP);
            BigDecimal denominator = thcPercentageMini().add(BigDecimal.ONE);
            if (denominator.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO; // segurança extra
            return pricePerGram.divide(denominator, 4, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            return BigDecimal.ZERO;
        }
    }


    public BigDecimal thcMin() {return thcMin;}

    public void thcMin(String thcMin) {this.thcMin = Constants.STRING_TO_BIGDECIMAL(thcMin);}

    public BigDecimal thcMax() {return thcMax;}

    public void thcMax(String thcMax) {this.thcMax = Constants.STRING_TO_BIGDECIMAL(thcMax);}

    public String thcUnit() {return thcUnit;}

    public void thcUnit(String thcUnit) {this.thcUnit = thcUnit;}

    public String cbdPercentage() {
        BigDecimal cbdMinPercentage = cbdMin.divide(BigDecimal.TEN, 0, RoundingMode.HALF_UP);
        BigDecimal cbdMaxPercentage = cbdMax.divide(BigDecimal.TEN, 0, RoundingMode.HALF_UP);
        return cbdMinPercentage.equals(cbdMaxPercentage) ? cbdMinPercentage + "%" : cbdMinPercentage + "% - " + cbdMaxPercentage + "%";
    }

    public BigDecimal cbdMin() {return cbdMin;}

    public void cbdMin(String cbdMin) {this.cbdMin = Constants.STRING_TO_BIGDECIMAL(cbdMin);}

    public BigDecimal cbdMax() {return cbdMax;}

    public void cbdMax(String cbdMax) {this.cbdMax = Constants.STRING_TO_BIGDECIMAL(cbdMax);}

    public String cbdUnit() {return cbdUnit;}

    public void cbdUnit(String cbdUnit) {this.cbdUnit = cbdUnit;}

    public int nItems() {return nItems;}

    public BigDecimal itemSize() {return itemSize;}

    public BigDecimal totalSize() {return totalSize;}

    public BigDecimal costBenefit() {return costBenefit;}

    public void extractWeightInfo() {
        // Primeiro tenta padrão com G (ex: "1G", "0.5G x 3")
        Pattern patternG = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*G(?:\\s*[xX]\\s*(\\d+))?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternG.matcher(title());

        BigDecimal itemSize = BigDecimal.ZERO;
        int nItems = 1;

        if (matcher.find()) {
            itemSize = new BigDecimal(matcher.group(1));
            if (matcher.group(2) != null) nItems = Integer.parseInt(matcher.group(2));
        } else {
            // Se não encontrar "G", tenta padrão como "0.5 x 3"
            Pattern patternX = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*[xX]\\s*(\\d+)");
            matcher = patternX.matcher(title());
            if (matcher.find()) {
                itemSize = new BigDecimal(matcher.group(1));
                nItems = Integer.parseInt(matcher.group(2));
            }
        }

        this.nItems = nItems;
        this.itemSize = itemSize;
        this.totalSize = itemSize.multiply(BigDecimal.valueOf(nItems));

        try {
            costBenefit = price.divide(totalSize, 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            String msg = "Não foi possível realizar o cálculo: " + price + " / " + totalSize + ": \n" + e.getMessage();
            Constants.SHOW_ERROR_DIALOG(new ArithmeticException(msg));
            costBenefit = BigDecimal.ZERO;
        }
    }


    @Override
    public String toString() {
        return """
                GProduct {
                    title='%s',
                    type='%s',
                    price=%s,
                    brand='%s',
                    productId='%s',
                    url='%s',
                    description='%s',
                    cultivator='%s',
                    effect='%s',
                    thcMin='%s',
                    thcMax='%s',
                    thcUnit='%s',
                    cbdMin='%s',
                    cbdMax='%s',
                    cbdUnit='%s'
                }
                """.formatted(title, type, price, brand, productId, url, description, cultivator, effect, thcMin, thcMax, thcUnit, cbdMin, cbdMax, cbdUnit);
    }
}
