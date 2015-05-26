package utils.dynamodb;

import entities.Log;
import entities.Metadata;
import exceptions.dynamodb.MetadataFieldNullException;
import exceptions.dynamodb.NonExistTableException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TestDataGenerator;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 08.05.15.
 */
public class DynamoDBUtilTest {
    private static final String TEST_TABLE_NAME = "testtable";
    private static final String TEST_TABLE_LOGS_NAME = "testtablelogs";
    private static final String TEST_TABLE_METADATAS = "testtablemetadatas";


    @Test
    public void testCreateTable() throws Exception {
        DynamoDBUtil.createTable(TEST_TABLE_NAME, 1, 1, "ID", "S");
        assertTrue(DynamoDBUtil.isTableExist(TEST_TABLE_NAME));
        DynamoDBUtil.deleteTable(TEST_TABLE_NAME);
        assertFalse(DynamoDBUtil.isTableExist(TEST_TABLE_NAME));
    }


    @Test
    public void testInsertMetadataRecord() throws Exception {
        DynamoDBUtil.createTableForMetadata(TEST_TABLE_METADATAS);
        final Metadata testMetadata=TestDataGenerator.getTestMetadata();
         DynamoDBUtil.insertMetadataRecord(TEST_TABLE_METADATAS,testMetadata );
        assertTrue(DynamoDBUtil.isMetadataObjectExist(TEST_TABLE_METADATAS, testMetadata));
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
        DynamoDBUtil.insertMetadataRecord("incorrectName", TestDataGenerator.getTestMetadata());
        DynamoDBUtil.deleteTable(TEST_TABLE_METADATAS);

    }

    @Test
    public void testInsertLogRecord() throws Exception {
        final Log log = TestDataGenerator.getTestLog();
        DynamoDBUtil.createTableForLogs(TEST_TABLE_LOGS_NAME);
        DynamoDBUtil.insertLogRecord(TEST_TABLE_LOGS_NAME, log);
        assertTrue(DynamoDBUtil.isLogObjectExist(TEST_TABLE_LOGS_NAME, log));
        DynamoDBUtil.deleteTable(TEST_TABLE_LOGS_NAME);
    }

    @Test
    public void testGetAllMetadataItemsRecords() throws Exception {
        final ArrayList<Metadata> metadataList = TestDataGenerator.getMetadatasTesList();

        DynamoDBUtil.createTableForMetadata(TEST_TABLE_METADATAS);
        metadataList.stream().forEach(m -> {
            try {
                DynamoDBUtil.insertMetadataRecord(TEST_TABLE_METADATAS, m);
            } catch (NonExistTableException | MetadataFieldNullException ne) {
            }
        });
        metadataList.stream().forEach(m -> {
            DynamoDBUtil.isMetadataObjectExist(TEST_TABLE_METADATAS, m);
        });
        DynamoDBUtil.deleteTable(TEST_TABLE_METADATAS);
    }

    @Test
    public void testGetAllLogItemsRecords() throws Exception {
        final ArrayList<Log> logList = TestDataGenerator.getLogTesList();
        DynamoDBUtil.createTableForLogs(TEST_TABLE_LOGS_NAME);
        logList.stream().forEach(l -> {
            DynamoDBUtil.insertLogRecord(TEST_TABLE_LOGS_NAME, l);
        });
        logList.stream().forEach(l -> {
            DynamoDBUtil.isLogObjectExist(TEST_TABLE_LOGS_NAME, l);
        });
        DynamoDBUtil.deleteTable(TEST_TABLE_LOGS_NAME);
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