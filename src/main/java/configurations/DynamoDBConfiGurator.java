package configurations;

/**
 * Created by serhii on 07.04.15.
 */
public class DynamoDBConfiGurator {
    private static String OUTPUT_LOGS_TABLE_NAME="logs";
    private static String OUTPUT_METADATA_TABLE_NAME="logitech-analytics-ksolod-eu-west-1-sqsoutput";

    public static String getOutputLogsTableName() {
        return OUTPUT_LOGS_TABLE_NAME;
    }

    public static void setOutputLogsTableName(String outputLogsTableName) {
        OUTPUT_LOGS_TABLE_NAME = outputLogsTableName;
    }

    public static String getOutputMetadataTableName() {
        return OUTPUT_METADATA_TABLE_NAME;
    }

    public static void setOutputMetadataTableName(String outputMetadataTableName) {
        OUTPUT_METADATA_TABLE_NAME = outputMetadataTableName;
    }
}
