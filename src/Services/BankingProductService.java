package Services;

import Helpers.ValidationHandler;

import java.util.HashSet;

public class BankingProductService {
    public static void initService() {
        System.out.println(">>> Banking Product Menu Initiated");
        System.out.println("1. Currencies");
        System.out.println("2. Accounts");
        System.out.println("3. Term Deposits");
        System.out.println("4. Cards");
        System.out.println("5. Transactions");

        HashSet<Integer> choices = (HashSet<Integer>) ValidationHandler.choicesValidator("Transactions", 1, 5);
        for (Integer choice : choices)
            switch (choice) {
                case 1 -> CurrencyService.getService().initService();
                case 2 -> AccountService.getService().initService();
                case 3 -> TermDepositService.getService().initService();
                case 4 -> CardService.getService().initService();
                case 5 -> TransactionService.getService().initService();
            }
    }
}
