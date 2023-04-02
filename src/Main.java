import Helpers.ValidationHandler;

import Services.BankingEntityService;
import Services.BankingProductService;

class Bank {
    private Bank() {
    }

    private static void getHelp() {
        // -1 to exit choice menu
    }

    public static void initBank() {
        boolean exit = false;

        while (!exit) {
            System.out.println(">>> MAIN MENU");
            System.out.println("1. Banking Entity Sub-Menu");
            System.out.println("2. Banking Product Sub-Menu");
            System.out.println("3. Help");
            System.out.println("4. Exit");


            int choice = ValidationHandler.intValidator("Enter your desired option: ", "Invalid option!", "Main", 0, 4);
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
        Bank.initBank();
    }
}