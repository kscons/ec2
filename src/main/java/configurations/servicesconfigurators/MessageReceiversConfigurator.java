package configurations.servicesconfigurators;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.util.Properties;

/**
 * Created by serhii on 07.04.15.
 */
@Deprecated
public class MessageReceiversConfigurator {
    private static String DEFAULT_QUEUE_NAME = "TestQueue";
    private static String DEFAULT_OUTPUT_BUCKET_NAME = "logitech-analytics-ksolod-eu-west-1-sqsoutput";
    private static String DEFAULT_INPUT_BUCKET_NAME = "logitech-analytics-ksolod-eu-west-1-sqsinput";

    private static long ALIVE_DAYS_COUNT = 100;
    private static Region DEFAULT_REGION = Region.getRegion(Regions.EU_WEST_1);


    public static String getDefaultQueueName() {
        return DEFAULT_QUEUE_NAME;
    }

    public static String getDefaultOutputBucketName() {
        return DEFAULT_OUTPUT_BUCKET_NAME;
    }


    public static long getAliveDaysCount() {
        return ALIVE_DAYS_COUNT;
    }

    public static Region getDefaultRegion() {
        return DEFAULT_REGION;
    }

    public static String getDefaultInputBucketName() {
        return DEFAULT_INPUT_BUCKET_NAME;
    }

    public static void configure(Properties properties) {
        DEFAULT_QUEUE_NAME = properties.getProperty("DEFAULT_QUEUE_NAME");
        DEFAULT_OUTPUT_BUCKET_NAME = properties.getProperty("DEFAULT_OUTPUT_BUCKET_NAME");
        DEFAULT_REGION = Region.getRegion(Regions.valueOf(properties.getProperty("DEFAULT_REGION")));
        DEFAULT_INPUT_BUCKET_NAME = properties.getProperty("DEFAULT_INPUT_BUCKET_NAME");
    }
}
