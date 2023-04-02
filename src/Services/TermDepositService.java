package Services;

import BankingProducts.Account;
import BankingProducts.TermDeposit;
import Helpers.ValidationHandler;

import java.util.ArrayList;
import java.util.HashSet;

public class TermDepositService extends AbstractService {
    private final ArrayList<TermDeposit> deposits;
    private static TermDepositService service = null;

    private TermDepositService() {
        deposits = new ArrayList<>();
    }

    public static TermDepositService getService() {
        if (service == null)
            service = new TermDepositService();

        return service;
    }

    public TermDeposit getTermDeposit(String shellIndicator) {
        if (deposits.isEmpty()) {
            System.out.println("Warning: No term deposits registered so far.");
            return null;
        }

        int id = ValidationHandler.intValidator("Enter the id of the desired term deposit: ", "Invalid id!", shellIndicator, 0, deposits.size() - 1);
        return deposits.get(id);
    }

    @Override
    protected void printAllEntities() {
        for (int i = 0; i < deposits.size(); i++) {
            System.out.println("------------------------------\nTerm Deposit id: " + i + "\n" + deposits.get(i));
            if (i == deposits.size() - 1)
                System.out.println("------------------------------");
        }
    }

    @Override
    protected void printEntity() {
        if (deposits.isEmpty()) {
            System.out.println("Warning: No term deposits registered so far.");
            return;
        }

        int id = ValidationHandler.intValidator("Enter the id of the desired term deposit: ", "Invalid id!", "Term Deposits", 0, deposits.size() - 1);
        System.out.println("------------------------------\nTerm Deposit id: " + id + "\n" + deposits.get(id));
    }

    @Override
    protected void registerEntity() {
        Account account = AccountService.getService().getAccount("Term Deposits");

        if (account == null)
            return;

        Double amount = ValidationHandler.doubleValidator("Enter the amount to be deposited: ", "Invalid amount!", "Term Deposits", 0d, account.getBalance());
        Integer durationInMonths = ValidationHandler.intValidator("Enter the duration of the term deposit (in months): ", "Invalid duration!", "Term Deposits", 3, 13);
        Double interestRate = (double) ValidationHandler.intValidator("Enter the interest rate of the term deposit: ", "Invalid interest rate!", "Term Deposits", 0, 20) / 100;

        deposits.add(new TermDeposit(account.getHolder(), amount, account.getCurrency(), durationInMonths, interestRate));
        System.out.println("Term Deposit registered successfully!");
    }

    @Override
    protected void unregisterEntity() {
        TermDeposit termDeposit = getTermDeposit("Term Deposits");

        deposits.remove(termDeposit);
        System.out.println("Term Deposit unregistered successfully!");
    }

    private void emptyDeposit() {
        TermDeposit termDeposit = getTermDeposit("Term Deposits");

        Account receivingAccount = AccountService.getService().getAccount("Term Deposits");

        if (receivingAccount == null)
            return;

        while (termDeposit.getOwner() != receivingAccount.getHolder()) {
            System.out.println("Error: The holder of the receiving account and the owner of the term deposit don't match!");
            receivingAccount = AccountService.getService().getAccount("Term Deposits");
        }

        termDeposit.emptyDeposit(receivingAccount);
    }

    @Override
    public void initService() {
        System.out.println(">>> Term Deposit Menu Initiated");
        System.out.println("1. Register term deposit");

        if (deposits.isEmpty()) {
            HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Term Deposits", 1, 1);
            if (!choices.isEmpty())
                registerEntity();
            return;
        }

        System.out.println("2. Unregister term deposit");
        System.out.println("3. List all term deposits");
        System.out.println("4. List term deposit by id");
        System.out.println("5. Empty deposit");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Term Deposits", 1, 5);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> registerEntity();
                case 2 -> unregisterEntity();
                case 3 -> printAllEntities();
                case 4 -> printEntity();
                case 5 -> emptyDeposit();
            }
    }
}
