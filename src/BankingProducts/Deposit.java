package BankingProducts;

import BankingEntities.BankingEntity;

import java.time.LocalDate;

public class Deposit {
    private final BankingEntity owner;
    private Double amount;
    private final Currency currency;
    private final LocalDate constitutionDate;
    private final Integer durationInMonths;
    private final Double interestRate;
    private Boolean emptied = false;

    public Deposit(BankingEntity owner, Double amount, Currency currency, Integer durationInMonths, Double interestRate) {
        this.owner = owner;
        this.amount = amount;
        this.currency = currency;
        this.constitutionDate = LocalDate.now();
        this.durationInMonths = durationInMonths;
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "Owner: " + owner.getIdentity() + "\nAmount: " + amount + currency.getIsoCode() +
                "\nConstitution Date: " + constitutionDate + "\nMaturation Date: " + constitutionDate.plusMonths(durationInMonths) +
                "\nInterest rate: " + interestRate * 100 + "%" + "\nEmptied: " + emptied;
    }

    public void emptyDeposit(Account receivingAccount) {
        if (emptied) {
            System.out.println("Deposit already emptied!");
            return;
        }

        if (LocalDate.now().isBefore(constitutionDate.plusMonths(durationInMonths))) {
            System.out.println("The deposit is locked until " + constitutionDate.plusMonths(durationInMonths) + "!");
            return;
        }

        if (!receivingAccount.getHolder().equals(owner)) {
            System.out.println("Invalid account!");
            return;
        }

        amount += interestRate * amount;
        Double convertedAmount = amount * currency.getDollarConversionFactor() / receivingAccount.getCurrency().getDollarConversionFactor();
        receivingAccount.depositSum(convertedAmount);
        emptied = true;
        System.out.println();
    }

    public BankingEntity getOwner() {
        return owner;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getConstitutionDate() {
        return constitutionDate;
    }

    public Integer getDurationInMonths() {
        return durationInMonths;
    }

    public Double getInterestRate() {
        return interestRate;
    }
}