package Services;

import BankingEntities.BankingEntity;
import BankingProducts.Account;
import BankingProducts.Card;
import BankingProducts.Transaction;
import Helpers.ValidationHandler;

import java.util.ArrayList;
import java.util.HashSet;

public class TransactionService {
    private final ArrayList<Transaction> transactions;
    private static TransactionService service = null;

    private TransactionService() {
        transactions = new ArrayList<>();
    }

    public static TransactionService getService() {
        if (service == null)
            service = new TransactionService();

        return service;
    }

    private void printAllEntities() {
        for (int i = 0; i < transactions.size(); i++) {
            System.out.println("------------------------------\nTransaction id: " + i + "\n" + transactions.get(i));
            if (i == transactions.size() - 1)
                System.out.println("------------------------------");
        }
    }

    private void printEntityById() {
        int id = ValidationHandler.intValidator("Enter the id of the desired transaction: ", "Invalid id!", "Transactions", 0, transactions.size() - 1);
        System.out.println("------------------------------\nTransaction id: " + id + "\n" + transactions.get(id) + "\n------------------------------");
    }

    private void printEntitiesBySender() {
        BankingEntity entity = BankingEntityService.getService().getBankingEntity("Transactions");

        if (entity == null)
            return;

        System.out.println("All transaction issued by: " + entity.getIdentity());
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getSourceAccount().getHolder() == entity)
                System.out.println("------------------------------\nTransaction id: " + i + "\n" + transactions.get(i));
    }

    private void printEntitiesByRecipient() {
        BankingEntity entity = BankingEntityService.getService().getBankingEntity("Transactions");

        if (entity == null)
            return;

        System.out.println("All transactions issued to: " + entity.getIdentity());
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getDestinationAccount().getHolder() == entity)
                System.out.println("------------------------------\nTransaction id: " + i + "\n" + transactions.get(i));
    }

    private void printEntitiesBySourceAccount() {
        Account account = AccountService.getService().getAccount("Transactions");

        if (account == null)
            return;

        System.out.println("All transactions issued from the account:\n" + account);
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getSourceAccount() == account)
                System.out.println("------------------------------\nTransaction id: " + i + "\n" + transactions.get(i));
    }

    private void printEntitiesByDestinationAccount() {
        Account account = AccountService.getService().getAccount("Transactions");

        if (account == null)
            return;

        System.out.println("All transactions issued to the account:\n" + account);
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getDestinationAccount() == account)
                System.out.println("------------------------------\nTransaction id: " + i + "\n" + transactions.get(i));
    }

    private void printEntitiesByCard() {
        Card card = CardService.getService().getCard("Transactions");

        if (card == null)
            return;

        System.out.println("All transactions issued using card:\n" + card);
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getCardUsedForPayment() == card)
                System.out.println("------------------------------\nTransaction id: " + i + "\n" + transactions.get(i));
    }

    public void registerEntity(Transaction transaction) {
        transactions.add(transaction);
    }

    public void initService() {
        if (transactions.isEmpty()) {
            System.out.println("Warning: No transactions issued so far.");
            return;
        }

        System.out.println(">>> Transaction Menu Initiated");
        System.out.println("1. List all transactions");
        System.out.println("2. List transaction by id");
        System.out.println("3. List transactions by sender");
        System.out.println("4. List transactions by recipient");
        System.out.println("5. List transactions by source account");
        System.out.println("6. List transactions by destination account");
        System.out.println("7. List transactions by card");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Transactions", 1, 7);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> printAllEntities();
                case 2 -> printEntityById();
                case 3 -> printEntitiesBySender();
                case 4 -> printEntitiesByRecipient();
                case 5 -> printEntitiesBySourceAccount();
                case 6 -> printEntitiesByDestinationAccount();
                case 7 -> printEntitiesByCard();
            }
    }
}
