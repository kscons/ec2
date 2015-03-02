package ec2.dynamodb;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class DynamoDBUtil {

    private static String DEFAULT_TABLE_NAME = "Logitech_test1";
    private static String ENDPOINT = "https://dynamodb.us-west-2.amazonaws.com";
    private DynamoDB dynamoDB;
    private static DynamoDBUtil instance = null;

    private DynamoDBUtil() {
        AmazonDynamoDBClient addbcl = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
        addbcl.setEndpoint(ENDPOINT);
        dynamoDB = new DynamoDB(addbcl);
    }


    public void insertRecord(Record record) {
        Table table = dynamoDB.getTable(DEFAULT_TABLE_NAME);
        Item item = new Item().withPrimaryKey("eventID", record.getEventID())
                .withNumber("userID", record.getUserId())
                .withNumber("machineID", record.getMachineId())
                .withNumber("time", record.getTime())
                .withString("eventType", record.getEventType())
                .withString("value", record.getValue());
        table.putItem(item);
    }

    public static DynamoDBUtil getInstance() {
        if (instance == null) {
            instance = new DynamoDBUtil();
        }
        return instance;
    }

}
