package BankingProducts;

import BankingEntities.BankingEntity;


public class SavingsAccount extends Account {
    private final Double interestRate;

    public SavingsAccount(BankingEntity holder, Currency currency, Double interestRate) {
        super(holder, currency);

        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return super.toString() + "\nInterest rate: " + interestRate * 100 + "%";
    }

    public void updateBalance() {
        balance += balance * interestRate;
    }
}
