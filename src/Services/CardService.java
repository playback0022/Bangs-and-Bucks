package Services;

import BankingProducts.Card;
import BankingProducts.Account;
import BankingProducts.Transaction;
import Helpers.ValidationHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class CardService extends AbstractService {
    private final ArrayList<Card> cards;
    private static CardService service = null;

    private CardService() {
        cards = new ArrayList<Card>();
    }

    public static CardService getService() {
        if (service == null)
            service = new CardService();

        return service;
    }

    public Card getCard() {
        int id = ValidationHandler.intValidator("Enter the id of the desired card: ", "Invalid id!", 0, cards.size());
        return cards.get(id);
    }

    public void printAllEntities() {
        for (int i = 0; i < cards.size(); i++)
            System.out.println("\nCard id: " + i + "\n" + cards.get(i));
    }

    public void printEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired card: ", "Invalid id!", 0, cards.size());
        System.out.println("\nAccount id: " + id + "\n" + cards.get(id));
    }

    public void registerEntity() {
        Account account = AccountService.getService().getAccount();

        Random generator = new Random();

        String number = String.format("%08d", generator.nextLong(100000000)) + String.format("%08d", generator.nextLong(100000000));
        LocalDate date = LocalDate.now().plusYears(4);
        String CVV = String.format("%03d", generator.nextLong(1000));

        cards.add(new Card(account, number, date, CVV));
        System.out.println("Card registered successfully!");
    }

    public void unregisterEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired card: ", "Invalid id!", 0, cards.size());

        cards.remove(id);
        System.out.println("Card unregistered successfully!");
    }

    public void makeDeposit() {
        Card card = getCard();
        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to deposit: ", "Invalid amount!", 0d, null);

        card.depositSum(sum);
        System.out.println("Deposit successfully processed!");
    }

    public void performWithdraw() {
        Card card = getCard();
        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", 0d, card.getAccount().getBalance());

        card.withdrawSum(sum);
        System.out.println("Amount successfully withdrawn!");
    }

    public void performTransfer() {
        Card card = getCard();
        Account destinationAccount = AccountService.getService().getAccount();
        Double sum = ValidationHandler.doubleValidator("Enter the amount you wish to withdraw: ", "Invalid amount!", 0d, card.getAccount().getBalance());
        String description = ValidationHandler.stringValidator("Enter the description of the transfer: ", "Invalid description!", ".+");

        card.withdrawSum(sum);
        destinationAccount.depositSum(sum * card.getAccount().getCurrency().getDollarConversionFactor() / destinationAccount.getCurrency().getDollarConversionFactor());

        // this will be passed to the registration method of the TransactionService class
        Transaction resultingTransaction = new Transaction(card.getAccount(), destinationAccount, sum, description, card);
    }
}
