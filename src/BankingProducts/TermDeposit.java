package BankingProducts;

import BankingEntities.BankingEntity;

import java.time.LocalDate;

public class TermDeposit {
    private final BankingEntity owner;
    private Double amount;
    private final Currency currency;
    private final LocalDate constitutionDate;
    private final Integer durationInMonths;
    private final Double interestRate;
    private Boolean emptied = false;

    public TermDeposit(BankingEntity owner, Double amount, Currency currency, Integer durationInMonths, Double interestRate) {
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

    public Boolean emptyDeposit(Account receivingAccount) {
        // term deposit can only be emptied once
        if (emptied) {
            System.out.println("Error: Deposit already emptied!");
            return Boolean.FALSE;
        }

        // emptying a term deposit before the maturation date is forbidden
        if (LocalDate.now().isBefore(constitutionDate.plusMonths(durationInMonths))) {
            System.out.println("Error: The deposit is locked until " + constitutionDate.plusMonths(durationInMonths) + "!");
            return Boolean.FALSE;
        }

        amount += interestRate * amount;
        Double convertedAmount = amount * currency.getDollarConversionFactor() / receivingAccount.getCurrency().getDollarConversionFactor();
        receivingAccount.depositSum(convertedAmount);
        emptied = true;
        System.out.println("Term Deposit successfully emptied!");
        return Boolean.TRUE;
    }

    public BankingEntity getOwner() {
        return owner;
    }
}
