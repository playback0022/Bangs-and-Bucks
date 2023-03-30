package BankingProducts;

import BankingEntities.BankingEntity;


public class SavingsAccount extends Account {
    private Double interestRate;

    public SavingsAccount(BankingEntity holder, String IBAN, Currency currency, Double interestRate) {
        super(holder,IBAN,currency);

        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return super.toString() + "\nInterest rate: " + interestRate * 100 + "%";
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void updateBalance() {
//        if (!today is the first of a month)
        System.out.println("Come back later.");

        balance += balance * interestRate;
    }
}
