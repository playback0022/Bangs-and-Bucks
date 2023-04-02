package Services;

import BankingEntities.BankingEntity;
import BankingProducts.Account;
import BankingProducts.TermDeposit;
import Helpers.ValidationHandler;

import java.util.ArrayList;

public class TermDepositService extends AbstractService {
    private final ArrayList<TermDeposit> deposits;
    private static TermDepositService service = null;

    private TermDepositService() {
        deposits = new ArrayList<TermDeposit>();
    }

    public static TermDepositService getService() {
        if (service == null)
            service = new TermDepositService();

        return service;
    }

    public TermDeposit getTermDeposit(String shellIndicator) {
        int id = ValidationHandler.intValidator("Enter the id of the desired term deposit: ", "Invalid id!", shellIndicator, 0, deposits.size() - 1);
        return deposits.get(id);
    }

    public void printAllEntities() {
        if (deposits.isEmpty()) {
            System.out.println("No deposits occurred so far.");
            return;
        }

        for (int i = 0; i < deposits.size(); i++)
            System.out.println("\nTerm Deposit id: " + i + "\n" + deposits.get(i));
    }

    public void printEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired term deposit: ", "Invalid id!", "Term Deposits", 0, deposits.size());
        System.out.println("\nTermDeposit id: " + id + "\n" + deposits.get(id));
    }

    public void registerEntity() {
        Account account = AccountService.getService().getAccount("Term Deposits");
        Double amount = ValidationHandler.doubleValidator("Enter the amount to be deposited: ", "Invalid amount!", "Term Deposits", 0d, account.getBalance());
        Integer durationInMonths = ValidationHandler.intValidator("Enter the duration of the term deposit (in months): ", "Invalid duration!", "Term Deposits", 3, 13);
        Double interestRate = (double) ValidationHandler.intValidator("Enter the interest rate of the term deposit: ", "Invalid interest rate!", "Term Deposits", 0, 20) / 100;

        deposits.add(new TermDeposit(account.getHolder(), amount, account.getCurrency(), durationInMonths, interestRate));
        System.out.println("Term Deposit registered successfully!");
    }

    public void unregisterEntity() {
        TermDeposit termDeposit = getTermDeposit("Term Deposits");

        deposits.remove(termDeposit);
        System.out.println("Term Deposit unregistered successfully!");
    }

    public void emptyDeposit() {
        TermDeposit termDeposit = getTermDeposit("Term Deposits");

        Account receivingAccount = AccountService.getService().getAccount("Term Deposits");
        while (termDeposit.getOwner() != receivingAccount.getHolder()) {
            System.out.println("Error: The holder of the receiving account and the owner of the term deposit don't match!");
            receivingAccount = AccountService.getService().getAccount("Term Deposits");
        }

        termDeposit.emptyDeposit(receivingAccount);
    }
}
