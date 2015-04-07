package exceptions;

/**
 * Created by serhii on 02.04.15.
 */
public class NoConnectionException extends Exception {
    public NoConnectionException() {
        super();
    }

    public NoConnectionException(String message) {
        super(message);
    }

}
