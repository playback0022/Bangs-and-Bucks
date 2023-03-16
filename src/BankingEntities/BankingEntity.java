package BankingEntities;

import java.util.Date;
import java.util.regex.Pattern;

public abstract class BankingEntity {
    private static int numberOfInstantiatedEntities = 0;
    private int id;
    private String email;
    private String phoneNumber;
    private Date joinDate;

    public BankingEntity(String email, String phoneNumber) {
        this.id = numberOfInstantiatedEntities++;
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.joinDate = new Date();
    }

    public abstract String toString();

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches())
            System.out.println("Invalid email.");

        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!Pattern.compile("^\\+\\d{1,3}[- .]?\\d{7,15}").matcher(phoneNumber).matches())
            System.out.println("Invalid phone number.");

        this.phoneNumber = phoneNumber;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public static int getNumberOfInstantiatedEntities() {
        return numberOfInstantiatedEntities;
    }
}
