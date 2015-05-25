package autodeploy;

import autodeploy.entities.Item;
import autodeploy.entities.dynamodbtable.DynamoDBLogsTable;
import autodeploy.entities.dynamodbtable.DynamoDBMetadataTable;
import autodeploy.entities.redshifttable.RedshiftTable;
import autodeploy.entities.s3buckets.InputBucket;
import autodeploy.entities.s3buckets.OutputBucket;
import autodeploy.entities.sqs.SQS;
import receiver.Receiver;
import java.util.ArrayList;

/**
 * Created by Logitech on 25.05.15.
 */
public class Listener {
    ArrayList<InputBucket> inputBuckets;
    ArrayList<SQS> queques;
    ArrayList<OutputBucket> outputBuckets;
    ArrayList<DynamoDBMetadataTable> dynamoDBMetadataTables;
    ArrayList<DynamoDBLogsTable> dynamoDBLogsTables;
    ArrayList<RedshiftTable> redshiftTables;
    ArrayList<ArrayList<? extends Item>> items;

    Receiver receiver;

    public Listener(Receiver receiver) {
        this.receiver = receiver;
       /* items.add(inputBuckets);
        items.add(queques);
        items.add(outputBuckets);
        items.add(dynamoDBMetadataTables);
        items.add(dynamoDBLogsTables);
        items.add(redshiftTables);*/
    }

    public void start() {
        try {
            receiver.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void deploy() {
        createAWSItems(inputBuckets);
        createAWSItems(queques);
        createAWSItems(outputBuckets);
        createAWSItems(dynamoDBMetadataTables);
        createAWSItems(dynamoDBLogsTables);
    }
    public void clear(){
        clearList(inputBuckets);
        clearList(queques);
        clearList(outputBuckets);
        clearList(dynamoDBMetadataTables);
        clearList(dynamoDBLogsTables);
    }

    public void clearList(ArrayList<? extends Item> itemList){
        itemList.forEach(item -> {item.delete();});

    }
    public void createAWSItems(ArrayList<? extends Item> itemList){
      itemList.forEach(item -> {item.create();});

    }

    public ArrayList<InputBucket> getInputBuckets() {
        return inputBuckets;
    }

    public void setInputBuckets(ArrayList<InputBucket> inputBuckets) {
        this.inputBuckets = inputBuckets;
    }

    public ArrayList<OutputBucket> getOutputBuckets() {
        return outputBuckets;
    }

    public void setOutputBuckets(ArrayList<OutputBucket> outputBuckets) {
        this.outputBuckets = outputBuckets;
    }

    public ArrayList<DynamoDBMetadataTable> getDynamoDBMetadataTables() {
        return dynamoDBMetadataTables;
    }

    public void setDynamoDBMetadataTables(ArrayList<DynamoDBMetadataTable> dynamoDBMetadataTables) {
        this.dynamoDBMetadataTables = dynamoDBMetadataTables;
    }

    public ArrayList<DynamoDBLogsTable> getDynamoDBLogsTables() {
        return dynamoDBLogsTables;
    }

    public void setDynamoDBLogsTables(ArrayList<DynamoDBLogsTable> dynamoDBLogsTables) {
        this.dynamoDBLogsTables = dynamoDBLogsTables;
    }

    public ArrayList<RedshiftTable> getRedshiftTables() {
        return redshiftTables;
    }

    public void setRedshiftTables(ArrayList<RedshiftTable> redshiftTables) {
        this.redshiftTables = redshiftTables;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public ArrayList<SQS> getQueques() {
        return queques;
    }

    public void setQueques(ArrayList<SQS> queques) {
        this.queques = queques;
    }


}
