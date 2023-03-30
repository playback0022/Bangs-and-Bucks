package BankingProducts;

import BankingEntities.Company;
import BankingEntities.Individual;
import BankingProducts.Account;

import java.util.Date;

public class Card {
    private Account account;
    private String number;
    private Date expiryDate;
    private String CVV;

    public Card(Account account, String number, Date expiryDate, String CVV) {
        this.account = account;
        this.number = number;
        this.expiryDate = expiryDate;
        this.CVV = CVV;
    }

    @Override
    public String toString() {
        return  "Holder: " + account.getHolder().getIdentity() + "\nNumber: " + number + "\nExpiry: " + expiryDate + "\nCVV: " + CVV;
    }

    public Account getAccount() {
        return account;
    }

    public String getNumber() {
        return number;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getCVV() {
        return CVV;
    }

    public void depositSum(Double sum) {
        if (sum <= 0) {
            System.out.println("Invalid sum of money to deposit.");
            return;
        }

        account.balance += sum;
    }

    public void withdrawSum(Double sum) {
//        if (sum <= 0 || sum > balance) {
//            System.out.println("Invalid sum of money to withdraw.");
//            return;
//        }

        account.balance -= sum;
    }

    public Transaction transferSum(Double sum, Account receivingAccount, String description) {
//        if (sum <= 0 || sum > balance)
//            System.out.println("Invalid sum of money to withdraw.");

        account.balance -= sum;
        receivingAccount.balance += sum * account.currency.getDollarConversionFactor() / receivingAccount.currency.getDollarConversionFactor();

        return new Transaction(account, receivingAccount, sum, description, this);
    }
}


