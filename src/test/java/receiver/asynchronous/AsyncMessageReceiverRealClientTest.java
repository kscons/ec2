package receiver.asynchronous;

import configurations.servicesconfigurators.MessageReceiversConfigurator;
import configurations.servicesconfigurators.RedshiftConfigurator;
import entities.Log;
import entities.Metadata;
import exceptions.ZIPFormatException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.s3.S3Util;
import utils.archiever.Decompresser;
import utils.dynamodb.NewDynamoDBUtil;
import utils.redshift.jdbc.RedshiftJDBCUtil;

import javax.jms.JMSException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 16.04.15.
 */
public class AsyncMessageReceiverRealClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageReceiver.class);

    private static boolean setUp = false;
    private static InputStream report;
    private static int countOfLogsInReport = 50;
    private static int reportCount = 1;

    private static int waiting = 30000;

    private static final String CLIENT_EXECUTE_STRING = "./analytics_test --gtest_filter=E* --bucket_name=\"logitech-analytics-ksolod-eu-west-1-sqsinput\"";

    @Before
    public void init() throws IOException {
        if (!setUp) {
            report = AsyncMessageReceiver.class.getClassLoader().getResourceAsStream("archive_sample.gz");
            Cleaner.clean();
            //System.exit(13);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AsyncMessageReceiver.main(new String[0]);
                    } catch (InterruptedException | JMSException ie) {
                        ie.printStackTrace();
                        System.exit(13);

                    }
                }

            }).start();

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(CLIENT_EXECUTE_STRING);
            LOG.info("[Test] ====== Client started");
            try {

                Thread.sleep(waiting);


            } catch (InterruptedException ite) {
                ite.printStackTrace();
            }

            LOG.info("[Test] ====== Waiting... server working");
            setUp = true;
        }

    }

    @Test
    public void testUnzip() {
        try {
            assertEquals(new String(Decompresser.decompress(report).toByteArray(), "UTF-8"), Mock.report);
        } catch (ZIPFormatException | UnsupportedEncodingException z) {

        }
    }



    @Test
    public void testInputBucketEmpty() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultInputBucketName()).size();
        assertEquals(0, countOfObjects);
    }

    @Test
    public void testOutputBucketCount() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultOutputBucketName()).size();
        assertEquals(reportCount, countOfObjects);
    }

    @Test
    public void testDynamoDBMetadataRecordsCount() {
        assertEquals(NewDynamoDBUtil.<Metadata>getAllRecords(Metadata.class).size(), reportCount);

    }

    @Test
    public void testDynamoDBLogRecordsCount() {
        assertEquals(NewDynamoDBUtil.<Log>getAllRecords(Log.class).size(), countOfLogsInReport * reportCount);
    }

    @Test
    public void testRedshiftLogRecordsCount() {
        assertEquals(RedshiftJDBCUtil.getAllLogsFromTable(RedshiftConfigurator.getLogsRedshiftOutputTableName()).size(), reportCount);
    }


}