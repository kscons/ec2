package utils.dynamodb;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.*;
import entities.Log;
import entities.Metadata;
import exceptions.dynamodb.MetadataFieldNullException;
import exceptions.dynamodb.NonExistTableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class is a tool that is used to insert records (in our case it is metadata) into DynamoDB.
 * The default table's name is Logitech_test_eu-west-1,but we can change it in code with the help of the setter.
 * This code creates connection with DynamoDB through AmazonDynamoDBClient.
 * When we need to write data from another class we should get instance of this object and call method insertMetadataRecord.
 */

public class DynamoDBUtil {

    private static final int DEFAULT_READ_UNITS_COUNT = 5;
    private static final int DEFAULT_WRITE_UNITS_COUNT = 5;


    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBUtil.class);
    private static String ENDPOINT = "https://dynamodb.eu-west-1.amazonaws.com";
    private static DynamoDB dynamoDB;
    static AmazonDynamoDBClient addbcl;

    static {
        //creating connection in default value of region which was set in ENDPOINT.
        addbcl = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
        addbcl.setEndpoint(ENDPOINT);
        dynamoDB = new DynamoDB(addbcl);
    }

    /**
     * Method inserts metadata into DynamoDB table.
     * When you want to set different table name you should call setter and set your name.
     *
     * @param metadata -the prepared POJO object that was formed out of metadata from S3Object.
     */
    public static void insertMetadataRecord(final String tableName, final Metadata metadata)
            throws MetadataFieldNullException, NonExistTableException {
        try {
            Table table = dynamoDB.getTable(tableName);
            Item item = new Item().withPrimaryKey("eventID", metadata.getEventID())
                    .withNumber("userID", metadata.getUserId())
                    .withNumber("machineID", metadata.getMachineId())
                    .withString("lastmodified", metadata.getLastModified() + "")
                    .withString("eventType", metadata.getEventType())
                    .withString("value", metadata.getValue());
            table.putItem(item);
            LOG.info("\tDynamoDB : metadata written into" + ENDPOINT + "\n\ttable name -" + tableName);
        } catch (IllegalArgumentException iae) {
            throw new MetadataFieldNullException("Some field of object is null");
        } catch (Exception ex) {
            throw new NonExistTableException("the table" + tableName + "is not exist");
        }
    }


    public static void insertLogRecord(final String tableName, final Log log) throws IllegalArgumentException {
        Table table = dynamoDB.getTable(tableName);
        Item item = new Item().withPrimaryKey("id", log.getId())
                .withNumber("userID", log.getUserId())
                .withString("time", log.getTimestamp())
                .withString("key", log.getKey())
                .withString("value", log.getValue());
        table.putItem(item);
        LOG.info("\tDynamoDB : log" + log + " written into" + ENDPOINT + "\n\ttable name -" + tableName);
    }


    public static ArrayList<Metadata> getAllMetadataItemsRecords(String tableName) {
        ArrayList<Metadata> listItems = new ArrayList<>();
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName);

        ScanResult result = addbcl.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {

            Metadata metadata = new Metadata(item);
            listItems.add(metadata);

        }
        return listItems;
    }


    public static ArrayList<Log> getAllLogItemsRecords(String tableName) {
        ArrayList<Log> listItems = new ArrayList<>();
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName);

        ScanResult result = addbcl.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {

            Log log = new Log();
            log.setId(item.get("id").toString());
            log.setUserId(Long.parseLong(item.get("userID").toString()));
            log.setTimestamp(item.get("time").toString());
            log.setKey(item.get("key").toString());
            log.setValue(item.get("value").toString());

        }
        return listItems;
    }

    public static void cleanLogsTable(String tableName) {
        cleanTable(tableName, Entities.LOG);
    }

    public static void cleanMetadatasTable(String tableName) {
        cleanTable(tableName, Entities.METADATA);
    }

    public static void cleanTable(String tableName, Entities entity) {
        String hashKeyName = "";
        switch (entity) {
            case LOG: {
                hashKeyName = "id";
                break;
            }
            case METADATA: {
                hashKeyName = "eventID";
                break;
            }
        }
        DynamoDBMapper mapper = new DynamoDBMapper(addbcl);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Metadata> result = mapper.scan(Metadata.class, scanExpression);
        for (Metadata data : result) {
            mapper.delete(data);
        }

    }


/**
 * Operations with tables
 */
    /**
     * This method is used to create new table with one hashkey.
     * To create table it calls another basic method
     * Example of use :   createTable("lo", 10L, 5L, "Id", "N");
     *
     * @param tableName          - name of table that is created
     * @param readCapacityUnits  - count of read capacity units
     * @param writeCapacityUnits - count of write capacity units
     * @param hashKeyName        - name of hashkey
     * @param hashKeyType        - type of hashkey
     */
    public static void createTable(
            final String tableName, final long readCapacityUnits, final long writeCapacityUnits,
            final String hashKeyName, final String hashKeyType) {
        createTable(tableName, readCapacityUnits, writeCapacityUnits,
                hashKeyName, hashKeyType, null, null);
    }


    /**
     * This method deletes the table that we selected from Data base.
     *
     * @param tableName - name of table that will be deleted
     */
    public static void deleteTable(final String tableName) {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName
                    + " to be deleted...this may take a while...");
            table.waitForDelete();

        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

    /**
     * Basic method that creates tables.
     * It contains all options that you need.
     *
     * @param tableName          - name of table
     * @param readCapacityUnits  - count of read  capacity units
     * @param writeCapacityUnits - count of write  capacity units
     * @param hashKeyName
     * @param hashKeyType
     * @param rangeKeyName
     * @param rangeKeyType
     */
    private static void createTable(
            final String tableName, final long readCapacityUnits, final long writeCapacityUnits,
            final String hashKeyName, final String hashKeyType,
            final String rangeKeyName, final String rangeKeyType) {
    if (isTableExist(tableName)){
        try {

            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement()
                    .withAttributeName(hashKeyName)
                    .withKeyType(KeyType.HASH));

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName(hashKeyName)
                    .withAttributeType(hashKeyType));

            if (rangeKeyName != null) {
                keySchema.add(new KeySchemaElement()
                        .withAttributeName(rangeKeyName)
                        .withKeyType(KeyType.RANGE));
                attributeDefinitions.add(new AttributeDefinition()
                        .withAttributeName(rangeKeyName)
                        .withAttributeType(rangeKeyType));
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(readCapacityUnits)
                            .withWriteCapacityUnits(writeCapacityUnits));

            // If this is the Reply table, define a local secondary index
            if (rangeKeyName != null && rangeKeyType != null) {

                attributeDefinitions.add(new AttributeDefinition()
                        .withAttributeName("PostedBy")
                        .withAttributeType("S"));

                ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
                localSecondaryIndexes.add(new LocalSecondaryIndex()
                        .withIndexName("PostedBy-Index")
                        .withKeySchema(
                                new KeySchemaElement().withAttributeName(hashKeyName).withKeyType(KeyType.HASH),
                                new KeySchemaElement().withAttributeName("PostedBy").withKeyType(KeyType.RANGE))
                        .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY)));

                request.setLocalSecondaryIndexes(localSecondaryIndexes);
            }

            request.setAttributeDefinitions(attributeDefinitions);

            System.out.println("Issuing CreateTable request for " + tableName);
            Table table = dynamoDB.createTable(request);
            System.out.println("Waiting for " + tableName
                    + " to be created...this may take a while...");
            table.waitForActive();

        } catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }}else {LOG.error("Table is exist already");}
    }

    public static void createTableForLogs(final String tableName, int readUnits, int writeUnits) {
        createTable(tableName, readUnits, writeUnits, "Id", "S");
    }


    public static void createTableForMetadata(final String tableName, int readUnits, int writeUnits) {
        createTable(tableName, readUnits, writeUnits, "eventID", "S");
    }

    public static void createTableForLogs(final String tableName) {
        createTableForLogs(tableName, DEFAULT_READ_UNITS_COUNT, DEFAULT_WRITE_UNITS_COUNT);
    }

    public static void createTableForMetadata(final String tableName) {
        createTableForLogs(tableName, DEFAULT_READ_UNITS_COUNT, DEFAULT_WRITE_UNITS_COUNT);
    }

    public static ArrayList<Table> getTablesList() {
        DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
                new ProfileCredentialsProvider()));
        TableCollection<ListTablesResult> tables = dynamoDB.listTables();
        final ArrayList<Table> listOfTables = new ArrayList<>();
        tables.forEach(listOfTables::add);
        return listOfTables;
    }

    public static boolean isTableExist(final String tableName) {
        return getTablesList().stream()
                .filter(t -> t.getTableName() == tableName)
                .findAny()
                .isPresent();
    }

}

