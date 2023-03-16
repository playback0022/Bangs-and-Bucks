package BankingEntities;

import java.util.Date;

public abstract class BankingEntity {
    private static int numberOfInstantiatedEntities = 0;
    private int id;
    private String email;
    private String phoneNumber;
    private Date joinDate;

    public BankingEntity(String email, String phoneNumber, String address) {
        this.id = numberOfInstantiatedEntities++;
        this.email = email;
        this.phoneNumber = phoneNumber;
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
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public static int getNumberOfInstantiatedEntities() {
        return numberOfInstantiatedEntities;
    }
}
