package BankingEntities;

import java.time.LocalDate;

public final class Company extends BankingEntity {
    private String name;

    public Company(int id, String email, String phoneNumber, LocalDate joinDate, String name) {
        super(id, email, phoneNumber, joinDate);

        this.setName(name);
    }

    @Override
    public String getIdentity() {
        return name;
    }

    @Override
    public String toString() {
        return "Banking entity id: " + this.getID() +
                "\nCompany name: " + this.name +
                "\nJoin date: " + this.getJoinDate() + "\nContact:" +
                "\n\t- e-mail: " + this.getEmail() +
                "\n\t- phone number: " + this.getPhoneNumber();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
