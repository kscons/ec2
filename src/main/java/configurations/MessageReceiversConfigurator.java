package configurations;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;

/**
 * Created by serhii on 07.04.15.
 */
public class MessageReceiversConfigurator {
    private static final String LOGS_TABLE_NAME = "logs";
    private static final String DEFAULT_QUEUE_NAME = "TestQueue";
    private static final String DEFAULT_OUTPUT_BUCKET_NAME = "logitech-analytics-ksolod-eu-west-1-sqsoutput";
    private static final String METADATA_OUTPUT_TABLE = "Logitech_test_eu-west-1";
    private static final long ALIVE_DAYS_COUNT = 100;
    private static final Region DEFAULT_REGION = Region.getRegion(Regions.EU_WEST_1);
    private static AmazonSQSClient sqsClient = new AmazonSQSClient(new ProfileCredentialsProvider());


    public static String getLogsTableName() {
        return LOGS_TABLE_NAME;
    }

    public static String getDefaultQueueName() {
        return DEFAULT_QUEUE_NAME;
    }

    public static String getDefaultOutputBucketName() {
        return DEFAULT_OUTPUT_BUCKET_NAME;
    }

    public static String getMetadataOutputTable() {
        return METADATA_OUTPUT_TABLE;
    }

    public static long getAliveDaysCount() {
        return ALIVE_DAYS_COUNT;
    }

    public static Region getDefaultRegion() {
        return DEFAULT_REGION;
    }

    public static AmazonSQSClient getSqsClient() {
        return sqsClient;
    }

    public static void setSqsClient(AmazonSQSClient sqsClient) {
        MessageReceiversConfigurator.sqsClient = sqsClient;
    }
}
