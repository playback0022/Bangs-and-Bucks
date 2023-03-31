package BankingProducts;


import java.time.LocalDate;

public class Card {
    private final Account account;
    private final String number;
    private final LocalDate expiryDate;
    private final String CVV;

    public Card(Account account, String number, LocalDate expiryDate, String CVV) {
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

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getCVV() {
        return CVV;
    }

    public void depositSum(Double sum) {
        account.balance += sum;
    }

    public void withdrawSum(Double sum) {
        account.balance -= sum;
    }
}


