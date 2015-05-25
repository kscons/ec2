package receiver.asynchronous.paralellcommands;

import utils.dynamodb.MapperDynamoDBUtil;
import utils.jsonprocessors.LogParser;
import utils.redshift.jdbc.RedshiftJDBCUtil;

public class SaveLogs {
    public static void save(final String logs, final long userId) {

        LogParser.parseLog(logs, userId)
                .parallelStream()
                .unordered()
                .forEach(log -> {
                    RedshiftJDBCUtil.insertLog(log);
                    MapperDynamoDBUtil.insertRecord(log);
                });

    }

}
