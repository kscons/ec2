package utils.dynamodb;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import configurations.servicesconfigurators.DynamoDBConfiGurator;
import entities.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entities.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Logitech on 17.04.15.
 */
public class NewDynamoDBUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBUtil.class);
    private static String ENDPOINT = "https://dynamodb.eu-west-1.amazonaws.com";
    private static DynamoDB dynamoDB;
    private static DynamoDBUtil instance;
    static AmazonDynamoDBClient addbcl;
    static private DynamoDBMapper mapper;




    static {
        //creating connection in default value of region which was set in ENDPOINT.
        addbcl = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
        addbcl.setEndpoint(ENDPOINT);
        dynamoDB = new DynamoDB(addbcl);
        mapper = new DynamoDBMapper(addbcl);
    }


    public static <T> void insertRecord(final T t) throws IllegalArgumentException {
        mapper.save(t);
    }

    public static <T> ArrayList<T> getAllRecords(Class<T> clazz) {
        DynamoDBMapper mapper = new DynamoDBMapper(addbcl);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedParallelScanList<T> result = mapper.parallelScan(clazz, scanExpression,10);
        return result
                .parallelStream()
                .collect(Collectors.toCollection(ArrayList<T>::new));
    }

    public static <T> void cleanTable(final Class clazz) {

        DynamoDBMapper mapper = new DynamoDBMapper(addbcl);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedParallelScanList<T> result = mapper.parallelScan(clazz, scanExpression,10);

        result.stream().
                unordered().
                parallel().
                forEach(data -> {
                    mapper.delete(data);
                });

    }


    public static void main(String[] a) {
        //createTable("l",5,5,"id","S");
        for (int i = 1; i < 10000000; i++) {
            try {
                cleanTable(Log.class);
            } catch (Exception ex) {
            }
        }
    }


}

