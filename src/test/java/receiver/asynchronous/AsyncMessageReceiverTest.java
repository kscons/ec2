package receiver.asynchronous;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import configurations.Configurator;
import configurations.servicesconfigurators.DynamoDBConfiGurator;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import configurations.servicesconfigurators.RedshiftConfigurator;
import entities.Log;
import entities.Metadata;
import exceptions.ZIPFormatException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s3filesgenerator.S3Runner;

import utils.S3Util;
import utils.archiever.Decompresser;
import utils.dynamodb.DynamoDBUtil;
import utils.dynamodb.NewDynamoDBUtil;
import utils.jsonprocessors.LogParser;
import utils.redshift.jdbc.RedshiftJDBCUtil;

import javax.jms.JMSException;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 16.04.15.
 */
public class AsyncMessageReceiverTest {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageReceiver.class);

    private static boolean setUp = false;
    private static InputStream report;
    private static int countOfLogsInReport=50;
    private static int reportCount=40;
    private static int time=50;

    @Before
    public void init() {
        if (!setUp) {
            S3Runner.REPORT_COUNT=reportCount;
            report = AsyncMessageReceiver.class.getClassLoader().getResourceAsStream("archive_sample.gz");
            Configurator.configureAll("config.properties");
            NewDynamoDBUtil.<Log>cleanTable(Log.class);
            NewDynamoDBUtil.<Metadata>cleanTable(Metadata.class);
            RedshiftJDBCUtil.deleteTable(RedshiftConfigurator.getLogsRedshiftOutputTableName());
            RedshiftJDBCUtil.createTableForLogs(RedshiftConfigurator.getLogsRedshiftOutputTableName());
            S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultOutputBucketName());
            S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultInputBucketName());
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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    S3Runner.main(new String[0]);
                }

            }).start();

            try {
                for(int i=1;i<time*reportCount;i++){
                    Thread.sleep(1000);
                    LOG.info("[Test] ====== "+((time*reportCount)-i) +"  seconds remaining."+" Server is  working");
                }
              //  Thread.sleep(30000 * S3Runner.REPORT_COUNT);
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
    public void testLogParser() {
        assertEquals(LogParser.parseLog(Mock.report, "test", 1).size(), countOfLogsInReport);
    }

    @Test
    public void testInputBucketEmpty() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultInputBucketName()).size();
        assertEquals(0, countOfObjects);
    }

    @Test
    public void testOutputBucketCount() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultOutputBucketName()).size();
        assertEquals(S3Runner.REPORT_COUNT, countOfObjects);
    }

    @Test
    public void testDynamoDBMetadataRecordsCount() {
        assertEquals(NewDynamoDBUtil.<Metadata>getAllRecords(Metadata.class).size(),  S3Runner.REPORT_COUNT);

    }
    @Test
    public void testDynamoDBLogRecordsCount() {
        assertEquals(NewDynamoDBUtil.<Log>getAllRecords(Log.class).size(), countOfLogsInReport * S3Runner.REPORT_COUNT);
    }

    @Test
    public void testRedshiftLogRecordsCount() {
        assertEquals(RedshiftJDBCUtil.getAllLogsFromTable(RedshiftConfigurator.getLogsRedshiftOutputTableName()).size(), countOfLogsInReport * S3Runner.REPORT_COUNT);
    }



}