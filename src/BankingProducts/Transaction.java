package BankingProducts;

import java.time.LocalDate;

public class Transaction {
    private final Account sourceAccount;
    private final Account destinationAccount;
    private final Double amountTransferredInSourceCurrency;
    private final LocalDate date;
    private final String description;
    private final Card cardUsedForPayment;

    public Transaction(Account sourceAccount, Account destinationAccount, Double amountTransferredInSourceCurrency, String description, Card cardUsedForPayment) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amountTransferredInSourceCurrency = amountTransferredInSourceCurrency;
        date = LocalDate.now();
        this.description = description;
        this.cardUsedForPayment = cardUsedForPayment;
    }

    @Override
    public String toString() {
        String base = "Source Account: " + sourceAccount.getIBAN() +
                "\nDestination Account:" + destinationAccount.getIBAN() +
                "\nAmount:" + amountTransferredInSourceCurrency +
                sourceAccount.currency.getIsoCode() +
                "\nDate:" + date + "\nDescription: '" + description + '\'';

        if (cardUsedForPayment != null)
            base += "\nCard (ending in): " + cardUsedForPayment.getNumber().substring(11);

        return base;
    }
}
