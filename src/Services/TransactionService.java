package Services;

import BankingEntities.BankingEntity;
import BankingProducts.Account;
import BankingProducts.Card;
import BankingProducts.Transaction;
import Helpers.ValidationHandler;

import java.util.ArrayList;

public class TransactionService {
    private final ArrayList<Transaction> transactions;
    private static TransactionService service = null;

    private TransactionService() {
        transactions = new ArrayList<Transaction>();
    }

    public static TransactionService getService() {
        if (service == null)
            service = new TransactionService();

        return service;
    }

    public void printAllEntities() {
        for (int i = 0; i < transactions.size(); i++)
            System.out.println("Transaction id: " + i + "\n" + transactions.get(i) + "\n");
    }

    public void printEntityById() {
        int id = ValidationHandler.intValidator("Enter the id of the desired transaction: ", "Invalid id!", 0, transactions.size());
        System.out.println(transactions.get(id) + "\n");
    }

    public void printEntitiesBySender() {
        BankingEntity entity = BankingEntityService.getService().getBankingEntity();

        System.out.println("All transaction issued by: " + entity.getIdentity());
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getSourceAccount().getHolder() == entity)
                System.out.println("Transaction id: " + i + "\n" + transactions.get(i) + "\n");
    }

    public void printEntitiesByRecipient() {
        BankingEntity entity = BankingEntityService.getService().getBankingEntity();

        System.out.println("All transactions issued to: " + entity.getIdentity());
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getDestinationAccount().getHolder() == entity)
                System.out.println("Transaction id: " + i + "\n" + transactions.get(i) + "\n");
    }

    public void printEntitiesBySourceAccount() {
        Account account = AccountService.getService().getAccount();

        System.out.println("All transactions issued from the account:\n" + account.toString());
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getSourceAccount() == account)
                System.out.println("Transaction id: " + i + "\n" + transactions.get(i) + "\n");
    }

    public void printEntitiesByDestinationAccount() {
        Account account = AccountService.getService().getAccount();

        System.out.println("All transactions issued to the account:\n" + account.toString());
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getDestinationAccount() == account)
                System.out.println("Transaction id: " + i + "\n" + transactions.get(i) + "\n");
    }

    public void printEntitiesByCard() {
        Card card = CardService.getService().getCard();

        System.out.println("All transactions issued using card:\n" + card.toString());
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getCardUsedForPayment() == card)
                System.out.println("Transaction id: " + i + "\n" + transactions.get(i) + "\n");
    }

    public void registerEntity(Transaction transaction) {
        transactions.add(transaction);
    }
}
