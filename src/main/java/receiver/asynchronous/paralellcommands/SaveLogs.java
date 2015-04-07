package receiver.asynchronous.paralellcommands;

import entities.Log;
import configurations.MessageReceiversConfigurator;
import pools.threadpools.LogSavingPool;
import utils.dynamodb.AsyncDynamoDBSaver;
import utils.dynamodb.DynamoDBUtil;
import utils.logparser.LogParser;
import utils.redshift.hibernate.RedshiftHibernateUtil;

import java.util.ArrayList;

public class SaveLogs {
    public static void save(final String logs, final String id, final long userId) {

        final ArrayList<Log> listLogs =LogParser.parseLog(logs, id, userId);
        LogSavingPool.runProcess(new Runnable() {
            @Override
            public void run() {
                RedshiftHibernateUtil.insertLogs(listLogs);
            }
        });
        LogSavingPool.runProcess(new Runnable() {
            @Override
            public void run() {
                AsyncDynamoDBSaver.insertLogRecords(listLogs);
            }
        });
    }
}
