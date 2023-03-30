package Helpers;

public class InvalidFieldRuntimeException extends RuntimeException {
    public InvalidFieldRuntimeException(String errorMessage) {
        super(errorMessage);
    }
}
