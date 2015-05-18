package exceptions.dynamodb;

/**
 * Created by Logitech on 15.05.15.
 */
public class MetadataFieldNullException extends  Exception{
    public MetadataFieldNullException() {
        super();
    }

    public MetadataFieldNullException(String message) {
        super(message);
    }

}
