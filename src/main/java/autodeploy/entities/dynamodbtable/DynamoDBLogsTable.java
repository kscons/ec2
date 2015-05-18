package autodeploy.entities.dynamodbtable;

import utils.dynamodb.DynamoDBUtil;

/**
 * Created by Logitech on 08.05.15.
 */
public class DynamoDBLogsTable extends DynamoDBTable {
    public DynamoDBLogsTable(String name) {
        super(name);
    }

    @Override
    public void create() {
        DynamoDBUtil.createTableForMetadata(super.getName());
    }


}
