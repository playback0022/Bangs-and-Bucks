package BankingEntities;

public final class Company extends BankingEntity {
    private String name;

    Company(String email, String phoneNumber, String name) {
        super(email, phoneNumber);

        this.setName(name);
    }

    @Override
    public String getIdentity() {
        return name;
    }

    @Override
    public String toString() {
        return "Company name: " + this.name + "\nJoin date: " + this.getJoinDate()
                + "\nContact:" + "\n\t- e-mail: " + this.getEmail() +
                "\n\t- phone number: " + this.getPhoneNumber();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
