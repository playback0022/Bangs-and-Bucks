package BankingEntities;

import java.time.LocalDate;

public abstract class BankingEntity {
    private final int id;
    private String email;
    private String phoneNumber;
    private final LocalDate joinDate;

    public BankingEntity(int id, String email, String phoneNumber, LocalDate joinDate) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.joinDate = joinDate;
    }

    public abstract String getIdentity();

    public int getID() {
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

    public LocalDate getJoinDate() {
        return joinDate;
    }
}
