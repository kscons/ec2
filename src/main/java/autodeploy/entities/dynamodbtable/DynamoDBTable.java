package autodeploy.entities.dynamodbtable;

import autodeploy.entities.Item;
import utils.dynamodb.DynamoDBUtil;
import utils.s3.S3Util;

/**
 * Created by Logitech on 07.05.15.
 */
public abstract class DynamoDBTable implements Item {
    private String name;

    public DynamoDBTable(String name) {
        this.name = name;
    }

    @Override
    public void delete() {
        DynamoDBUtil.deleteTable(name);
    }


    @Override
    public boolean isCreated() {
        return true;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
