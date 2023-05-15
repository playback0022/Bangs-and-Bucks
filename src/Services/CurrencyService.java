package Services;

import BankingProducts.Currency;
import Helpers.AuditEngine;
import Helpers.DatabaseManagement;
import Helpers.ValidationHandler;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class CurrencyService extends AbstractService {
    private final Set<Currency> currencies;
    private static CurrencyService service = null;

    // mirroring database state, as to avoid unnecessary queries;
    // whenever state changing actions take place, they will be executed both
    // onto the local set and onto the database table to maintain integrity;
    private CurrencyService() {
        currencies = new HashSet<>();

        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
            ResultSet databaseCurrencies = statement.executeQuery("SELECT * FROM CURRENCY");

            // iterating through returned result set, creating objects
            // based on the column contents and adding them to the set;
            while (databaseCurrencies.next()) {
                Currency currentCurrency = new Currency(databaseCurrencies.getString("iso_code"), databaseCurrencies.getDouble("dollar_conversion_factor"));
                currencies.add(currentCurrency);
            }
        }
        catch (SQLException exception) {
            System.exit(1);
        }
    }

    public static CurrencyService getService() {
        if (service == null)
            service = new CurrencyService();

        return service;
    }

    // get method by isoCode
    public Currency getCurrency(String shellIndicator) {
        if (currencies.isEmpty()) {
            System.out.println("Warning: No currencies registered so far.");
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
        for (Currency currency : currencies)
            System.out.println("------------------------------\n" + currency);

        AuditEngine.log("Currencies - List all currencies", null);
    }

    @Override
    protected void printEntity() {
        Currency currency = getCurrency("Currencies");

        if (currency == null)
            return;

        System.out.println("------------------------------\n" + currency);
        AuditEngine.log("Currencies - List currency by ISO code (" + currency.getIsoCode() + ")", null);
    }

    @Override
    protected void registerEntity() {
        String isoCode = ValidationHandler.stringValidator("Enter the ISO code of the currency: ", "Invalid ISO code!", "Currencies", "[A-Z]{3}");
        Double dollarConversionFactor = ValidationHandler.doubleValidator("Enter the dollar conversion factor of the currency: ", "Invalid dollar conversion factor!", "Currencies", 0d, null);

        // verifying that the provided iso code is not assigned to another existing currency
        for (Currency currency : currencies)
            if (currency.getIsoCode().equals(isoCode)) {
                System.out.println("Error: Currencies must be unique!");
                return;
            }

        currencies.add(new Currency(isoCode, dollarConversionFactor));

        // register new entity into database
        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
            statement.executeUpdate("INSERT INTO CURRENCY (iso_code, dollar_conversion_factor) VALUES ('" + isoCode + "', "  + dollarConversionFactor + ")");
        }
        catch (SQLException exception) {
            System.exit(1);
        }

        AuditEngine.log("Currencies - Register currency with ISO-code=" + isoCode, null);
        System.out.println("Currency built successfully!");
    }

    @Override
    protected void unregisterEntity() {
        Currency currency = getCurrency("Currencies");

        if (currency == null)
            return;

        currencies.remove(currency);

        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
            statement.executeUpdate("DELETE FROM CURRENCY where iso_code='" + currency.getIsoCode() + "'");
        }
        catch (SQLException exception) {
            System.exit(1);
        }

        AuditEngine.log("Currencies - Unregister currency with ISO-code=" + currency.getIsoCode(), null);
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
        System.out.println("4. List currency by ISO code");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Currencies", 1, 4);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> registerEntity();
                case 2 -> unregisterEntity();
                case 3 -> printAllEntities();
                case 4 -> printEntity();
            }
    }
}
