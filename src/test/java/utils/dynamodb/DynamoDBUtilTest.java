package utils.dynamodb;

import entities.Metadata;
import exceptions.dynamodb.MetadataFieldNullException;
import exceptions.dynamodb.NonExistTableException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 08.05.15.
 */
public class DynamoDBUtilTest {
    private static final String TEST_TABLE_NAME="testtable";
    private static final String TEST_TABLE_LOGS_NAME="testtablelogs";
    private static final String TEST_TABLE_METADATAS="testtablemetadatas";
    private static final Metadata TEST_METADATA_OBJECT=new Metadata("eventID", 1234, 1234,new Date(), "eventtime", "value");


    @Test
    public void testCreateAndDeleteTable() throws Exception {
        DynamoDBUtil.createTable(TEST_TABLE_NAME,1,1,"ID","S");
        assertTrue(DynamoDBUtil.isTableExist(TEST_TABLE_NAME));
        DynamoDBUtil.deleteTable(TEST_TABLE_NAME);
        assertFalse(DynamoDBUtil.isTableExist(TEST_TABLE_NAME));

    }

    @Test
    public void testInsertMetadataRecord() throws Exception {
    DynamoDBUtil.createTableForMetadata(TEST_TABLE_METADATAS);
        DynamoDBUtil.insertMetadataRecord(TEST_TABLE_METADATAS, TEST_METADATA_OBJECT);
        DynamoDBUtil.deleteTable(TEST_TABLE_METADATAS);

    }

    @Test(expected = MetadataFieldNullException.class)
    public void testInsertNullRecord() throws Exception {
        DynamoDBUtil.createTableForMetadata(TEST_TABLE_METADATAS);
        DynamoDBUtil.insertMetadataRecord(TEST_TABLE_METADATAS, null);
        DynamoDBUtil.deleteTable(TEST_TABLE_METADATAS);

    }
    @Test(expected = NonExistTableException.class)
    public void testInsertintoNonExistTable() throws Exception {
        DynamoDBUtil.createTableForMetadata(TEST_TABLE_METADATAS);
        DynamoDBUtil.insertMetadataRecord("incorrectName", TEST_METADATA_OBJECT);
        DynamoDBUtil.deleteTable(TEST_TABLE_METADATAS);

    }

    @Test
    public void testInsertLogRecord() throws Exception {

    }

    @Test
    public void testGetAllMetadataItemsRecords() throws Exception {

    }

    @Test
    public void testGetAllLogItemsRecords() throws Exception {

    }

    @Test
    public void testCleanLogsTable() throws Exception {

    }

    @Test
    public void testCleanMetadatasTable() throws Exception {

    }

    @Test
    public void testCleanTable() throws Exception {

    }

    @Test
    public void testCreateTableForLogs() throws Exception {
        DynamoDBUtil.createTableForLogs(TEST_TABLE_LOGS_NAME);

    }

    @Test
    public void testCreateTableForMetadata() throws Exception {

    }

    @Test
    public void testCreateTableForLogs1() throws Exception {

    }

    @Test
    public void testCreateTableForMetadata1() throws Exception {

    }
    @Test
    public void testGetListTables() throws Exception {

    }
    @Test
    public void isTableExist() throws Exception {

    }

}