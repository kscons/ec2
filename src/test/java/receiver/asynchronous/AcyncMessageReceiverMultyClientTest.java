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
import s3filesgenerator.S3Runner;
import utils.Cleaner;
import utils.archiever.Decompresser;
import utils.dynamodb.MapperDynamoDBUtil;
import utils.jsonprocessors.LogParser;
import utils.redshift.jdbc.RedshiftJDBCUtil;
import utils.s3.S3Util;

import javax.jms.JMSException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class AcyncMessageReceiverMultyClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageReceiver.class);

    private static boolean setUp = false;
    private static InputStream report;
    private static int countOfLogsInReport = 50;
    private static int reportCount = 2;
    private static int time = 40;
    private static int checkStatetime = 3000;
    private static int chechStateFrequency = 30;
    private static int clientCount = 5;


    @Before
    public void init() {
        if (!setUp) {
            S3Runner.REPORT_COUNT = reportCount;
            report = AsyncMessageReceiver.class.getClassLoader().getResourceAsStream("archive_sample.gz");
            Cleaner.clean();//System.exit(13);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new AsyncMessageReceiver("TestQueue").start();
                    } catch (InterruptedException | JMSException ie) {
                        ie.printStackTrace();
                        System.exit(13);

                    }
                }

            }).start();

          for (int i=0;i<clientCount;i++){


            new Thread(new Runnable() {
                @Override
                public void run() {
                    S3Runner.main(new String[0]);
                }

            }).start();}

            try {
                for (int i = 1; i < time * reportCount*clientCount; i++) {
                    Thread.sleep(1000);
                    LOG.info("[Test] ====== " + ((time * reportCount*clientCount) - i + (time * reportCount*clientCount % chechStateFrequency * checkStatetime / 1000)) + "  seconds remaining." + " Server is  working");
                    if (i % chechStateFrequency == 0) {
                        LOG.info("\t [TEST]  Objects(files) in INPUT BUCKET = " + S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultInputBucketName()).size());
                        LOG.info("\t [TEST]  Objects(files) in OUTPUT BUCKET = " + S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultOutputBucketName()).size());
                        LOG.info("\t [TEST]  Objects(metadata) in DYNAMODB  = " + MapperDynamoDBUtil.getAllRecords(Metadata.class).size());
                        LOG.info("\t [TEST]  Objects(logs) in DYNAMODB  = " + MapperDynamoDBUtil.<Log>getAllRecords(Log.class).size());
                        LOG.info("\t [TEST]  Objects(logs) in REDSHIFT  = " + RedshiftJDBCUtil.getAllLogsFromTable(RedshiftConfigurator.getLogsRedshiftOutputTableName()).size());
                        // Thread.sleep(checkStatetime);


                    }
                }
                //  Thread.sleep(30000 * ReportGenerator.REPORT_COUNT);
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
            z.printStackTrace();
        }
    }


    @Test
    public void testLogParser() {
        assertEquals(LogParser.parseLog(Mock.report, 1).size(), countOfLogsInReport);
    }

    @Test
    public void testInputBucketEmpty() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultInputBucketName()).size();
        assertEquals(0, countOfObjects);
    }

    @Test
    public void testOutputBucketCount() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultOutputBucketName()).size();
        assertEquals(S3Runner.REPORT_COUNT*clientCount, countOfObjects);
    }

    @Test
    public void testDynamoDBMetadataRecordsCount() {
        assertEquals(MapperDynamoDBUtil.<Metadata>getAllRecords(Metadata.class).size(), S3Runner.REPORT_COUNT*clientCount);

    }

    @Test
    public void testDynamoDBLogRecordsCount() {
        assertEquals(MapperDynamoDBUtil.<Log>getAllRecords(Log.class).size(), countOfLogsInReport * S3Runner.REPORT_COUNT*clientCount);
    }

    @Test
    public void testRedshiftLogRecordsCount() {
        assertEquals(RedshiftJDBCUtil.getAllLogsFromTable(RedshiftConfigurator.getLogsRedshiftOutputTableName()).size(), countOfLogsInReport * S3Runner.REPORT_COUNT*clientCount);
    }


}

