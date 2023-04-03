package BankingProducts;

public class Currency {
    private final String isoCode;
    // when transferring funds between accounts with different currencies,
    // the dollar conversion factor must be known in order for the exchange
    // to be correct
    private final Double dollarConversionFactor;

    public Currency(String isoCode, Double dollarConversionFactor) {
        this.isoCode = isoCode;
        this.dollarConversionFactor = dollarConversionFactor;
    }

    @Override
    public String toString() {
        return "ISO Code: " + isoCode + "\nDollar Conversion Factor: " + dollarConversionFactor;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public Double getDollarConversionFactor() {
        return dollarConversionFactor;
    }
}
