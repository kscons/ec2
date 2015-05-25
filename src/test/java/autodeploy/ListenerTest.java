package autodeploy;

import autodeploy.entities.dynamodbtable.DynamoDBLogsTable;
import autodeploy.entities.dynamodbtable.DynamoDBMetadataTable;
import autodeploy.entities.s3buckets.InputBucket;
import autodeploy.entities.s3buckets.OutputBucket;
import autodeploy.entities.sqs.SQS;
import org.junit.*;
import receiver.asynchronous.AsyncMessageReceiver;
import utils.dynamodb.DynamoDBUtil;
import utils.jsonprocessors.JSONProcessor;
import utils.s3.S3Util;
import utils.sqs.SQSUtil;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 25.05.15.
 */
public class ListenerTest {
    private static Listener listener = new Listener("deploy-sqs-test-1");

    @BeforeClass
    public static void setUp() throws Exception {
        ArrayList<InputBucket> inputBuckets = new ArrayList<>();
        inputBuckets.add(new InputBucket("deploy-input-test-1"));
        inputBuckets.add(new InputBucket("deploy-input-test-2"));
        //=====================================================
        ArrayList<OutputBucket> outputBuckets = new ArrayList<>();
        outputBuckets.add(new OutputBucket("deploy-outputbucket-test-1"));
        outputBuckets.add(new OutputBucket("deploy-outputbucket-test-2"));
        //=====================================================
       SQS queue=new SQS("deploy-sqs-test-1");
        //=====================================================
        ArrayList<DynamoDBLogsTable> dynamoDBLogsTables = new ArrayList<>();
        dynamoDBLogsTables.add(new DynamoDBLogsTable("deploy-dynamodb-log-table-test"));
        ArrayList<DynamoDBMetadataTable> dynamoDBMetadataTables = new ArrayList<>();
        dynamoDBMetadataTables.add(new DynamoDBMetadataTable("deploy-dynamodb-metadata-test-1"));
        dynamoDBMetadataTables.add(new DynamoDBMetadataTable("deploy-dynamodb-metadata-test-2"));


        listener.setInputBuckets(inputBuckets);
        listener.setOutputBuckets(outputBuckets);
        listener.setDynamoDBLogsTables(dynamoDBLogsTables);
        listener.setDynamoDBMetadataTables(dynamoDBMetadataTables);

        listener.deploy();
    }


    @Test
    public void testS3BucketsCreate() throws Exception {
        assertTrue(S3Util.isExist("deploy-input-test-1"));
        assertTrue(S3Util.isExist("deploy-input-test-2"));
        assertTrue(S3Util.isExist("deploy-outputbucket-test-1"));
        assertTrue(S3Util.isExist("deploy-input-test-2"));
    }

    @Test
    public void testSQSQueueCreate() {
        assertTrue(SQSUtil.isExist("deploy-sqs-test-1"));

    }

    @Test
    public void testDynamoTablesCreate() {
        assertTrue(DynamoDBUtil.isTableExist("deploy-dynamodb-log-table-test"));
        assertTrue(DynamoDBUtil.isTableExist("deploy-dynamodb-metadata-test-1"));
        assertTrue(DynamoDBUtil.isTableExist("deploy-dynamodb-metadata-test-2"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        listener.clear();
    }
}