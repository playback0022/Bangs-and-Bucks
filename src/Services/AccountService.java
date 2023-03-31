package Services;

import BankingProducts.Account;
import BankingProducts.SavingsAccount;
import BankingProducts.Currency;
import BankingProducts.Transaction;
import BankingEntities.BankingEntity;
import Helpers.ValidationHandler;

import java.util.ArrayList;

public class AccountService extends AbstractService {
    private final ArrayList<Account> accounts;
    private static AccountService service = null;

    private AccountService() {
        accounts = new ArrayList<Account>();
    }

    public static AccountService getService() {
        if (service == null)
            service = new AccountService();

        return service;
    }

    public Account getAccount() {
        int id = ValidationHandler.intValidator("Enter the id of the desired account: ", "Invalid id!", 0, accounts.size());

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
                Double interestRate = ValidationHandler.doubleValidator("Enter the interest rate of the savings account: ", "Invalid interest rate!", 0d, 100d);

                account = new SavingsAccount(holder, currency, interestRate / 100);
                break;
        }

        accounts.add(account);
        System.out.println("Account registered successfully!");
    }

    public void unregisterEntity() {
        Account account = getAccount();

        accounts.remove(account);
        System.out.println("Account unregistered successfully!");
    }

    public void makeDeposit() {
        Account account = getAccount();
        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to deposit: ", "Invalid amount!", 0d, null);

        account.depositSum(sum);
        System.out.println("Deposit successfully processed!");
    }
    
    public void performWithdraw() {
        Account account = getAccount();
        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", 0d, account.getBalance());

        account.withdrawSum(sum);
        System.out.println("Amount successfully withdrawn!");
    }
    
    public void performTransfer() {
        Account sourceAccount = getAccount();
        Account destinationAccount = getAccount();
        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", 0d, sourceAccount.getBalance());
        String description = ValidationHandler.stringValidator("Enter the description of the transfer: ", "Invalid description!", ".+");

        sourceAccount.withdrawSum(sum);
        destinationAccount.depositSum(sum * sourceAccount.getCurrency().getDollarConversionFactor() / destinationAccount.getCurrency().getDollarConversionFactor());

        // this will be passed to the registration method of the TransactionService class
        Transaction resultingTransaction = new Transaction(sourceAccount, destinationAccount, sum, description, null);
    }
}
