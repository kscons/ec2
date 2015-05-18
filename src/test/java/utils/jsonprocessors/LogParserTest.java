package utils.jsonprocessors;

import entities.Log;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Logitech on 08.05.15.
 */
public class LogParserTest {
    @BeforeClass
    public static void initExpectedList() throws Exception {
        EXPECTED_LIST_OF_LOGS.add(new Log("id", USER_ID, "os_version", "1422981452757", "6.3.9600.17056"));
        EXPECTED_LIST_OF_LOGS.add(new Log("id", USER_ID, "os_type", "1422981452757", "windows"));
        EXPECTED_LIST_OF_LOGS.add(new Log("id", USER_ID, "app_change", "1422981452828", "analytics_test.exe;0.0.0.0"));
        EXPECTED_LIST_OF_LOGS.add(new Log("id", USER_ID, "app_change", "1422981453321", "devenv.exe;12.0.21005.1"));

    }

    @Test
    public void testLogParser() {
        assertEquals(EXPECTED_LIST_OF_LOGS, LogParser.parseLog(LOGS_STRING, USER_ID));
    }

    private static final int USER_ID = 12345;
    private static ArrayList<Log> EXPECTED_LIST_OF_LOGS = new ArrayList<>();
    private static final String LOGS_STRING = "[{\"key\":\"os_version\",\"timestamp\":1422981452757,\"value\":\"6.3.9600.17056\"},\n" +
            "  {\"key\":\"os_type\",\"timestamp\":1422981452757,\"value\":\"windows\"},\n" +
            "  {\"key\":\"app_change\",\"timestamp\":1422981452828,\"value\":\"analytics_test.exe;0.0.0.0\"},\n" +
            "  {\"key\":\"app_change\",\"timestamp\":1422981453321,\"value\":\"devenv.exe;12.0.21005.1\"}]";
}