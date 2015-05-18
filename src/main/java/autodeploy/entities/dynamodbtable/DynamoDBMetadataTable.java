package autodeploy.entities.dynamodbtable;

import utils.dynamodb.DynamoDBUtil;

/**
 * Created by Logitech on 08.05.15.
 */
public class DynamoDBMetadataTable extends DynamoDBTable {

    public DynamoDBMetadataTable(String name) {
        super(name);
    }

    @Override
    public void create() {
        DynamoDBUtil.createTableForMetadata(super.getName());
    }


    @Override
    public boolean isCreated() {
        return super.isCreated();
    }
}
