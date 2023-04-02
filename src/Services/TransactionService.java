package Services;

import BankingProducts.TermDeposit;

import java.util.ArrayList;

public class TransactionService extends AbstractService {
    private ArrayList<TermDeposit> transfers;
    private static TransactionService service = null;

    private TransactionService() {
        transfers = new ArrayList<TermDeposit>();
    }

    public static TransactionService getService() {
        if (service == null)
            service = new TransactionService();

        return service;
    }

    public void printAllEntities() {

    }
}
