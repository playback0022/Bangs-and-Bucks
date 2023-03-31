package Services;

import BankingProducts.Account;
import BankingProducts.SavingsAccount;
import BankingProducts.Currency;
import BankingEntities.BankingEntity;
import Helpers.ValidationHandler;

import java.time.LocalDate;
import java.util.ArrayList;

public class AccountService extends AbstractService {
    private final ArrayList<Account> accounts;
    private static AccountService service = null;

    private AccountService() {
        accounts = new ArrayList<>();
    }

    public static AccountService getService() {
        if (service == null)
            service = new AccountService();

        return service;
    }

    public Account getAccount() {
        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", 0, accounts.size());

        return accounts.get(id);
    }

    public void printAllEntities() {
        for (int i = 0; i < accounts.size(); i++)
            System.out.println("\nAccount id: " + i + "\n" + accounts.get(i));
    }

    public void printAllPlainAccounts() {
        for (int i = 0; i < accounts.size(); i++)
            if (!(accounts.get(i) instanceof SavingsAccount))
                System.out.println("\nAccount id: " + i + "\n" + accounts.get(i));
    }

    public void printAllSavingsAccounts() {
        for (int i = 0; i < accounts.size(); i++)
            if (accounts.get(i) instanceof SavingsAccount)
                System.out.println("\nAccount id: " + i + "\n" + accounts.get(i));
    }

    public void printEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired account: ", "Invalid id!", 0, accounts.size());

        System.out.println("\nAccount id: " + id + "\n" + accounts.get(id));
    }

    public void registerEntity() {
        int choice = ValidationHandler.intValidator("Do you wish to open a simple account(0) or a savings account(1)? ", "Invalid choice!", 0, 1);

        BankingEntity holder = BankingEntityService.getService().getBankingEntity();
        Currency currency = CurrencyService.getService().getCurrency();

        if (currency == null) {
            System.out.println("Account registration failed!");
            return;
        }

        Account account = null;
        switch (choice) {
            case 0:
                account = new Account(holder, currency);
                break;

            case 1:
                Double interestRate = ValidationHandler.doubleValidator("Enter the interest rate of the savings account: ", "Invalid interest rate!", 0, 100);

                account = new SavingsAccount(holder, currency, interestRate / 100);
                break;
        }

        accounts.add(account);
        System.out.println("Account registered successfully!");
    }

    public void unregisterEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired account: ", "Invalid id!", 0, accounts.size());

        accounts.remove(id);
        System.out.println("Account unregistered successfully!");
    }
}
