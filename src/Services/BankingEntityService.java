package Services;

import BankingEntities.BankingEntity;
import BankingEntities.Company;
import BankingEntities.Individual;
import Helpers.AdjustmentLog;
import Helpers.AuditEngine;
import Helpers.DatabaseManagement;
import Helpers.ValidationHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BankingEntityService extends AbstractService {
    private final ArrayList<BankingEntity> entities;
    private static BankingEntityService service = null;

    private BankingEntityService() {
        entities = new ArrayList<>();

        // 'Individual' entries are retrieved first
        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
            ResultSet databaseIndividuals = statement.executeQuery("SELECT * FROM INDIVIDUAL i join BANKING_ENTITY b on i.id = b.id");

            // iterating through returned result set, creating objects
            // based on the column contents and adding them to the array;
            while (databaseIndividuals.next()) {
                Individual currentIndividual = new Individual(databaseIndividuals.getInt("id"), databaseIndividuals.getString("email"),
                                                            databaseIndividuals.getString("phone_number"), databaseIndividuals.getDate("join_date").toLocalDate(),
                                                            databaseIndividuals.getString("first_name"), databaseIndividuals.getString("last_name"), databaseIndividuals.getDate("birth_date").toLocalDate());
                entities.add(currentIndividual);
            }
        }
        catch (SQLException exception) {
            System.exit(1);
        }

        // 'Company' entries are retrieved separately
        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
            ResultSet databaseCompanies = statement.executeQuery("SELECT * FROM COMPANY c join BANKING_ENTITY b on c.id = b.id");

            // iterating through returned result set, creating objects
            // based on the column contents and adding them to the array;
            while (databaseCompanies.next()) {
                Company currentCompany = new Company(databaseCompanies.getInt("id"), databaseCompanies.getString("email"),
                                                    databaseCompanies.getString("phone_number"), databaseCompanies.getDate("join_date").toLocalDate(),
                                                    databaseCompanies.getString("name"));
                entities.add(currentCompany);
            }
        }
        catch (SQLException exception) {
            System.exit(1);
        }
    }

    public static BankingEntityService getService() {
        if (service == null)
            service = new BankingEntityService();

        return service;
    }

    public BankingEntity getBankingEntity(String shellIndicator) {
        if (entities.isEmpty()) {
            System.out.println("Warning: No banking entities registered so far.");
            return null;
        }

        int id = ValidationHandler.intValidator("Enter the id of the desired banking entity: ", "Invalid id!", shellIndicator, 1, Integer.MAX_VALUE);

        for (BankingEntity entity : entities)
            if (entity.getID() == id)
                return entity;

        System.out.println("Error: The provided id could not be found!");
        return null;
    }

    @Override
    protected void printAllEntities() {
        for (int i = 0; i < entities.size(); i++) {
            System.out.println("------------------------------\n" + entities.get(i));
            if (i == entities.size() - 1)
                System.out.println("------------------------------");
        }

        AuditEngine.log("Banking Entities - List all banking entities", null);
    }

    private void printAllIndividuals() {
        for (int i = 0; i < entities.size(); i++)
            if (entities.get(i) instanceof Individual)
                System.out.println("------------------------------\n" + entities.get(i));

        AuditEngine.log("Banking Entities - List all individuals", null);
    }

    private void printAllCompanies() {
        for (int i = 0; i < entities.size(); i++)
            if (entities.get(i) instanceof Company)
                System.out.println("------------------------------\n" + entities.get(i));

        AuditEngine.log("Banking Entities - List all banking companies", null);
    }

    @Override
    protected void printEntity() {
        if (entities.isEmpty()) {
            System.out.println("Warning: No banking entities registered so far.");
            return;
        }

        BankingEntity entity = this.getBankingEntity("Banking Entities");

        if (entity == null)
            return;

        System.out.println("------------------------------\n" + entity + "\n------------------------------");
        AuditEngine.log("Banking Entities - List banking entity by id (" + entity.getID() + ")", null);
    }

    @Override
    protected void registerEntity() {
        int choice = ValidationHandler.intValidator("Is the entity an individual(0) or a company(1)? ", "Invalid choice!", "Banking Entities", 0, 1);

        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "Banking Entities", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "Banking Entities", "^\\+\\d{1,3}[- .]?\\d{7,15}");

        BankingEntity newEntity = null;
        switch (choice) {
            case 0 -> {
                String firstName = ValidationHandler.stringValidator("Enter the first name of the individual: ", "Invalid first name!", "Banking Entities", "[A-Z][a-z]*");
                String lastName = ValidationHandler.stringValidator("Enter the last name of the individual: ", "Invalid last name!", "Banking Entities", "[A-Z][a-z]*");
                LocalDate birthDate = ValidationHandler.dateValidator("Enter the birth date of the individual: ", "Invalid birth date!", "Banking Entities", 18);
                int id = 0;

                // register new entity into database
                try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                    statement.executeUpdate("INSERT INTO BANKING_ENTITY (email, phone_number, join_date) VALUES ('" + email + "', '"  + phoneNumber + "', " +  "CURDATE())", Statement.RETURN_GENERATED_KEYS);
                    // the auto-incremented id in the database should correspond to the
                    // in-memory copy, therefore the generated id is requested and stored
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    generatedKeys.next();
                    id = generatedKeys.getInt(1);
                }
                catch (SQLException exception) {
                    System.exit(1);
                }

                try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                    statement.executeUpdate("INSERT INTO INDIVIDUAL VALUES ('" + id + "', '" + firstName + "', '"  + lastName + "', '" + birthDate + "')");
                }
                catch (SQLException exception) {
                    System.exit(1);
                }

                newEntity = new Individual(id, email, phoneNumber, LocalDate.now(), firstName, lastName, birthDate);
                AuditEngine.log("Banking Entities - Register individual with id=" + id, null);
            }
            case 1 -> {
                String name = ValidationHandler.stringValidator("Enter the name of the company: ", "Invalid name!", "Banking Entities", ".+");
                int id = 0;

                // register new entity into database
                try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                    statement.executeUpdate("INSERT INTO BANKING_ENTITY (email, phone_number, join_date) VALUES ('" + email + "', '"  + phoneNumber + "', " +  "CURDATE())", Statement.RETURN_GENERATED_KEYS);
                    // the auto-incremented id in the database should correspond to the
                    // in-memory copy, therefore the generated id is requested and stored
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    generatedKeys.next();
                    id = generatedKeys.getInt(1);
                }
                catch (SQLException exception) {
                    System.exit(1);
                }

                try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                    statement.executeUpdate("INSERT INTO COMPANY VALUES ('" + id + "', '" + name + "')");
                }
                catch (SQLException exception) {
                    System.exit(1);
                }

                newEntity = new Company(id, email, phoneNumber, LocalDate.now(), name);
                AuditEngine.log("Banking Entities - Register company with id=" + id, null);
            }
        }

        entities.add(newEntity);
        System.out.println("Entity registered successfully!");
    }

    private void updateEntity() {
        BankingEntity entity = getBankingEntity("Banking Entities");

        if (entity == null)
            return;

        System.out.println(">>> Editable fields:");
        System.out.println("1.E-mail");
        System.out.println("2.Phone Number");

        if (entity instanceof Individual) {
            System.out.println("3.First Name");
            System.out.println("4.Last Name");
            System.out.println("5.Birth Date");

            Individual individual = (Individual) entity;
            Set<Integer> individualChoices = ValidationHandler.choicesValidator("Banking Entities", 1, 5);
            ArrayList<AdjustmentLog> logs = new ArrayList<>();

            for (Integer choice : individualChoices) {
                switch (choice) {
                    case 1 -> {
                        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "Banking Entities", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                        logs.add(new AdjustmentLog("email", individual.getEmail(), email));
                        individual.setEmail(email);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE BANKING_ENTITY SET email='" + email + "' WHERE id=" + individual.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                    case 2 -> {
                        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "Banking Entities", "^\\+\\d{1,3}[- .]?\\d{7,15}");
                        logs.add(new AdjustmentLog("phone number", individual.getPhoneNumber(), phoneNumber));
                        individual.setPhoneNumber(phoneNumber);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE BANKING_ENTITY SET phone_number='" + phoneNumber + "' WHERE id=" + individual.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                    case 3 -> {
                        String firstName = ValidationHandler.stringValidator("Enter the first name of the individual: ", "Invalid first name!", "Banking Entities", "[A-Z][a-z]*");
                        logs.add(new AdjustmentLog("first name", individual.getFirstName(), firstName));
                        individual.setFirstName(firstName);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE INDIVIDUAL SET first_name='" + firstName + "' WHERE id=" + individual.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                    case 4 -> {
                        String lastName = ValidationHandler.stringValidator("Enter the last name of the individual: ", "Invalid last name!", "Banking Entities", "[A-Z][a-z]*");
                        logs.add(new AdjustmentLog("last name", individual.getLastName(), lastName));
                        individual.setLastName(lastName);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE INDIVIDUAL SET last_name='" + lastName + "' WHERE id=" + individual.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                    case 5 -> {
                        LocalDate birthDate = ValidationHandler.dateValidator("Enter the birth date of the individual: ", "Invalid birth date!", "Banking Entities", 18);
                        logs.add(new AdjustmentLog("birth date", individual.getBirthDate().toString(), birthDate.toString()));
                        individual.setBirthDate(birthDate);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE INDIVIDUAL SET birth_date='" + birthDate + "' WHERE id=" + individual.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                }
            }

            AuditEngine.log("Banking Entities - Update individual with id=" + individual.getID(), logs);

        }

        else {
            System.out.println("3.Name");

            Company company = (Company) entity;
            Set<Integer> companyChoices = ValidationHandler.choicesValidator("Banking Entities", 1, 3);
            ArrayList<AdjustmentLog> logs = new ArrayList<>();

            for (Integer choice : companyChoices) {
                switch (choice) {
                    case 1 -> {
                        String email = ValidationHandler.stringValidator("Enter the email of the entity: ", "Invalid email!", "Banking Entities", "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                        logs.add(new AdjustmentLog("email", company.getEmail(), email));
                        company.setEmail(email);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE BANKING_ENTITY SET email='" + email + "' WHERE id=" + company.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                    case 2 -> {
                        String phoneNumber = ValidationHandler.stringValidator("Enter the phone number of the entity: ", "Invalid phone number!", "Banking Entities", "^\\+\\d{1,3}[- .]?\\d{7,15}");
                        logs.add(new AdjustmentLog("phone number", company.getPhoneNumber(), phoneNumber));
                        company.setPhoneNumber(phoneNumber);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE BANKING_ENTITY SET phone_number='" + phoneNumber + "' WHERE id=" + company.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                    case 3 -> {
                        String name = ValidationHandler.stringValidator("Enter the name of the company: ", "Invalid name!", "Banking Entities", ".+");
                        logs.add(new AdjustmentLog("name", company.getName(), name));
                        company.setName(name);

                        // mirroring changes
                        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
                            statement.executeUpdate("UPDATE COMPANY SET name='" + name + "' WHERE id=" + company.getID());
                        }
                        catch (SQLException e) {
                            System.exit(1);
                        }
                    }
                }
            }

            AuditEngine.log("Banking Entities - Update company with id=" + company.getID(), logs);
        }

        System.out.println("Entity updated successfully!");
    }

    @Override
    protected void unregisterEntity() {
        BankingEntity entity = getBankingEntity("Banking Entities");

        if (entity == null)
            return;

        entities.remove(entity);

        try (Statement statement = DatabaseManagement.acquireConnection().createStatement()) {
            statement.executeUpdate("DELETE FROM BANKING_ENTITY where id=" + entity.getID());
        }
        catch (SQLException exception) {
            System.exit(1);
        }

        AuditEngine.log("Banking Entities - Unregister banking entity with id=" + entity.getID(), null);
        System.out.println("Entity unregistered successfully!");
    }

    @Override
    public void initService() {
        System.out.println(">>> Banking Entity Menu Initiated");
        System.out.println("1. Register banking entity");

        if (entities.isEmpty()) {
            HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Banking Entities", 1, 1);
            if (!choices.isEmpty())
                registerEntity();
            return;
        }

        System.out.println("2. Update entity");
        System.out.println("3. Unregister entity");
        System.out.println("4. List all banking entities");
        System.out.println("5. List all individuals");
        System.out.println("6. List all companies");
        System.out.println("7. List banking entity by id");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Banking Entities", 1, 7);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> registerEntity();
                case 2 -> updateEntity();
                case 3 -> unregisterEntity();
                case 4 -> printAllEntities();
                case 5 -> printAllIndividuals();
                case 6 -> printAllCompanies();
                case 7 -> printEntity();
            }
    }
}
