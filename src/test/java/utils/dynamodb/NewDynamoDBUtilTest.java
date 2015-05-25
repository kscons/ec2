package utils.dynamodb;

import entities.Log;
import entities.Metadata;
import org.junit.Test;
import utils.TestDataGenerator;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 18.05.15.
 */
public class NewDynamoDBUtilTest {
    @Test
    public void testInsertMetadataRecord() throws Exception {
        Metadata metadata = TestDataGenerator.getTestMetadata();
        NewDynamoDBUtil.insertRecord(metadata);
        assertTrue(NewDynamoDBUtil.isRecordExist(metadata));
    }

    @Test
    public void testGetAllMetadataRecords() throws Exception {
        ArrayList<Metadata> arrayList = TestDataGenerator.getMetadatasTesList();
        arrayList.stream()
                .forEach(NewDynamoDBUtil::insertRecord);

        NewDynamoDBUtil.getAllRecords(Metadata.class).stream()
                .forEach(current -> {
                    assertTrue(
                            arrayList.stream()
                                    .filter(metadata -> current.equals(metadata))
                                    .findAny()
                                    .isPresent());
                });
    }

    @Test
    public void testCleanMetadataTable() throws Exception {
        NewDynamoDBUtil.cleanTable(Log.class);
        assertFalse(NewDynamoDBUtil.isRecordExist(Log.class));
    }


    @Test
    public void testInsertLogRecord() throws Exception {
        Log log = TestDataGenerator.getTestLog();
        NewDynamoDBUtil.insertRecord(log);
        assertTrue(NewDynamoDBUtil.isRecordExist(log));
    }

    @Test
    public void testGetAllLogRecords() throws Exception {
        ArrayList<Log> arrayList = TestDataGenerator.getLogTesList();
        arrayList.stream()
                .forEach(NewDynamoDBUtil::insertRecord);

        NewDynamoDBUtil.getAllRecords(Log.class).stream()
                .forEach(current -> {
                    assertTrue(
                            arrayList.stream()
                                    .filter(log -> current.equals(log))
                                    .findAny()
                                    .isPresent());
                });
    }
    @Test
    public void testCleanLogTable() throws Exception {
        NewDynamoDBUtil.cleanTable(Log.class);
        assertFalse(NewDynamoDBUtil.isRecordExist(Log.class));
    }
}