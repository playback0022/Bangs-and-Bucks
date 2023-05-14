package Helpers;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseManagement {
    private static Connection connection;

    private DatabaseManagement() {}

    public static void performBootstrap(String name, String port, String database, String user, String password) {
        System.out.println("[*] Attempting to establish database connection...");

        try {
            connection = DriverManager.getConnection("jdbc:mariadb://" + name + ":" + port + "/" + database + "?user=" + user + "&password=" + password);
        }
        catch (SQLException exception) {
            System.out.println("[*] Connection failed!");
            System.exit(1);
        }

        System.out.println("[*] Connection established successfully.");

        // Inheritance will be modelled by using separate tables for each class and referencing the
        // primary key of the superclass (Banking Entity) in both subclasses (Individual and Company);

        try (Statement statement = connection.createStatement()) {
            System.out.println("[*] Creating 'Banking Entity' table...");
            statement.executeUpdate("create table BANKING_ENTITY (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "email VARCHAR(30) NOT NULL, phone_number VARCHAR(20) NOT NULL, join_date DATE NOT NULL)");
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
    }

    public static Connection acquireConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        }
        catch (SQLException exception) {
            System.out.println("\n[*] Some error occurred...");
        }

        System.out.println("[*] Goodbye!");
    }
}
