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
import studies.s3datasourcegenerator.S3DataSourceGenerator;
import utils.Cleaner;
import utils.s3.S3Util;
import utils.archiever.Decompresser;
import utils.dynamodb.NewDynamoDBUtil;
import utils.jsonprocessors.LogParser;
import utils.redshift.jdbc.RedshiftJDBCUtil;

import javax.jms.JMSException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Logitech on 17.04.15.
 */
public class AsyncMessageReceiverWithClientInfo {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageReceiver.class);

    private static boolean setUp = false;
    private static InputStream report;
    private static int countOfLogsInReport=50;
    private static int reportCount=4;
    private static int time=30;
    private int chechStateFrequency=20;

    @Before
    public void init() {
        if (!setUp) {
            report = AsyncMessageReceiver.class.getClassLoader().getResourceAsStream("archive_sample.gz");
            Cleaner.clean();
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
                    S3DataSourceGenerator.main(new String[0]);
                }

            }).start();

            try {
                for(int i=1;i<time*reportCount;i++){
                    Thread.sleep(1000);
                    LOG.info("[Test] ====== "+((time*reportCount)-i) +"  seconds remaining."+" Server is  working");
                    if (i%chechStateFrequency==0){
                        LOG.info("\t [TEST]  Objects(files) in INPUT BUCKET = "+S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultInputBucketName()).size());
                        LOG.info("\t [TEST]  Objects(files) in OUTPUT BUCKET = "+S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultOutputBucketName()).size());
                        LOG.info("\t [TEST]  Objects(metadata) in DYNAMODB  = " + NewDynamoDBUtil.getAllRecords(Metadata.class).size());
                        LOG.info("\t [TEST]  Objects(logs) in DYNAMODB  = " + NewDynamoDBUtil.<Log>getAllRecords(Log.class).size());
                        LOG.info("\t [TEST]  Objects(logs) in REDSHIFT  = " + RedshiftJDBCUtil.getAllLogsFromTable(RedshiftConfigurator.getLogsRedshiftOutputTableName()).size());




                    }
                }
                //  Thread.sleep(30000 * S3Runner.REPORT_COUNT);
            } catch (InterruptedException ite) {
                ite.printStackTrace();
            }


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
        assertEquals(LogParser.parseLog(Mock.report, 1).size(), countOfLogsInReport);
    }

    @Test
    public void testInputBucketCount() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultInputBucketName()).size();
        assertEquals(reportCount, countOfObjects);
    }

    @Test
    public void testOutputBucketCount() {
        int countOfObjects = S3Util.getAllObjectSummaries(MessageReceiversConfigurator.getDefaultOutputBucketName()).size();
        assertEquals(reportCount*2, countOfObjects);
    }

    @Test
    public void testDynamoDBMetadataRecordsCount() {
        assertEquals(NewDynamoDBUtil.<Metadata>getAllRecords(Metadata.class).size(),  reportCount);

    }
    @Test
    public void testDynamoDBLogRecordsCount() {
        assertEquals(NewDynamoDBUtil.<Log>getAllRecords(Log.class).size(), countOfLogsInReport * reportCount);
    }

    @Test
    public void testRedshiftLogRecordsCount() {
        assertEquals(RedshiftJDBCUtil.getAllLogsFromTable(RedshiftConfigurator.getLogsRedshiftOutputTableName()).size(), countOfLogsInReport * reportCount);
    }


}
