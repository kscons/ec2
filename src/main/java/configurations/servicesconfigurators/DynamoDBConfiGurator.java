package configurations.servicesconfigurators;

import java.util.Properties;

/**
 * Created by serhii on 07.04.15.
 */
public class DynamoDBConfiGurator {
    private static String LOGS_DYNAMODB_OUTPUT_TABLE_NAME ="logs";
    private static  String METADATA_OUTPUT_TABLE = "Logitech_test_eu-west-1";
    public static String getLogsDynamodbOutputTableName() {
        return LOGS_DYNAMODB_OUTPUT_TABLE_NAME;
    }

    public static void setLogsDynamodbOutputTableName(String logsDynamodbOutputTableName) {
        LOGS_DYNAMODB_OUTPUT_TABLE_NAME = logsDynamodbOutputTableName;
    }

    public static String getMetadataOutputTable() {
        return METADATA_OUTPUT_TABLE;
    }

    public static void setMetadataOutputTable(String metadataOutputTable) {
        METADATA_OUTPUT_TABLE = metadataOutputTable;
    }

    public static void configure(Properties properties){
     LOGS_DYNAMODB_OUTPUT_TABLE_NAME=   properties.getProperty("LOGS_DYNAMODB_OUTPUT_TABLE_NAME");
       METADATA_OUTPUT_TABLE= properties.getProperty("METADATA_OUTPUT_TABLE");

    }
}
