package receiver.synchronous.synchronouscommands;

import entities.Log;
import configurations.MessageReceiversConfigurator;
import utils.dynamodb.DynamoDBUtil;
import utils.logparser.LogParser;
import utils.redshift.jdbc.RedshiftJDBCUtil;


public class SaveLogs {

    public static void save(final String logs, final String id, final long userId) {
        for (Log log : LogParser.parseLog(logs, id, userId)){
            RedshiftJDBCUtil.insertLog(MessageReceiversConfigurator.getLogsTableName(), log );
            DynamoDBUtil.insertLogRecord(MessageReceiversConfigurator.getLogsTableName(),log );
        }
    }

}
