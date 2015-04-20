package configurations.properties.io;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by serhii on 08.04.15.
 */
public class PropertiesWriter {

    public static void save(final String fileName) {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream("config.properties")) {

            // set the properties value

            //messagereceiverconfig
            prop.setProperty("DEFAULT_QUEUE_NAME", "TestQueue");
            prop.setProperty("DEFAULT_OUTPUT_BUCKET_NAME", "logitech-analytics-ksolod-eu-west-1-sqsoutput");
            prop.setProperty("DEFAULT_INPUT_BUCKET_NAME", "logitech-analytics-ksolod-eu-west-1-sqsinput");
            prop.setProperty("ALIVE_DAYS_COUNT", "100");
            prop.setProperty("DEFAULT_REGION", Regions.EU_WEST_1 + "");
            //dynamodbconfig
            prop.setProperty("LOGS_DYNAMODB_OUTPUT_TABLE_NAME", "logs");
            prop.setProperty("METADATA_OUTPUT_TABLE", "Logitech_test_eu-west-1");

            //redshiftconfig
            prop.setProperty("REDSHIFT_ENDPOINT", "logitech-analytics-ksolod.cdcrmjgf6mls.eu-west-1.redshift.amazonaws.com");
            prop.setProperty("masterUsername", "ksolod");
            prop.setProperty("masterUserPassword", "Logitech1234");
            prop.setProperty("dbURL", "jdbc:postgresql://logitech-analytics-ksolod.cdcrmjgf6mls.eu-west-1.redshift.amazonaws.com:5439/logs");
            prop.setProperty("LOGS_REDSHIFT_OUTPUT_TABLE_NAME", "logs");
            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    public static void main(String[] args) {
        save("awsconfig.properties");
    }
}


