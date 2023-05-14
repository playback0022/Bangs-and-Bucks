import Helpers.ValidationHandler;

import Services.BankingEntityService;
import Services.BankingProductService;

import java.sql.*;

// main entry-point to the banking services
class Bank {
    private Bank() {
    }

    private static void getHelp() {
        System.out.println(">>> HELP");
        System.out.println("- Navigation through the menus is achieved by providing the number\ncorresponding to the specific options you wish to select");
        System.out.println("- Multiple choices can be specified when selecting the desired options");
        System.out.println("- Option '-1' is reserved as the exit code of any choice-based menu");
        System.out.println("- Once any innermost option of a menu has been selected, the operation\ncannot be canceled");
        System.out.println("- Percentages will be provided as decimals");
        System.out.println("- Dates must be provided in the following format: 'yyyy-MM-dd'");
        System.out.println("- The provided phone numbers should be fully-qualified (the country\ncode must be included)");
    }

    public static void initBank() {
        boolean exit = false;

        while (!exit) {
            System.out.println(">>> MAIN MENU");
            System.out.println("1. Banking Entity Sub-Menu");
            System.out.println("2. Banking Product Sub-Menu");
            System.out.println("3. Help");
            System.out.println("4. Exit");


            int choice = ValidationHandler.intValidator("Enter your desired option: ", "Invalid option!", "Main", 1, 4);
            switch (choice) {
                case 1 -> BankingEntityService.getService().initService();
                case 2 -> BankingProductService.initService();
                case 3 -> getHelp();
                case 4 -> exit = true;
            }
        }
    }
}

public class Main {
    private static Connection bootstrapDatabase (String name, String port, String database, String user, String password) {
        System.out.println("[*] Attempting to establish database connection...");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://" + name + ":" + port + "/" + database + "?user=" + user + "&password=" + password);
        }
        catch (SQLException exception) {
            System.out.println("[*] Connection failed!");
            System.out.println("[*] The following error occurred: '" + exception.getMessage() + "'.");
            System.out.println("[*] Exiting...");
            System.exit(1);
        }

        System.out.println("[*] Connection established successfully.");

        try (Statement statement = connection.createStatement()) {
            System.out.println("[*] Creating 'Banking Entity' table...");
            statement.executeUpdate("create table BANKING_ENTITY (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "email VARCHAR(30) NOT NULL, phoneNumber VARCHAR(20) NOT NULL, join_date DATE NOT NULL)");
        }
        catch (SQLException exception) {}

        try (Statement statement = connection.createStatement()) {
            System.out.println("[*] Creating 'Individual' table...");
            statement.executeUpdate("create table INDIVIDUAL (id INT NOT NULL, first_name VARCHAR(20) NOT NULL, " +
                    "last_name VARCHAR(20) NOT NULL, birth_date DATE NOT NULL, CONSTRAINT individual_id_fk FOREIGN " +
                    "KEY (id) REFERENCES BANKING_ENTITY(id) ON DELETE CASCADE)");
        }
        catch (SQLException exception) {}

        try (Statement statement = connection.createStatement()) {
            System.out.println("[*] Creating 'Company' table...");
            statement.executeUpdate("create table COMPANY (id INT NOT NULL, name VARCHAR(20) NOT NULL, CONSTRAINT " +
                    "company_id_fk FOREIGN KEY (id) REFERENCES BANKING_ENTITY(id) ON DELETE CASCADE)");
        }
        catch (SQLException exception) {}

        try (Statement statement = connection.createStatement()) {
            System.out.println("[*] Creating 'Currency' table...");
            statement.executeUpdate("create table CURRENCY (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "iso_code VARCHAR(30) NOT NULL, dollar_conversion_factor FLOAT NOT NULL)");
        }
        catch (SQLException exception) {}

        System.out.println("[*] Database setup completed.");
        System.out.println("[*] Welcome to 'Bangs & Bucks'!\n");

        return connection;
    }

    private static void closeConnection(Connection connection) {
        try {
            connection.close();
        }
        catch (SQLException exception) {
            System.out.println("\n[*] Some error occurred...");
        }

        System.out.println("[*] Goodbye!");
    }

    public static void main(String[] args) {
        Connection connection = bootstrapDatabase(args[0], args[1], args[2], args[3], args[4]);

        Bank.initBank();

        closeConnection(connection);
    }
}