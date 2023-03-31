package Services;

import BankingProducts.TermDeposit;

import java.util.ArrayList;

public class TransferService extends AbstractService {
    private ArrayList<TermDeposit> transfers;
    private static TransferService service = null;

    private TransferService() {
        transfers = new ArrayList<TermDeposit>();
    }

    public static TransferService getService() {
        if (service == null)
            service = new TransferService();

        return service;
    }

    public void printAllEntities() {

    }
}
