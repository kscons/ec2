package autodeploy.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import utils.jsonprocessors.JSONProcessor;

import java.util.ArrayList;

/**
 * Created by Logitech on 26.05.15.
 */
public class ListenerJSONObject {
    private String name;
    private ArrayList<String> inputBuckets;
    private String queque;
    private ArrayList<String> outputBuckets;
    private ArrayList<String> dynamoDBMetadataTables;
    private ArrayList<String> dynamoDBLogsTables;
    private ArrayList<String> redshiftTables;


    public static void main(String[] a) {
        String name="listenet1-test";
        //====================================================
        ArrayList<String> inputBuckets = new ArrayList<>();
        inputBuckets.add("deploy-input-test-1");
        inputBuckets.add("deploy-input-test-2");
        //=====================================================
        ArrayList<String> outputBuckets = new ArrayList<>();
        outputBuckets.add("deploy-outputbucket-test-1");
        outputBuckets.add("deploy-outputbucket-test-2");
        //=====================================================
        String queue = "deploy-sqs-test-1";
        //=====================================================
        ArrayList<String> dynamoDBLogsTables = new ArrayList<>();
        dynamoDBLogsTables.add("deploy-dynamodb-log-table-test");
        ArrayList<String> dynamoDBMetadataTables = new ArrayList<>();
        dynamoDBMetadataTables.add("deploy-dynamodb-metadata-test-1");
        dynamoDBMetadataTables.add("deploy-dynamodb-metadata-test-2");

        ArrayList<String> redshiftTables = new ArrayList<>();
        dynamoDBMetadataTables.add("deploy-redshift-test-1");

        ListenerJSONObject ljo = new ListenerJSONObject();
        ljo.name=name;
        ljo.inputBuckets = inputBuckets;
        ljo.outputBuckets = outputBuckets;
        ljo.queque = queue;
        ljo.dynamoDBLogsTables = dynamoDBLogsTables;
        ljo.dynamoDBMetadataTables = dynamoDBMetadataTables;
        ljo.redshiftTables = redshiftTables;

        try {
            System.out.print(JSONProcessor.generateJSON(ljo));
        } catch (JsonProcessingException jpe) {
        }

    }

    public ArrayList<String> getInputBuckets() {
        return inputBuckets;
    }

    public void setInputBuckets(ArrayList<String> inputBuckets) {
        this.inputBuckets = inputBuckets;
    }

    public String getQueque() {
        return queque;
    }

    public void setQueque(String queque) {
        this.queque = queque;
    }

    public ArrayList<String> getOutputBuckets() {
        return outputBuckets;
    }

    public void setOutputBuckets(ArrayList<String> outputBuckets) {
        this.outputBuckets = outputBuckets;
    }

    public ArrayList<String> getDynamoDBMetadataTables() {
        return dynamoDBMetadataTables;
    }

    public void setDynamoDBMetadataTables(ArrayList<String> dynamoDBMetadataTables) {
        this.dynamoDBMetadataTables = dynamoDBMetadataTables;
    }

    public ArrayList<String> getDynamoDBLogsTables() {
        return dynamoDBLogsTables;
    }

    public void setDynamoDBLogsTables(ArrayList<String> dynamoDBLogsTables) {
        this.dynamoDBLogsTables = dynamoDBLogsTables;
    }

    public ArrayList<String> getRedshiftTables() {
        return redshiftTables;
    }

    public void setRedshiftTables(ArrayList<String> redshiftTables) {
        this.redshiftTables = redshiftTables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


