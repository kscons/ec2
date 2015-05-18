package utils.dynamodb;

import entities.Metadata;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 18.05.15.
 */
public class NewDynamoDBUtilTest {
    private static final Metadata TEST_METADATA_OBJECT=new Metadata("eventID", 1234, 1234,new Date(), "eventtime", "value");
    private static final String METADATA_TABLE_NAME="Logitech_test_eu-west-1";
    @Test
    public void testInsertRecord() throws Exception {
        NewDynamoDBUtil.insertRecord(TEST_METADATA_OBJECT);
        assertTrue(NewDynamoDBUtil.isRecordExist(TEST_METADATA_OBJECT));
    }

    @Test
    public void testGetAllRecords() throws Exception {

    }

    @Test
    public void testCleanTable() throws Exception {

    }
}