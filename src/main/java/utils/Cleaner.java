package utils;

import configurations.Configurator;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import configurations.servicesconfigurators.RedshiftConfigurator;
import entities.Log;
import entities.Metadata;
import utils.s3.S3Util;
import utils.dynamodb.MapperDynamoDBUtil;
import utils.redshift.jdbc.RedshiftJDBCUtil;

/**
 * Created by Logitech on 29.04.15.
 */
public class    Cleaner {
    public static void clean() {
        Configurator.configureAll("config.properties");
        MapperDynamoDBUtil.<Log>cleanTable(Log.class);
        MapperDynamoDBUtil.<Metadata>cleanTable(Metadata.class);
        RedshiftJDBCUtil.deleteTable(RedshiftConfigurator.getLogsRedshiftOutputTableName());
        RedshiftJDBCUtil.createTableForLogs(RedshiftConfigurator.getLogsRedshiftOutputTableName());
        S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultOutputBucketName());
        S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultInputBucketName());
    }
    public static void main(String[] ar){
        clean();
    }

}
