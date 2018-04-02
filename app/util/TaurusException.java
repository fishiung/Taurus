package util;

public class TaurusException extends RuntimeException {

    public TaurusException() {
    }

    public TaurusException(String message) {
        super(message);
    }

    public TaurusException(String message, Throwable cause) {
        super(message, cause);
    }
}
