package Services;

import BankingProducts.Account;
import BankingProducts.SavingsAccount;
import BankingProducts.Currency;
import BankingProducts.Transaction;
import BankingEntities.BankingEntity;
import Helpers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

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

    public Account getAccount(String shellIndicator) {
        // warning should be propagated to the source
        if (accounts.isEmpty()) {
            System.out.println("Warning: No account registered so far.");
            return null;
        }

        int id = ValidationHandler.intValidator("Enter the id of the desired account: ", "Invalid id!", shellIndicator, 0, accounts.size() - 1);
        return accounts.get(id);
    }

    @Override
    protected void printAllEntities() {
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println("------------------------------\nAccount id: " + i + "\n" + accounts.get(i));
            if (i == accounts.size() - 1)
                System.out.println("------------------------------");
        }

        AuditEngine.log("Accounts - List all accounts", null);
    }

    private void printAllPlainAccounts() {
        for (int i = 0; i < accounts.size(); i++)
            if (!(accounts.get(i) instanceof SavingsAccount))
                System.out.println("------------------------------\nAccount id: " + i + "\n" + accounts.get(i));

        AuditEngine.log("Accounts - List all simple accounts", null);
    }

    private void printAllSavingsAccounts() {
        for (int i = 0; i < accounts.size(); i++)
            if (accounts.get(i) instanceof SavingsAccount)
                System.out.println("------------------------------\nAccount id: " + i + "\n" + accounts.get(i));

        AuditEngine.log("Accounts - List all savings accounts", null);
    }

    @Override
    protected void printEntity() {
        if (accounts.isEmpty()) {
            System.out.println("Warning: No account registered so far.");
            return;
        }

        int id = ValidationHandler.intValidator("Enter the id of the desired account: ", "Invalid id!", "Accounts", 0, accounts.size() - 1);

        System.out.println("------------------------------\nAccount id: " + id + "\n" + accounts.get(id) + "\n------------------------------");

        AuditEngine.log("Accounts - List account by id (" + id + ")", null);
    }

    @Override
    protected void registerEntity() {
        int choice = ValidationHandler.intValidator("Do you wish to open a simple account(0) or a savings account(1)? ", "Invalid choice!", "Accounts", 0, 1);

        BankingEntity holder = BankingEntityService.getService().getBankingEntity("Accounts");

        if (holder == null)
            return;

        Currency currency = CurrencyService.getService().getCurrency("Accounts");

        if (currency == null)
            return;

        Account account = null;
        switch (choice) {
            case 0 -> {
                account = new Account(holder, currency);
                AuditEngine.log("Accounts - Register simple account in '" + currency.getIsoCode() + "' with id=" + accounts.size(), null);
            }
            case 1 -> {
                Double interestRate = ValidationHandler.doubleValidator("Enter the interest rate of the savings account: ", "Invalid interest rate!", "Accounts", 0d, 100d);
                account = new SavingsAccount(holder, currency, interestRate / 100);
                AuditEngine.log("Accounts - Register savings account in '" + currency.getIsoCode() + "' with id=" + accounts.size(), null);
            }
        }

        accounts.add(account);
        System.out.println("Account registered successfully!");
    }

    @Override
    protected void unregisterEntity() {
        Account account = getAccount("Accounts");

        if (account == null)
            return;

        AuditEngine.log("Accounts - Unregister account with id=" + accounts.indexOf(account), null);
        accounts.remove(account);
        System.out.println("Account unregistered successfully!");
    }

    private void makeDeposit() {
        Account account = getAccount("Accounts");

        if (account == null)
            return;

        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to deposit: ", "Invalid amount!", "Accounts", 0d, null);
        // both sums should be stored in order to be logged
        Double currentBalance = account.getBalance();
        Double newBalance = account.getBalance() + sum;

        account.depositSum(sum);

        ArrayList<AdjustmentLog> logs = new ArrayList<>();
        logs.add(new AdjustmentLog("balance", currentBalance.toString(), newBalance.toString()));

        AuditEngine.log("Accounts - Make deposit into account with id=" + accounts.indexOf(account), logs);
        System.out.println("Deposit successfully processed!");
    }

    private void performWithdraw() {
        Account account = getAccount("Accounts");

        if (account == null)
            return;

        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", "Accounts", 0d, account.getBalance());
        // both sums should be stored in order to be logged
        Double currentBalance = account.getBalance();
        Double newBalance = account.getBalance() - sum;

        account.withdrawSum(sum);

        ArrayList<AdjustmentLog> logs = new ArrayList<>();
        logs.add(new AdjustmentLog("balance", currentBalance.toString(), newBalance.toString()));

        AuditEngine.log("Accounts - Perform withdraw", logs);
        System.out.println("Amount successfully withdrawn!");
    }

    private void performTransfer() {
        Account sourceAccount = getAccount("Accounts");

        if (sourceAccount == null)
            return;

        Account destinationAccount = getAccount("Accounts");

        if (destinationAccount == null)
            return;

        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", "Accounts", 0d, sourceAccount.getBalance());
        String description = ValidationHandler.stringValidator("Enter the description of the transfer: ", "Invalid description!", "Accounts", ".+");

        Double currentBalanceSource = sourceAccount.getBalance();
        Double newBalanceSource = sourceAccount.getBalance() - sum;
        Double currentBalanceDestination = destinationAccount.getBalance();
        Double newBalanceDestination = destinationAccount.getBalance() + sum * sourceAccount.getCurrency().getDollarConversionFactor() / destinationAccount.getCurrency().getDollarConversionFactor();

        sourceAccount.withdrawSum(sum);
        destinationAccount.depositSum(sum * sourceAccount.getCurrency().getDollarConversionFactor() / destinationAccount.getCurrency().getDollarConversionFactor());

        // 'resultingTransaction' will be passed to the registration method of the TransactionService class
        Transaction resultingTransaction = new Transaction(sourceAccount, destinationAccount, sum, description, null);
        TransactionService.getService().registerEntity(resultingTransaction);

        ArrayList<AdjustmentLog> logs = new ArrayList<>();
        logs.add(new AdjustmentLog("source account balance", currentBalanceSource.toString(), newBalanceSource.toString()));
        logs.add(new AdjustmentLog("destination account balance", currentBalanceDestination.toString(), newBalanceDestination.toString()));

        AuditEngine.log("Accounts - Perform transfer from source-account-id=" + accounts.indexOf(sourceAccount) + " to destination-account-id=" + accounts.indexOf(destinationAccount), logs);
    }

    private void updateBalance() {
        Account savingsAccount = getAccount("Accounts");

        if (savingsAccount == null)
            return;

        if (!(savingsAccount instanceof SavingsAccount)) {
            System.out.println("Error: Simple accounts don't have and 'update balance' feature!");
            return;
        }

        if (LocalDate.now().getDayOfMonth() != 1) {
            System.out.println("Error: Balance updates occur only on the first of every month!");
            return;
        }

        ((SavingsAccount) savingsAccount).updateBalance();
        System.out.println("Balance updated successfully!");
    }

    @Override
    public void initService() {
        System.out.println(">>> Account Menu Initiated");
        System.out.println("1. Register account");

        if (accounts.isEmpty()) {
            HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Accounts", 1, 1);
            if (!choices.isEmpty())
                registerEntity();
            return;
        }

        System.out.println("2. Unregister account");
        System.out.println("3. List all accounts");
        System.out.println("4. List all simple accounts");
        System.out.println("5. List all savings accounts");
        System.out.println("6. List account by id");
        System.out.println("7. Make deposit");
        System.out.println("8. Perform withdraw");
        System.out.println("9. Perform transfer");
        System.out.println("10. Update balance (savings accounts)");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Accounts", 1, 10);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> registerEntity();
                case 2 -> unregisterEntity();
                case 3 -> printAllEntities();
                case 4 -> printAllPlainAccounts();
                case 5 -> printAllSavingsAccounts();
                case 6 -> printEntity();
                case 7 -> makeDeposit();
                case 8 -> performWithdraw();
                case 9 -> performTransfer();
                case 10 -> updateBalance();
            }
    }
}
