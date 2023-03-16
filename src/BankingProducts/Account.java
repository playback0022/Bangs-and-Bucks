package BankingProducts;

import BankingEntities.BankingEntity;

import java.util.regex.Pattern;

public class Account {
    BankingEntity holder;
    String IBAN;
    Currency currency;
    Double balance=0d;

    Account(BankingEntity holder, String IBAN, Currency currency) {
        this.holder = holder;
        this.setIBAN(IBAN);
        this.currency = currency;
    }

    Account(BankingEntity holder, String IBAN, Currency currency, Double startingBalance) {
        this.holder = holder;
        this.setIBAN(IBAN);
        this.currency = currency;
        this.depositSum(startingBalance);
    }

    public BankingEntity getHolder() {
        return holder;
    }

    public void setHolder(BankingEntity holder) {
        this.holder = holder;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        if (!Pattern.compile("^RO[0-9]{2}[A-Z]{4}[A-Z0-9]{16}").matcher(IBAN).matches())
            System.out.println("Invalid IBAN.");

        this.IBAN = IBAN;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void depositSum(Double sum) {
        if (sum <= 0)
            System.out.println("Invalid sum of money to deposit.");

        balance += sum;
    }

    public void withdrawSum(Double sum) {
        if (sum <=0 || sum > balance)
            System.out.println("Invalid sum of money to withdraw.");

        balance -= sum;
    }

    public Transaction transferSum(Double sum, Account receivingAccount) {
        if (sum <= 0 || sum > balance)
            System.out.println("Invalid sum of money to withdraw.");

        balance -= sum;
        receivingAccount.balance += sum;

        return new BankTransfer(this, receivingAccount, );
    }
}
