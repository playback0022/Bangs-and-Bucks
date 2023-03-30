package BankingProducts;

import java.util.Objects;

public class Currency {
    private final String isoCode;
    private final Double dollarConversionFactor;
    private static int numberOfCurrencies = 0;
    private static Currency[] currencies = new Currency[100];

    private Currency(String isoCode, Double dollarConversionFactor) {
        this.isoCode = isoCode;
        this.dollarConversionFactor = dollarConversionFactor;
    }

    @Override
    public String toString() {
        return isoCode;
    }

    public static void buildCurrency(String isoCode, Double dollarConversionFactor) {
        for (Currency currency : currencies)
            if (currency!=null && (Objects.equals(currency.isoCode, isoCode))) {
                System.out.println("Currencies must be unique.");
                return;
            }

//        "[A-Z]{3}"
//            System.out.println("ISO Code must be three letters long and can only contain uppercase alphabetical characters.");

//        if (dollarConversionFactor <= 0)
//            System.out.println("The dollar conversion factor must be strictly positive.");

//        for (int i = 0; i<numberOfCurrencies; i++)
//            if (currencies[i] == null) {
//                currencies[i] = new Currency(isoCode, dollarConversionFactor);
//                return;
//            }

        currencies[numberOfCurrencies++] = new Currency(isoCode, dollarConversionFactor);
    }

    public static void removeCurrency(String isoCode) {
//        if (id < 0 || id >= numberOfCurrencies)
//            System.out.println("Index out of bounds.");

        for (int i=0; i<numberOfCurrencies-1; i++)
            if (currencies[i].isoCode == isoCode)
                for (int j = i; j<numberOfCurrencies - 1; j++)
                    currencies[j] = currencies[j+1];
    }

    public static Currency getCurrency(int id) {
        return currencies[id];
    }

    public static Currency[] getAllCurrencies() {
        return currencies;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public Double getDollarConversionFactor() {
        return dollarConversionFactor;
    }
}
