package BankingProducts;

import BankingEntities.BankingEntity;

import java.util.regex.Pattern;

public class Account {
    private final BankingEntity holder;
    private final String IBAN;
    protected final Currency currency;
    protected Double balance=0d;

    Account(BankingEntity holder, String IBAN, Currency currency) {
        this.holder = holder;
        this.IBAN = IBAN;
        this.currency = currency;
    }

    Account(BankingEntity holder, String IBAN, Currency currency, Double startingBalance) {
        this.holder = holder;
        this.IBAN = IBAN;
        this.currency = currency;
        this.depositSum(startingBalance);
    }

    @Override
    public String toString() {
        return  "Holder: " + holder.getIdentity() + "\nIBAN: " + IBAN + "\nBalance: " + balance + currency.getIsoCode();
    }

    public BankingEntity getHolder() {
        return holder;
    }

    public String getIBAN() {
        return IBAN;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void depositSum(Double sum) {
        if (sum <= 0) {
            System.out.println("Invalid sum of money to deposit.");
            return;
        }

        balance += sum;
    }

    public void withdrawSum(Double sum) {
        if (sum <= 0 || sum > balance) {
            System.out.println("Invalid sum of money to withdraw.");
            return;
        }

        balance -= sum;
    }

    public Transaction transferSum(Double sum, Account receivingAccount, String description) {
        if (sum <= 0 || sum > balance)
            System.out.println("Invalid sum of money to withdraw.");

        balance -= sum;
        receivingAccount.balance += sum * currency.getDollarConversionFactor() / receivingAccount.currency.getDollarConversionFactor();

        return new Transaction(this, receivingAccount, sum, description, null);
    }
}
