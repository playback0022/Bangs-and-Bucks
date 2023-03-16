package BankingEntities;

import java.util.Date;

public final class Company extends BankingEntity {
    private String name;
    private String registerNumber;

    Company(String email, String phoneNumber, String address, String name, String registerNumber) {
        super(email, phoneNumber, address);

        this.name = name;
        this.registerNumber = registerNumber;
    }

    @Override
    public String toString() {
        return "Banking entity ID: " + this.getId() + "\nCompany name: " + this.name + "\nRegister number: " +
                this.registerNumber + "\nJoin date: " + this.getJoinDate() + "\nContact:" + "\n\t- e-mail: " +
                this.getEmail() + "\n\t- phone number: " + this.getPhoneNumber() + "\n\t- address: ";
    }
}
