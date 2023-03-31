package BankingProducts;

import Helpers.ValidationHandler;

import java.util.HashSet;
import java.util.Set;

public class CurrencyService {
    private final Set<Currency> currencies;
    private static CurrencyService service = null;

    private CurrencyService() {
        currencies = new HashSet<Currency>();
    }

    public static CurrencyService getService() {
        if (service == null)
            service = new CurrencyService();

        return service;
    }

    public Currency getCurrency() {
        String isoCode = ValidationHandler.stringValidator("Enter the ISO code of the desired currency: ", "Invalid ISO code!", "[A-Z]{3}");

        for (Currency currency : currencies)
            if (currency.getIsoCode().equals(isoCode))
                return currency;

        System.out.println("Error: The provided ISO code could not be found!");
        return null;
    }

    public void printAllCurrencies() {
        for (Currency currency: currencies)
            System.out.println(currency);
    }

    public void printCurrency() {
        Currency currency = getCurrency();

        if (currency == null)
            return;

        System.out.println(currency);
    }

    public void buildCurrency() {
        String isoCode = ValidationHandler.stringValidator("Enter the ISO code of the currency: ", "Invalid ISO code!", "[A-Z]{3}");
        Double dollarConversionFactor = ValidationHandler.doubleValidator("Enter the dollar conversion factor of the currency: ", "Invalid dollar conversion factor!", 0, null);

        for (Currency currency : currencies)
            if (currency!=null && (currency.getIsoCode().equals(isoCode))) {
                System.out.println("Error: Currencies must be unique!");
                return;
            }

        currencies.add(new Currency(isoCode, dollarConversionFactor));
        System.out.println("Currency built successfully!");
    }

    public void removeCurrency() {
        Currency currency = getCurrency();

        if (currency == null)
            return;

        currencies.remove(currency);
        System.out.println("Currency removed successfully!");
    }
}
