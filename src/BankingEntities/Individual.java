package BankingEntities;

import java.time.LocalDate;

public final class Individual extends BankingEntity {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public Individual(String email, String phoneNumber, String firstName, String lastName, LocalDate birthDate) {
        super(email, phoneNumber);

        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setBirthDate(birthDate);
    }

    @Override
    public String getIdentity() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "First name: " + this.firstName + "\nLast name: " +
                this.lastName + "\nBirth date: " + this.birthDate +
                "\nJoin date: " + this.getJoinDate() + "\nContact:" +
                "\n\t- e-mail: " + this.getEmail() + "\n\t- phone number: " +
                this.getPhoneNumber();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
