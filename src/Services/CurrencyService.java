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

    @Override
    protected void printAllEntities() {
        for (Currency currency: currencies)
            System.out.println(currency);
    }

    @Override
    protected void printEntity() {
        Currency currency = getCurrency("Currencies");

        if (currency == null)
            return;

        System.out.println(currency);
    }

    @Override
    protected void registerEntity() {
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

    @Override
    protected void unregisterEntity() {
        Currency currency = getCurrency("Currencies");

        if (currency == null)
            return;

        currencies.remove(currency);
        System.out.println("Currency removed successfully!");
    }

    @Override
    public void initService() {
        System.out.println(">>> Currency Menu Initiated");
        System.out.println("1. Register currency");

        if (currencies.isEmpty()) {
            HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Currencies", 1, 1);
            if (!choices.isEmpty())
                registerEntity();
            return;
        }

        System.out.println("2. Unregister currency");
        System.out.println("3. List all currencies");
        System.out.println("4. List term currency by id");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Transactions", 1, 4);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> registerEntity();
                case 2 -> unregisterEntity();
                case 3 -> printAllEntities();
                case 4 -> printEntity();
            }
    }
}
