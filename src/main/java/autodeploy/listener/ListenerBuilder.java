package autodeploy.listener;

import autodeploy.entities.dynamodbtable.DynamoDBLogsTable;
import autodeploy.entities.dynamodbtable.DynamoDBMetadataTable;
import autodeploy.entities.redshifttable.RedshiftTable;
import autodeploy.entities.s3buckets.InputBucket;
import autodeploy.entities.s3buckets.OutputBucket;
import receiver.asynchronous.AsyncMessageReceiver;
import utils.jsonprocessors.JSONProcessor;

import java.io.IOException;

/**
 * Created by Logitech on 26.05.15.
 */
public class ListenerBuilder {
    public static Listener build(String json) throws IOException {
        ListenerJSONObject listenerJSONObject = JSONProcessor.parseJSON(ListenerJSONObject.class, json);
        final Listener listener = new Listener(listenerJSONObject.getName(), new AsyncMessageReceiver(listenerJSONObject.getName()));

        listenerJSONObject.getInputBuckets()
                .stream()
                .forEach(inputBucketName -> {
                    listener.getInputBuckets().add(new InputBucket(inputBucketName));
                });

        listenerJSONObject.getOutputBuckets()
                .stream()
                .forEach(outputBucketName -> {
                    listener.getOutputBuckets().add(new OutputBucket(outputBucketName));
                });

        listenerJSONObject.getDynamoDBLogsTables()
                .stream()
                .forEach(dynamoDBLogtableName -> {
                    listener.getDynamoDBLogsTables().add(new DynamoDBLogsTable(dynamoDBLogtableName));
                });

        listenerJSONObject.getDynamoDBMetadataTables()
                .stream()
                .forEach(dynamoDBMetadataTableName -> {
                    listener.getDynamoDBMetadataTables().add(new DynamoDBMetadataTable(dynamoDBMetadataTableName));
                });

        listenerJSONObject.getRedshiftTables()
                .stream()
                .forEach(redshiftTablesName -> {
                    listener.getRedshiftTables().add(new RedshiftTable(redshiftTablesName));
                });


        return listener;
    }

    private static final String mock = "{  \n" +
            "   \"name\":\"listenet1-test\",\n" +
            "   \"inputBuckets\":[  \n" +
            "      \"deploy-input-test-1\",\n" +
            "      \"deploy-input-test-2\"\n" +
            "   ],\n" +
            "   \"queque\":\"deploy-sqs-test-1\",\n" +
            "   \"outputBuckets\":[  \n" +
            "      \"deploy-outputbucket-test-1\",\n" +
            "      \"deploy-outputbucket-test-2\"\n" +
            "   ],\n" +
            "   \"dynamoDBMetadataTables\":[  \n" +
            "      \"deploy-dynamodb-metadata-test-1\",\n" +
            "      \"deploy-dynamodb-metadata-test-2\",\n" +
            "      \"deploy-redshift-test-1\"\n" +
            "   ],\n" +
            "   \"dynamoDBLogsTables\":[  \n" +
            "      \"deploy-dynamodb-log-table-test\"\n" +
            "   ],\n" +
            "   \"redshiftTables\":[  \n" +
            "\n" +
            "   ]\n" +
            "}";

    public static void main(String[] a) {
        try {
            Listener listener = build(mock);
            System.out.println(listener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
