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

    public TermDeposit getTermDeposit() {
        int id = ValidationHandler.intValidator("Enter the id of the desired term deposit: ", "Invalid id!", 0, deposits.size());
        return deposits.get(id);
    }

    public void printAllEntities() {
        for (int i = 0; i < deposits.size(); i++)
            System.out.println("\nTerm Deposit id: " + i + "\n" + deposits.get(i));
    }

    public void printEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired term deposit: ", "Invalid id!", 0, deposits.size());
        System.out.println("\nTermDeposit id: " + id + "\n" + deposits.get(id));
    }

    public void registerEntity() {
        Account account = AccountService.getService().getAccount();
        Double amount = ValidationHandler.doubleValidator("Enter the amount to be deposited: ", "Invalid amount!", 0d, account.getBalance());
        Integer durationInMonths = ValidationHandler.intValidator("Enter the duration of the term deposit (in months): ", "Invalid duration!", 3, 13);
        Double interestRate = (double) ValidationHandler.intValidator("Enter the interest rate of the term deposit: ", "Invalid interest rate!", 0, 20) / 100;

        deposits.add(new TermDeposit(account.getHolder(), amount, account.getCurrency(), durationInMonths, interestRate));
        System.out.println("Term Deposit registered successfully!");
    }

    public void unregisterEntity() {
        TermDeposit termDeposit = getTermDeposit();

        deposits.remove(termDeposit);
        System.out.println("Term Deposit unregistered successfully!");
    }

    public void emptyDeposit() {
        TermDeposit termDeposit = getTermDeposit();

        Account receivingAccount = AccountService.getService().getAccount();
        while (termDeposit.getOwner() != receivingAccount.getHolder()) {
            System.out.println("Error: The holder of the receiving account and the owner of the term deposit don't match!");
            receivingAccount = AccountService.getService().getAccount();
        }

        termDeposit.emptyDeposit(receivingAccount);
    }
}
