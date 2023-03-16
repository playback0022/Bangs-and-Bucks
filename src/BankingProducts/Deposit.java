package BankingProducts;

import BankingEntities.BankingEntity;

import java.util.Date;

public class Deposit {
    private BankingEntity owner;
    private Double amount;
    private Date constitutionDate;
    private Integer duration;
    private Double interestRate;

    public void emptyDeposit(Account receivingAccount) {
        if (!receivingAccount.getHolder().equals(owner))
            System.out.println("Invalid account.");
    }

    public BankingEntity getOwner() {
        return owner;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getConstitutionDate() {
        return constitutionDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public Double getInterestRate() {
        return interestRate;
    }
}
