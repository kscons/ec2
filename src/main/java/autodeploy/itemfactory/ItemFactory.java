package autodeploy.itemfactory;

import autodeploy.entities.Item;
import autodeploy.entities.dynamodbtable.DynamoDBLogsTable;
import autodeploy.entities.dynamodbtable.DynamoDBMetadataTable;
import autodeploy.entities.redshifttable.RedshiftTable;
import autodeploy.entities.s3buckets.InputBucket;
import autodeploy.entities.s3buckets.OutputBucket;
import autodeploy.entities.sqs.SQS;
import utils.s3.S3Util;
import utils.sqs.SQSUtil;

import java.util.ArrayList;

/**
 * Created by Logitech on 05.05.15.
 */
public class ItemFactory {
    static ArrayList<Item> itemsList = new ArrayList<>();

    public static Item createItem(final String itemName, ItemType type) {
        Item item = null;
        switch (type) {
            case INPUT_BUCKET:
                item = new InputBucket(itemName);
                break;
            case OUTPUT_BUCKET:
                item = new OutputBucket(itemName);
                break;
            case SQS:
                item = new SQS(itemName);
                break;
            case DYNAMODB_LOG_TABLE:
                item = new DynamoDBLogsTable(itemName);
                break;
            case DYNAMODB_METADATA_TABLE:
                item = new DynamoDBMetadataTable(itemName);
                break;
            case REDSHIFT_TABLE:
                item = new RedshiftTable(itemName);
                break;
        }
        itemsList.add(item);
        return item;
    }

    public static void createAll() {
        itemsList.stream().forEachOrdered(item -> item.create());
    }


    public static void main(String[] a) {

    }

}



