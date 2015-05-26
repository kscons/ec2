package autodeploy;

import autodeploy.entities.Item;
import autodeploy.entities.dynamodbtable.DynamoDBLogsTable;
import autodeploy.entities.dynamodbtable.DynamoDBMetadataTable;
import autodeploy.entities.redshifttable.RedshiftTable;
import autodeploy.entities.s3buckets.InputBucket;
import autodeploy.entities.s3buckets.OutputBucket;
import autodeploy.entities.sqs.SQS;
import receiver.Receiver;
import receiver.asynchronous.AsyncMessageReceiver;

import java.util.ArrayList;

/**
 * Created by Logitech on 25.05.15.
 */
public class Listener {
    private ArrayList<InputBucket> inputBuckets;
    private SQS queque;
    private ArrayList<OutputBucket> outputBuckets;
    private ArrayList<DynamoDBMetadataTable> dynamoDBMetadataTables;
    private ArrayList<DynamoDBLogsTable> dynamoDBLogsTables;
    private ArrayList<RedshiftTable> redshiftTables;
    private ArrayList<ArrayList<? extends Item>> items;

    private Receiver receiver;
    private String name;

    public Listener(String name,Receiver receiver) {
        this.setReceiver(receiver);
        this.name = name;
        queque=new SQS(name);

    }

    public Listener(String name) {
        this.setReceiver(new AsyncMessageReceiver(name));
        this.name = name;
        queque=new SQS(name);
    }



    public void deploy() {
        createList(getInputBuckets());
        createList(getOutputBuckets());
        createList(getDynamoDBMetadataTables());
        createList(getDynamoDBLogsTables());
        getQueque().create();

    }
    public void start() {
        try {
            getReceiver().start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void stop(){

    }
    public void clear(){
        clearList(getInputBuckets());
        clearList(getOutputBuckets());
        clearList(getDynamoDBMetadataTables());
        clearList(getDynamoDBLogsTables());
        getQueque().delete();
    }

    public void test(){

    }

    public void clearList(ArrayList<? extends Item> itemList){
        itemList.forEach(item -> {item.delete();});

    }
    public void createList(ArrayList<? extends Item> itemList){
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



    public SQS getQueque() {
        return queque;
    }

    public void setQueque(SQS queque) {
        this.queque = queque;
    }

    public ArrayList<ArrayList<? extends Item>> getItems() {
        return items;
    }

    public void setItems(ArrayList<ArrayList<? extends Item>> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
