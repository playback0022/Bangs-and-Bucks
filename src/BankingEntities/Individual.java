package BankingEntities;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public final class Individual extends BankingEntity {
    private String firstName;
    private String lastName;
    private Date birthDate;

    Individual(String email, String phoneNumber, String firstName, String lastName, Date birthDate) {
        super(email, phoneNumber);

        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setBirthDate(birthDate);
    }

    @Override
    public String toString() {
        return "Banking entity ID: " + this.getId() + "\nFirst name: " + this.firstName + "\nLast name: " +
                this.lastName + "\nBirth date: " + this.birthDate + "\nJoin date: " + this.getJoinDate() +
                "\nContact:" + "\n\t- e-mail: " + this.getEmail() + "\n\t- phone number: " +
                this.getPhoneNumber() + "\n\t- address: ";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(!Pattern.compile("[A-Z][a-z]*").matcher(firstName).matches())
            System.out.println("Invalid first name.");

        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(!Pattern.compile("[A-Z][a-z]*").matcher(lastName).matches())
            System.out.println("Invalid last name.");

        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        Date atLeast18 = calendar.getTime();

        if (birthDate.after(atLeast18))
            System.out.println("Customers must be at least 18 years old.");

        this.birthDate = birthDate;
    }

//    public static void main(String[] args) {
//        new Individual("tar.ne@proton.me", "+40746566723", "Dan", "Castan", new Date(10000));
//        new Individual("tar.ne@proton.me", "+ 746566723", "Dan", "Castan", new Date(10000));
//        new Individual("t.n@proton.me", "+40746566723", "Dan", "astan", new Date(10000));
//        new Individual("t.n@proton.me", "+40 746566723", "dan", "Castan", new Date(10000));
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.YEAR, -1);
//        new Individual("=1212t.n@proton.me", "+40-746566723", "Dan", "Castan", c.getTime());
//    }
}
