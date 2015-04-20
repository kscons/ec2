package receiver.synchronous.synchronouscommands;

import configurations.servicesconfigurators.DynamoDBConfiGurator;
import configurations.servicesconfigurators.RedshiftConfigurator;
import entities.Log;
import utils.dynamodb.DynamoDBUtil;
import utils.jsonprocessors.LogParser;
import utils.redshift.jdbc.RedshiftJDBCUtil;


public class SaveLogs {

    public static void save(final String logs, final String id, final long userId) {
        for (Log log : LogParser.parseLog(logs, id, userId)){
            RedshiftJDBCUtil.insertLog(RedshiftConfigurator.getLogsRedshiftOutputTableName(), log );
            DynamoDBUtil.insertLogRecord(DynamoDBConfiGurator.getLogsDynamodbOutputTableName(),log );
        }
    }

}
