package Helpers;

public class AdjustmentLog {
    private String field;
    private String initialValue;
    private String currentValue;

    public AdjustmentLog(String field, String initialValue, String currentValue) {
        this.field = field;
        this.initialValue = initialValue;
        this.currentValue = currentValue;
    }

    @Override
    public String toString() {
        return "\t• " + field + ": " + initialValue + " -> " + currentValue;
    }
}
