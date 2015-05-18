package utils.jsonprocessors;

import entities.Log;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Logitech on 08.05.15.
 */
public class JSONProcessorTest {
    private static final Log TEST_OBJECT_LOG = new Log("id", 1234, "os_version", "1422981452757", "6.3.9600.17056");

    @Test
    public void testGenerateJSONLog() throws Exception {
        assertEquals(LOG_JSON, JSONProcessor.generateJSON(TEST_OBJECT_LOG));
    }


    @Test
    public void testParseJSON() throws Exception {
        Log parsedLog = TEST_OBJECT_LOG;
        parsedLog.setId(null);
        parsedLog.setUserId(0);
        assertEquals(TEST_OBJECT_LOG, JSONProcessor.parseJSON(Log.class, LOG_JSON));
    }

    private static final String LOG_JSON = "{\"key\":\"os_version\",\"timestamp\":\"1422981452757\",\"value\":\"6.3.9600.17056\"}";
}