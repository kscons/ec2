package autodeploy.listener;

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

    private Receiver receiver;
    private String name;

    public Listener(String name,Receiver receiver) {
        this.setReceiver(receiver);
        this.name = name;
        queque=new SQS(name);

        init();

    }

    public Listener(String name) {
        this.setReceiver(new AsyncMessageReceiver(name));
        this.name = name;
        queque=new SQS(name);

        init();
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


    public  void init(){
        inputBuckets=new  ArrayList<InputBucket>() ;
        outputBuckets=new ArrayList<OutputBucket> ();
        dynamoDBMetadataTables=new  ArrayList<DynamoDBMetadataTable>() ;
        dynamoDBLogsTables=new  ArrayList<DynamoDBLogsTable> ();
        redshiftTables=new  ArrayList<RedshiftTable>() ;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Listener listener = (Listener) o;

        if (inputBuckets != null ? !inputBuckets.equals(listener.inputBuckets) : listener.inputBuckets != null)
            return false;
        if (queque != null ? !queque.equals(listener.queque) : listener.queque != null) return false;
        if (outputBuckets != null ? !outputBuckets.equals(listener.outputBuckets) : listener.outputBuckets != null)
            return false;
        if (dynamoDBMetadataTables != null ? !dynamoDBMetadataTables.equals(listener.dynamoDBMetadataTables) : listener.dynamoDBMetadataTables != null)
            return false;
        if (dynamoDBLogsTables != null ? !dynamoDBLogsTables.equals(listener.dynamoDBLogsTables) : listener.dynamoDBLogsTables != null)
            return false;
        if (redshiftTables != null ? !redshiftTables.equals(listener.redshiftTables) : listener.redshiftTables != null)
            return false;
        if (receiver != null ? !receiver.equals(listener.receiver) : listener.receiver != null) return false;
        return !(name != null ? !name.equals(listener.name) : listener.name != null);

    }

    @Override
    public int hashCode() {
        int result = inputBuckets != null ? inputBuckets.hashCode() : 0;
        result = 31 * result + (queque != null ? queque.hashCode() : 0);
        result = 31 * result + (outputBuckets != null ? outputBuckets.hashCode() : 0);
        result = 31 * result + (dynamoDBMetadataTables != null ? dynamoDBMetadataTables.hashCode() : 0);
        result = 31 * result + (dynamoDBLogsTables != null ? dynamoDBLogsTables.hashCode() : 0);
        result = 31 * result + (redshiftTables != null ? redshiftTables.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Listener{" +
                "inputBuckets=" + inputBuckets +
                ", queque=" + queque +
                ", outputBuckets=" + outputBuckets +
                ", dynamoDBMetadataTables=" + dynamoDBMetadataTables +
                ", dynamoDBLogsTables=" + dynamoDBLogsTables +
                ", redshiftTables=" + redshiftTables +
                ", receiver=" + receiver +
                ", name='" + name + '\'' +
                '}';
    }
}
