package utils;

import entities.Log;
import entities.Metadata;
import sun.misc.UUDecoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Logitech on 18.05.15.
 */
public class TestDataGenerator {
    public static Metadata getTestMetadata(){
       return new Metadata("eventID", (long)Math.random()*100000,(long) Math.random()*100000,new Date(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }
    public static Log getTestLog(){
       return new Log("id",(long)Math.random()*100000, UUID.randomUUID().toString(),UUID.randomUUID().toString(),UUID.randomUUID().toString());
    }
    public static ArrayList<Metadata> getMetadatasTesList(){
        ArrayList<Metadata> metadatasList=new ArrayList<>();
        for (int i=1;i<Math.random()*100;i++){
            metadatasList.add(new Metadata("eventID", (long)Math.random()*100000,(long) Math.random()*100000,new Date(), UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
        return metadatasList;
    }
    public static ArrayList<Log> getLogTesList(){
        ArrayList<Log> metadatasList=new ArrayList<>();
        for (int i=1;i<Math.random()*100;i++){
metadatasList.add(new Log("id",(long)Math.random()*100000, UUID.randomUUID().toString(),UUID.randomUUID().toString(),UUID.randomUUID().toString()));        }
        return metadatasList;
    }
}
