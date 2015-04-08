package configurations.servicesconfigurators;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import java.util.Properties;

/**
 * Created by serhii on 07.04.15.
 */
public class RedshiftConfigurator {
    private static  String REDSHIFT_ENDPOINT = "logitech-analytics-ksolod.cdcrmjgf6mls.eu-west-1.redshift.amazonaws.com";
    private static  String masterUsername = "ksolod";
    private static  String masterUserPassword = "Logitech1234";
    private static  String dbURL = "jdbc:postgresql://logitech-analytics-ksolod.cdcrmjgf6mls.eu-west-1.redshift.amazonaws.com:5439/logs";
    private static  String LOGS_REDSHIFT_OUTPUT_TABLE_NAME = "logs";

    public static String getEndpoint() {
        return REDSHIFT_ENDPOINT;
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

    public static void setRedshiftEndpoint(String redshiftEndpoint) {
        RedshiftConfigurator.REDSHIFT_ENDPOINT = redshiftEndpoint;
    }

    public static void setMasterUsername(String masterUsername) {
        RedshiftConfigurator.masterUsername = masterUsername;
    }

    public static void setMasterUserPassword(String masterUserPassword) {
        RedshiftConfigurator.masterUserPassword = masterUserPassword;
    }

    public static void setDbURL(String dbURL) {
        RedshiftConfigurator.dbURL = dbURL;
    }

    public static void setLogsRedshiftOutputTableName(String logsRedshiftOutputTableName) {
        LOGS_REDSHIFT_OUTPUT_TABLE_NAME = logsRedshiftOutputTableName;
    }

    public static String getRedshiftEndpoint() {
        return REDSHIFT_ENDPOINT;
    }

    public static String getLogsRedshiftOutputTableName() {
        return LOGS_REDSHIFT_OUTPUT_TABLE_NAME;
    }

    public static void configure(Properties properties) {
       REDSHIFT_ENDPOINT= properties.getProperty("REDSHIFT_ENDPOINT");
       masterUsername= properties.getProperty("masterUsername");
      masterUserPassword = properties.getProperty("masterUserPassword");
       dbURL =properties.getProperty("dbURL");
        LOGS_REDSHIFT_OUTPUT_TABLE_NAME=properties.getProperty("LOGS_REDSHIFT_OUTPUT_TABLE_NAME");

    }
}
