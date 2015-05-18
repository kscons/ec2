package utils.dynamodb;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import configurations.servicesconfigurators.DynamoDBConfiGurator;
import entities.Log;
import entities.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by serhii on 07.04.15.
 */
public class AsyncDynamoDBSaver {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBUtil.class);
    private static String ENDPOINT = "https://dynamodb.eu-west-1.amazonaws.com";

    private static ExecutorService executorService = Executors.newFixedThreadPool(100);
    private static AmazonDynamoDBClient addbcl = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
    private static DynamoDB  dynamoDB = new DynamoDB(addbcl);
    private static final  Table metadataTable=dynamoDB.getTable(DynamoDBConfiGurator.getMetadataOutputTable());
    private static final Table logsTable=dynamoDB.getTable(DynamoDBConfiGurator.getLogsDynamodbOutputTableName());

    static {
        //setting endpoint
        addbcl.setEndpoint(ENDPOINT);
    }


    public static void insertMetadataRecord(final Metadata metadata) throws IllegalArgumentException {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                     Item item = new Item().withPrimaryKey("eventID", metadata.getEventID())
                        .withNumber("userID", metadata.getUserId())
                        .withNumber("machineID", metadata.getMachineId())
                        .withString("lastmodified", metadata.getLastModified() + "")
                        .withString("eventType", metadata.getEventType())
                        .withString("value", metadata.getValue());
                metadataTable.putItem(item);
                LOG.info("\tDynamoDB : metadata written into" + ENDPOINT + "\n\ttable name -" + DynamoDBConfiGurator.getMetadataOutputTable());
            }
        });

    }


    public static void insertLogRecord(final Log log) throws IllegalArgumentException {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Item item = new Item().withPrimaryKey("id", log.getId())
                        .withNumber("userID", log.getUserId())
                        .withString("time", log.getTimestamp())
                        .withString("key", log.getKey())
                        .withString("value", log.getValue());
                logsTable.putItem(item);
                LOG.info("\tDynamoDB : log" + log + " written into" + ENDPOINT + "\n\ttable name -" + DynamoDBConfiGurator.getLogsDynamodbOutputTableName());
            }
        });

    }
    public static synchronized void  insertLogRecords(final ArrayList<Log> logs) throws IllegalArgumentException {
     for(Log log :logs){
         insertLogRecord(log);

     }
    }
/*
    public static synchronized void  insertMetadataRecords(final ArrayList<Metadata> metadatas) throws IllegalArgumentException {
        for(Metadata metadata :metadatas){
            insertMetadataRecord(metadata);

        }
    }
*/
}
