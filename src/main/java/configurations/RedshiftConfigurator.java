package configurations;

/**
 * Created by serhii on 07.04.15.
 */
public class RedshiftConfigurator {
    private static final String ENDPOINT = "logitech-analytics-ksolod.cdcrmjgf6mls.eu-west-1.redshift.amazonaws.com";
    private static final String masterUsername = "ksolod";
    private static final String masterUserPassword = "Logitech1234";
    private static final String dbURL = "jdbc:postgresql://logitech-analytics-ksolod.cdcrmjgf6mls.eu-west-1.redshift.amazonaws.com:5439/logs";
    private static String defaultTable="logs";

    public static String getEndpoint() {
        return ENDPOINT;
    }

    public static String getMasterUsername() {
        return masterUsername;
    }

    public static String getMasterUserPassword() {
        return masterUserPassword;
    }

    public static String getDbURL() {
        return dbURL;
    }

    public static String getDefaultTable() {
        return defaultTable;
    }

    public static void setDefaultTable(String defaultTable) {
        RedshiftConfigurator.defaultTable = defaultTable;
    }
}
