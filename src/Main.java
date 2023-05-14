import Helpers.DatabaseManagement;
import Helpers.ValidationHandler;

import Services.BankingEntityService;
import Services.BankingProductService;

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
    public static void main(String[] args) {
        DatabaseManagement.performBootstrap(args[0], args[1], args[2], args[3], args[4]);
        Bank.initBank();
        DatabaseManagement.closeConnection();
    }
}