package utils.redshift.jdbc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Logitech on 08.05.15.
 */
public class RedshiftJDBCUtilTest {
    private static final String TEST_LOGS_TABLE_NAME = "testlogs";

    @BeforeClass
    public static void testCreateConnection() throws Exception {
        RedshiftJDBCUtil.createConnection();
    }

    @Test
    public void testCreateTableForLogs() throws Exception {
        RedshiftJDBCUtil.createTableForLogs(TEST_LOGS_TABLE_NAME);

    }

    @Test
    public void testDeleteTable() throws Exception {

    }

    @Test
    public void testInsertLog() throws Exception {

    }

    @Test
    public void testInsertLog1() throws Exception {

    }

    @Test
    public void testGetAllLogsFromTable() throws Exception {

    }

    @Test
    public void testGetLogsFromTableByIDRange() throws Exception {

    }

    @Test
    public void testGetLogsFromTableByValue() throws Exception {

    }

    @Test
    public void testGetLogsFromTableByKey() throws Exception {

    }

    @AfterClass
    public static void testProcessQuery() throws Exception {
        RedshiftJDBCUtil.deleteTable(TEST_LOGS_TABLE_NAME);
    }
}