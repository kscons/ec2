package receiver.asynchronous.paralellcommands;

import configurations.servicesconfigurators.DynamoDBConfiGurator;
import entities.Log;
import pools.threadpools.LogSavingPool;
import utils.dynamodb.AsyncDynamoDBSaver;

import utils.dynamodb.DynamoDBUtil;
import utils.dynamodb.NewDynamoDBUtil;
import utils.jsonprocessors.LogParser;
import utils.redshift.hibernate.RedshiftHibernateUtil;
import utils.redshift.jdbc.RedshiftJDBCUtil;

import java.util.ArrayList;

public class SaveLogs {
    public static void save(final String logs, final long userId) {

        LogParser.parseLog(logs, userId)
                .parallelStream()
                .unordered()
                .forEach(log -> {
                    RedshiftJDBCUtil.insertLog(log);
                    NewDynamoDBUtil.insertRecord(log);
                });

    }

}
