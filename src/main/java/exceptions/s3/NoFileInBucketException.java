package exceptions.s3;

/**
 * Created by Logitech on 12.05.15.
 */
public class NoFileInBucketException extends Exception {
    public NoFileInBucketException() {
        super();
    }

    public NoFileInBucketException(String message) {
        super(message);
    }

}
