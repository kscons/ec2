package utils.dynamodb;

import entities.Log;
import entities.Metadata;
import org.junit.Test;
import utils.TestDataGenerator;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 18.05.15.
 */
public class MapperDynamoDBUtilTest {
    @Test
    public void testInsertMetadataRecord() throws Exception {
        Metadata metadata = TestDataGenerator.getTestMetadata();
        MapperDynamoDBUtil.insertRecord(metadata);
        assertTrue(MapperDynamoDBUtil.isRecordExist(metadata));
    }

    @Test
    public void testGetAllMetadataRecords() throws Exception {
        ArrayList<Metadata> arrayList = TestDataGenerator.getMetadatasTesList();
        MapperDynamoDBUtil.cleanTable(Metadata.class);

        arrayList.stream()
                .forEach(MapperDynamoDBUtil::insertRecord);

        MapperDynamoDBUtil.getAllRecords(Metadata.class).stream()
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
        MapperDynamoDBUtil.cleanTable(Log.class);
        assertFalse(MapperDynamoDBUtil.isRecordExist(Log.class));
    }


    @Test
    public void testInsertLogRecord() throws Exception {
        Log log = TestDataGenerator.getTestLog();
        MapperDynamoDBUtil.insertRecord(log);
        assertTrue(MapperDynamoDBUtil.isRecordExist(log));
    }

    @Test
    public void testGetAllLogRecords() throws Exception {
        ArrayList<Log> arrayList = TestDataGenerator.getLogTesList();

        MapperDynamoDBUtil.cleanTable(Log.class);


        arrayList.stream()
                .forEach(MapperDynamoDBUtil::insertRecord);
        ArrayList<Log> arrayList1=   MapperDynamoDBUtil.getAllRecords(Log.class);
        MapperDynamoDBUtil.getAllRecords(Log.class).stream()
                .forEach(current -> {
                    assertTrue(
                            arrayList.stream()
                                    .filter(log -> current.getId().equals(log.getId()))
                                    .findAny()
                                    .isPresent());
                });


    }
    @Test
    public void testCleanLogTable() throws Exception {
        MapperDynamoDBUtil.cleanTable(Log.class);
        assertFalse(MapperDynamoDBUtil.isRecordExist(Log.class));
    }
}