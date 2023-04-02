package BankingProducts;

import BankingEntities.BankingEntity;

import java.util.Random;

public class Account {
    private final BankingEntity holder;
    private final String IBAN;
    protected final Currency currency;
    protected Double balance = 0d;

    public Account(BankingEntity holder, Currency currency) {
        Random randomIBANGenerator = new Random();
        int securityCode = randomIBANGenerator.nextInt(100);
        int accountNumberFirstSection = randomIBANGenerator.nextInt(10000000);
        int accountNumberSecondSection = randomIBANGenerator.nextInt(10000000);

        this.holder = holder;
        this.IBAN = "RO" + String.format("%02d", securityCode) + "BNGS" + String.format("%08d", accountNumberFirstSection) + String.format("%08d", accountNumberSecondSection);
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Holder: " + holder.getIdentity() + "\nIBAN: " + IBAN + "\nBalance: " + balance + currency.getIsoCode();
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
        balance += sum;
    }

    public void withdrawSum(Double sum) {
        balance -= sum;
    }
}
