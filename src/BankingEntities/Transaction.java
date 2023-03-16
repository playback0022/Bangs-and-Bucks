package BankingEntities;

import BankingProducts.Account;

public class Transaction {
    private Account sourceAccount;
    private Account destinationAccount;
    private Double amountTransferredInSourceCurrency;
    private Double date;
    private String description;
    private Card cardUsedForPayment = null;
}
