package Services;

import BankingProducts.Card;
import BankingProducts.Account;
import BankingProducts.Transaction;
import Helpers.AdjustmentLog;
import Helpers.AuditEngine;
import Helpers.ValidationHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class CardService extends AbstractService {
    private final ArrayList<Card> cards;
    private static CardService service = null;

    private CardService() {
        cards = new ArrayList<>();
    }

    public static CardService getService() {
        if (service == null)
            service = new CardService();

        return service;
    }

    public Card getCard(String shellIndicator) {
        if (cards.isEmpty()) {
            System.out.println("Warning: No cards registered so far.");
            return null;
        }

        int id = ValidationHandler.intValidator("Enter the id of the desired card: ", "Invalid id!", shellIndicator, 0, cards.size() - 1);
        return cards.get(id);
    }

    protected void printAllEntities() {
        for (int i = 0; i < cards.size(); i++) {
            System.out.println("------------------------------\nCard id: " + i + "\n" + cards.get(i));
            if (i == cards.size() - 1)
                System.out.println("------------------------------");
        }

        AuditEngine.log("Cards - List all cards", null);
    }

    protected void printEntity() {
        if (cards.isEmpty()) {
            System.out.println("Warning: No cards registered so far.");
            return;
        }

        int id = ValidationHandler.intValidator("Enter the id of the desired card: ", "Invalid id!", "Cards", 0, cards.size() - 1);
        System.out.println("------------------------------\nCard id: " + id + "\n" + cards.get(id) + "\n------------------------------");
        AuditEngine.log("Cards - List card by id (" + id + ")", null);
    }

    protected void registerEntity() {
        Account account = AccountService.getService().getAccount("Cards");

        if (account == null)
            return;

        Random generator = new Random();

        String number = String.format("%08d", generator.nextLong(100000000)) + String.format("%08d", generator.nextLong(100000000));
        LocalDate date = LocalDate.now().plusYears(4);
        String CVV = String.format("%03d", generator.nextLong(1000));

        cards.add(new Card(account, number, date, CVV));
        AuditEngine.log("Cards - Register card with id=" + (cards.size() - 1), null);
        System.out.println("Card registered successfully!");
    }

    protected void unregisterEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired card: ", "Invalid id!", "Cards", 0, cards.size() - 1);

        cards.remove(id);
        AuditEngine.log("Cards - Unregister card with id=" + id, null);
        System.out.println("Card unregistered successfully!");
    }

    private void makeDeposit() {
        Card card = getCard("Cards");

        if (card == null)
            return;

        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            System.out.println("Error: This card has expired!");
            return;
        }

        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to deposit: ", "Invalid amount!", "Cards", 0d, null);
        Double currentBalance = card.getAccount().getBalance();
        Double newBalance = card.getAccount().getBalance() + sum;

        card.depositSum(sum);

        ArrayList<AdjustmentLog> logs = new ArrayList<>();
        logs.add(new AdjustmentLog("balance", currentBalance.toString(), newBalance.toString()));

        AuditEngine.log("Cards - Make deposit into card with id=" + cards.indexOf(card), logs);
        System.out.println("Deposit successfully processed!");
    }

    private void performWithdraw() {
        Card card = getCard("Cards");

        if (card == null)
            return;

        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            System.out.println("Error: This card has expired!");
            return;
        }

        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", "Cards", 0d, card.getAccount().getBalance());
        Double currentBalance = card.getAccount().getBalance();
        Double newBalance = card.getAccount().getBalance() - sum;

        ArrayList<AdjustmentLog> logs = new ArrayList<>();
        logs.add(new AdjustmentLog("balance", currentBalance.toString(), newBalance.toString()));

        card.withdrawSum(sum);
        AuditEngine.log("Cards - Perform withdraw from card with id=" + cards.indexOf(card), logs);
        System.out.println("Amount successfully withdrawn!");
    }

    private void performTransfer() {
        Card card = getCard("Cards");

        if (card == null)
            return;

        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            System.out.println("Error: This card has expired!");
            return;
        }

        Account destinationAccount = AccountService.getService().getAccount("Cards");

        if (destinationAccount == null)
            return;

        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", "Cards", 0d, card.getAccount().getBalance());
        String description = ValidationHandler.stringValidator("Enter the description of the transfer: ", "Invalid description!", "Cards", ".+");

        Double currentBalanceSource = card.getAccount().getBalance();
        Double newBalanceSource = card.getAccount().getBalance() - sum;
        Double currentBalanceDestination = destinationAccount.getBalance();
        Double newBalanceDestination = destinationAccount.getBalance() + sum * card.getAccount().getCurrency().getDollarConversionFactor() / destinationAccount.getCurrency().getDollarConversionFactor();

        card.withdrawSum(sum);
        destinationAccount.depositSum(sum * card.getAccount().getCurrency().getDollarConversionFactor() / destinationAccount.getCurrency().getDollarConversionFactor());

        // 'resultingTransaction' will be passed to the registration method of the TransactionService class, with the card reference set
        Transaction resultingTransaction = new Transaction(card.getAccount(), destinationAccount, sum, description, card);
        TransactionService.getService().registerEntity(resultingTransaction);

        ArrayList<AdjustmentLog> logs = new ArrayList<>();
        logs.add(new AdjustmentLog("source account balance", currentBalanceSource.toString(), newBalanceSource.toString()));
        logs.add(new AdjustmentLog("destination account balance", currentBalanceDestination.toString(), newBalanceDestination.toString()));

        AuditEngine.log("Cards - Perform transfer from card with id=" + cards.indexOf(card), logs);
    }

    @Override
    public void initService() {
        System.out.println(">>> Card Menu Initiated");
        System.out.println("1. Register card");

        if (cards.isEmpty()) {
            HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Cards", 1, 1);
            if (!choices.isEmpty())
                registerEntity();
            return;
        }

        System.out.println("2. Unregister card");
        System.out.println("3. List all cards");
        System.out.println("4. List card by id");
        System.out.println("5. Make deposit");
        System.out.println("6. Perform withdraw");
        System.out.println("7. Perform transfer");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Cards", 1, 7);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> registerEntity();
                case 2 -> unregisterEntity();
                case 3 -> printAllEntities();
                case 4 -> printEntity();
                case 5 -> makeDeposit();
                case 6 -> performWithdraw();
                case 7 -> performTransfer();
            }
    }
}
