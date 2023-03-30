package BankingEntities;

import Helpers.ValidationHandler;

import java.time.LocalDate;
import java.util.Set;

public class BankingEntityService {
    private BankingEntity[] entities;
    private int numberOfBankingEntities = 0;
    private static BankingEntityService service = null;

    private BankingEntityService() {
        entities = new BankingEntity[200];
    }

    public static BankingEntityService getService() {
        if (service == null)
            service = new BankingEntityService();

        return service;
    }

    // used by BankingProductService to query banking entities
    public BankingEntity getBankingEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", 0, numberOfBankingEntities);

        return entities[id];
    }

    private void printAllEntities() {
        for (int i = 0; i < numberOfBankingEntities; i++)
            System.out.println("\nBanking entity id: " + i + "\n" + entities[i]);
    }

    private void printEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", 0, numberOfBankingEntities);

        System.out.println("\nBanking entity id: " + id + "\n" + entities[id]);
    }

    private void registerEntity() {
        int choice = ValidationHandler.intValidator("Is the entity an individual(0) or a company(1)? ", "Invalid choice!", 0, 1);

        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "^\\+\\d{1,3}[- .]?\\d{7,15}");

        BankingEntity newEntity = null;
        switch (choice) {
            case 0:
                String firstName = ValidationHandler.stringValidator("Enter the first name of the individual: ", "Invalid first name!", "[A-Z][a-z]*");
                String lastName = ValidationHandler.stringValidator("Enter the last name of the individual: ", "Invalid last name!", "[A-Z][a-z]*");
                ;
                LocalDate birthDate = ValidationHandler.dateValidator("Enter the birth date of the individual: ", "Invalid birth date!", 18);

                newEntity = new Individual(email, phoneNumber, firstName, lastName, birthDate);
                break;

            case 1:
                String name = ValidationHandler.stringValidator("Enter the name of the company: ", "Invalid name!", ".+");

                newEntity = new Company(email, phoneNumber, name);
                break;
        }

        entities[numberOfBankingEntities++] = newEntity;
        System.out.println("Entity created successfully!");
    }

    private void updateEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", 0, numberOfBankingEntities);

        System.out.println("Multiple fields can be updated at once, by specifying them on the same line.");
        System.out.println("Editable fields:");
        System.out.println("\t1.E-mail");
        System.out.println("\t2.Phone Number");

        if (entities[id] instanceof Individual) {
            System.out.println("\t3.First Name");
            System.out.println("\t4.Last Name");
            System.out.println("\t5.Birth Date");

            Set<Integer> individualChoices = ValidationHandler.choicesValidator(1, 5);
            for (Integer choice : individualChoices) {
                Individual individual = (Individual) entities[id];

                switch (choice) {
                    case 1:
                        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                        individual.setEmail(email);
                        break;
                    case 2:
                        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "^\\+\\d{1,3}[- .]?\\d{7,15}");
                        individual.setPhoneNumber(phoneNumber);
                        break;
                    case 3:
                        String firstName = ValidationHandler.stringValidator("Enter the first name of the individual: ", "Invalid first name!", "[A-Z][a-z]*");
                        individual.setFirstName(firstName);
                        break;
                    case 4:
                        String lastName = ValidationHandler.stringValidator("Enter the last name of the individual: ", "Invalid last name!", "[A-Z][a-z]*");
                        individual.setLastName(lastName);
                        break;
                    case 5:
                        LocalDate birthDate = ValidationHandler.dateValidator("Enter the birth date of the individual: ", "Invalid birth date!", 18);
                        individual.setBirthDate(birthDate);
                        break;
                }
            }

        } else {
            System.out.println("\t3.Name");

            Set<Integer> companyChoices = ValidationHandler.choicesValidator(1, 3);
            for (Integer choice : companyChoices) {
                Company company = (Company) entities[id];

                switch (choice) {
                    case 1:
                        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                        company.setEmail(email);
                        break;
                    case 2:
                        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "^\\+\\d{1,3}[- .]?\\d{7,15}");
                        company.setPhoneNumber(phoneNumber);
                        break;
                    case 3:
                        String name = ValidationHandler.stringValidator("Enter the name of the company: ", "Invalid name!", ".+");
                        company.setName(name);
                        break;
                }
            }
        }

        System.out.println("Entity updated successfully!");
    }

    private void unregisterEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", 0, numberOfBankingEntities);

        for (int i = id; i < numberOfBankingEntities - 1; i++)
            entities[i] = entities[i + 1];
        numberOfBankingEntities--;

        System.out.println("Entity unregistered successfully!");
    }
}
