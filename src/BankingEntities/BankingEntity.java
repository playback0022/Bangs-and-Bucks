package BankingEntities;

import java.time.LocalDate;

public abstract class BankingEntity {
    private String email;
    private String phoneNumber;
    private LocalDate joinDate;

    public BankingEntity(String email, String phoneNumber) {
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.joinDate = LocalDate.now();
    }

    public abstract String getIdentity();

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
