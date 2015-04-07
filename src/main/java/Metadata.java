import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private String eventID;
    private long userId;
    private long machineId;
    private long time;
    private String eventType;
    private String value;

    public Metadata() {
    }
    public Metadata(ObjectMetadata om) {
       try{
           Map<String,String> md=  om.getUserMetadata();
           ArrayList a=new ArrayList(md.values());
           eventID=a.get(4).toString();
           userId=Long.parseLong(a.get(2).toString());
           machineId=Long.parseLong(a.get(1).toString());
           time=Long.parseLong(a.get(0).toString());
           eventType=a.get(5).toString();
           value=a.get(3).toString();


       }catch (NumberFormatException nfe){

       }
    }
    public Metadata(String eventID, long userId, long machineId, long time, String eventType, String value) {
        this.eventID = eventID;
        this.userId = userId;
        this.machineId = machineId;
        this.time = time;
        this.eventType = eventType;
        this.value = value;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
