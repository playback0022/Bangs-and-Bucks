package BankingEntities;

import java.util.Date;

public final class Individual extends BankingEntity {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String socialSecurityNumber;

    Individual(String email, String phoneNumber, String address, String firstName, String lastName, Date birthDate, String socialSecurityNumber) {
        super(email, phoneNumber, address);

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this. socialSecurityNumber = socialSecurityNumber;
    }

    @Override
    public String toString() {
        return "Banking entity ID: " + this.getId() + "\nFirst name: " + this.firstName + "\nLast name: " +
                this.lastName + "\nSocial security number: " + this.socialSecurityNumber + "\nBirth date: " +
                this.birthDate + "\nJoin date: " + this.getJoinDate() + "\nContact:" + "\n\t- e-mail: " +
                this.getEmail() + "\n\t- phone number: " + this.getPhoneNumber() + "\n\t- address: ";
    }
}
