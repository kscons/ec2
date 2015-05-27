package utils;

import entities.Log;
import entities.Metadata;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Logitech on 18.05.15.
 */
public class TestDataGenerator {
    public static Metadata getTestMetadata() {
        return new Metadata("eventID",genRandomLong(), genRandomLong(), genRandomLong(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public static Log getTestLog() {
        return new Log("id", genRandomLong(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public static ArrayList<Metadata> getMetadatasTesList() {
        ArrayList<Metadata> metadatasList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            metadatasList.add(new Metadata("eventID", genRandomLong(),genRandomLong(), genRandomLong(), UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
        return metadatasList;
    }

    public static ArrayList<Log> getLogTesList() {
        ArrayList<Log> metadatasList = new ArrayList<>();
        for (int i = 1; i <10; i++) {
            metadatasList.add(new Log("id"+genRandomLong(), genRandomLong(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
        return metadatasList;
    }

    public static  long genRandomLong(){
        return (long) Math.random() * 100000;
    }
}
