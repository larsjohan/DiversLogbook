package no.ntnu.diverslogbook.exceptions;

public class DangerousDiveException extends RuntimeException {

    public DangerousDiveException() {
        super("The calculated values defines this dive as dangerous");
    }

    public DangerousDiveException(String message) {
        super(message);
    }
}
