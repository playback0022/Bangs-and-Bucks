package Services;

import BankingEntities.BankingEntity;
import BankingEntities.Company;
import BankingEntities.Individual;
import Helpers.ValidationHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class BankingEntityService extends AbstractService {
    private final ArrayList<BankingEntity> entities;
    private static BankingEntityService service = null;

    private BankingEntityService() {
        entities = new ArrayList<BankingEntity>();
    }

    public static BankingEntityService getService() {
        if (service == null)
            service = new BankingEntityService();

        return service;
    }

    // used by BankingProductService to query banking entities
    public BankingEntity getBankingEntity(String shellIndicator) {
        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", shellIndicator, 0, entities.size());

        return entities.get(id);
    }

    public void printAllEntities() {
        for (int i = 0; i < entities.size(); i++)
            System.out.println("\nBanking entity id: " + i + "\n" + entities.get(i));
    }

    public void printAllIndividuals() {
        for (int i = 0; i < entities.size(); i++)
            if (entities.get(i) instanceof Individual)
                System.out.println("\nBanking entity id: " + i + "\n" + entities.get(i));
    }

    public void printAllCompanies() {
        for (int i = 0; i < entities.size(); i++)
            if (entities.get(i) instanceof Company)
                System.out.println("\nBanking entity id: " + i + "\n" + entities.get(i));
    }

    public void printEntity() {
        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", "Banking Entities", 0, entities.size());

        System.out.println("\nBanking entity id: " + id + "\n" + entities.get(id));
    }

    public void registerEntity() {
        int choice = ValidationHandler.intValidator("Is the entity an individual(0) or a company(1)? ", "Invalid choice!", "Banking Entities", 0, 1);

        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "Banking Entities", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "Banking Entities", "^\\+\\d{1,3}[- .]?\\d{7,15}");

        BankingEntity newEntity = null;
        switch (choice) {
            case 0:
                String firstName = ValidationHandler.stringValidator("Enter the first name of the individual: ", "Invalid first name!", "Banking Entities", "[A-Z][a-z]*");
                String lastName = ValidationHandler.stringValidator("Enter the last name of the individual: ", "Invalid last name!", "Banking Entities", "[A-Z][a-z]*");
                LocalDate birthDate = ValidationHandler.dateValidator("Enter the birth date of the individual: ", "Invalid birth date!",  "Banking Entities",18);

                newEntity = new Individual(email, phoneNumber, firstName, lastName, birthDate);
                break;

            case 1:
                String name = ValidationHandler.stringValidator("Enter the name of the company: ", "Invalid name!", "Banking Entities", ".+");

                newEntity = new Company(email, phoneNumber, name);
                break;
        }

        entities.add(newEntity);
        System.out.println("Entity registered successfully!");
    }

    public void updateEntity() {
        BankingEntity entity = getBankingEntity("Banking Entities");

        System.out.println("Multiple fields can be updated at once, by specifying them on the same line.");
        System.out.println("Editable fields:");
        System.out.println("\t1.E-mail");
        System.out.println("\t2.Phone Number");

        if (entity instanceof Individual) {
            System.out.println("\t3.First Name");
            System.out.println("\t4.Last Name");
            System.out.println("\t5.Birth Date");

            Set<Integer> individualChoices = ValidationHandler.choicesValidator("Banking Entities", 1, 5);
            for (Integer choice : individualChoices) {
                Individual individual = (Individual) entity;

                switch (choice) {
                    case 1:
                        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "Banking Entities", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                        individual.setEmail(email);
                        break;
                    case 2:
                        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "Banking Entities", "^\\+\\d{1,3}[- .]?\\d{7,15}");
                        individual.setPhoneNumber(phoneNumber);
                        break;
                    case 3:
                        String firstName = ValidationHandler.stringValidator("Enter the first name of the individual: ", "Invalid first name!", "Banking Entities", "[A-Z][a-z]*");
                        individual.setFirstName(firstName);
                        break;
                    case 4:
                        String lastName = ValidationHandler.stringValidator("Enter the last name of the individual: ", "Invalid last name!", "Banking Entities", "[A-Z][a-z]*");
                        individual.setLastName(lastName);
                        break;
                    case 5:
                        LocalDate birthDate = ValidationHandler.dateValidator("Enter the birth date of the individual: ", "Invalid birth date!", "Banking Entities", 18);
                        individual.setBirthDate(birthDate);
                        break;
                }
            }

        }

        else {
            System.out.println("\t3.Name");

            Set<Integer> companyChoices = ValidationHandler.choicesValidator("Banking Entities", 1, 3);
            for (Integer choice : companyChoices) {
                Company company = (Company) entity;

                switch (choice) {
                    case 1:
                        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "Banking Entities", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                        company.setEmail(email);
                        break;
                    case 2:
                        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "Banking Entities", "^\\+\\d{1,3}[- .]?\\d{7,15}");
                        company.setPhoneNumber(phoneNumber);
                        break;
                    case 3:
                        String name = ValidationHandler.stringValidator("Enter the name of the company: ", "Invalid name!", "Banking Entities", ".+");
                        company.setName(name);
                        break;
                }
            }
        }

        System.out.println("Entity updated successfully!");
    }

    public void unregisterEntity() {
        BankingEntity entity = getBankingEntity("Banking Entities");

        entities.remove(entity);
        System.out.println("Entity unregistered successfully!");
    }
}
