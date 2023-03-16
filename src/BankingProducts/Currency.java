package BankingProducts;

import java.util.Objects;
import java.util.regex.*;

public class Currency {
    private final int id;
    private final String state;
    private final String fullName;
    private final String isoCode;
    private final Double dollarConversionFactor;
    private static int numberOfCurrencies = 0;
    private static Currency[] currencies = new Currency[100];

    private Currency(String state, String fullName, String isoCode, Double dollarConversionFactor) {
        id = numberOfCurrencies++;
        this.state = state;
        this.fullName = fullName;
        this.isoCode = isoCode;
        this.dollarConversionFactor = dollarConversionFactor;
    }

    public static Currency buildCurrency(String state, String fullName, String isoCode, Double dollarConversionFactor) {
        for (Currency currency : currencies)
            if (currency!=null && (Objects.equals(currency.state, state) || Objects.equals(currency.fullName, fullName) || Objects.equals(currency.isoCode, isoCode)))
                System.out.println("Currencies must be unique.");

        if (!Pattern.compile("[A-Z][ a-z]*").matcher(state).matches())
            System.out.println("The first character in a state's name must be uppercase, the rest must be lowercase and can only contain spaces or alphabetical characters.");

        if (!Pattern.compile("[A-Z][ a-z]*").matcher(fullName).matches())
            System.out.println("The first character in a currency's full name must be uppercase, the rest must be lowercase and can only contain spaces or alphabetical characters.");

        if (!Pattern.compile("[A-Z]{3}").matcher(isoCode).matches())
            System.out.println("ISO Code must be three letters long and can only contain uppercase alphabetical characters.");

        if (dollarConversionFactor <= 0)
            System.out.println("The dollar conversion factor must be strictly positive.");

        currencies[numberOfCurrencies] = new Currency(state, fullName, isoCode, dollarConversionFactor);

        return currencies[numberOfCurrencies-1];
    }

    public static void removeCurrency(int id) {
        if (id < 0 || id >= numberOfCurrencies)
            System.out.println("Index out of bounds.");

        for (int i=0; i<numberOfCurrencies; i++)
            if (currencies[i].id == id)
                System.out.println("deleted.");
    }

    public static Currency getCurrency(int id) {
        if (id < 0 || id >= numberOfCurrencies)
            System.out.println("Index out of bounds.");

        for (int i=0; i<numberOfCurrencies; i++)
            if (currencies[i].id == id)
                return currencies[i];

        return null;
    }

    public static Currency[] getAllCurrencies() {
        return currencies;
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public Double getDollarConversionFactor() {
        return dollarConversionFactor;
    }

//    public static void main(String[] args) {
//        Currency c;
//        buildCurrency("romania", "Leu romanesc", "RON", 5d);
//        buildCurrency("Afghanistan", "afghan afghani", "AFN", 20d);
//        buildCurrency("Algeria", "Algerian dinar", "DZd", 30d);
//        buildCurrency("Armenia", "Armenian dram", "Am", 0.5);
//        buildCurrency("Armenia", "Armenian dram", "Am", 0d);
//
//        c = Currency.getCurrency(0);
//        System.out.println(c.getIsoCode());
//        c = Currency.getCurrency(-1);
//        c = Currency.getCurrency(2);
//        System.out.println(c.getIsoCode());
//    }
}
