package exceptions.dynamodb;

/**
 * Created by Logitech on 15.05.15.
 */
public class NonExistTableException extends Exception{
    public NonExistTableException() {
        super();
    }

    public NonExistTableException(String message) {
        super(message);
    }

}
