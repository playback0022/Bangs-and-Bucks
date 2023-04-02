package Services;

import BankingProducts.Currency;
import Helpers.ValidationHandler;

import java.util.HashSet;
import java.util.Set;

public class CurrencyService extends AbstractService {
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

    public Currency getCurrency(String shellIndicator) {
        if (currencies.isEmpty()) {
            System.out.println(">>> No currencies registered so far.");
            return null;
        }

        String isoCode = ValidationHandler.stringValidator("Enter the ISO code of the desired currency: ", "Invalid ISO code!", shellIndicator, "[A-Z]{3}");

        for (Currency currency : currencies)
            if (currency.getIsoCode().equals(isoCode))
                return currency;

        System.out.println("Error: The provided ISO code could not be found!");
        return null;
    }

    public void printAllEntities() {
        for (Currency currency: currencies)
            System.out.println(currency);
    }

    public void printEntity() {
        Currency currency = getCurrency("Currencies");

        if (currency == null)
            return;

        System.out.println(currency);
    }

    public void registerEntity() {
        String isoCode = ValidationHandler.stringValidator("Enter the ISO code of the currency: ", "Invalid ISO code!", "Currencies", "[A-Z]{3}");
        Double dollarConversionFactor = ValidationHandler.doubleValidator("Enter the dollar conversion factor of the currency: ", "Invalid dollar conversion factor!", "Currencies", 0d, null);

        for (Currency currency : currencies)
            if (currency!=null && (currency.getIsoCode().equals(isoCode))) {
                System.out.println("Error: Currencies must be unique!");
                return;
            }

        currencies.add(new Currency(isoCode, dollarConversionFactor));
        System.out.println("Currency built successfully!");
    }

    public void unregisterEntity() {
        Currency currency = getCurrency("Currencies");

        if (currency == null)
            return;

        currencies.remove(currency);
        System.out.println("Currency removed successfully!");
    }
}
