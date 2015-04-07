package entities;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.util.Date;
import java.util.Map;

/**
 * This is a simple POJO Object which uses to correct and comfortable transfering metadata into DynamoDB.
 */
public class Metadata {
    private String eventID;
    private long userId;
    private long machineId;
    private Date lastModified;
    private String eventType;
    private String value;
    private boolean allFieldsNotNull;

    public Metadata() {
    }

    public Metadata(ObjectMetadata om) {
        Map<String, String> md = om.getUserMetadata();
        eventID = md.get("eventid");
        userId = Long.parseLong(md.get("userid"));
        machineId = Long.parseLong(md.get("machineid"));
        lastModified = om.getLastModified();
        eventType = md.get("eventtype");
        value = md.get("value");
    }


    public Metadata(String eventID, long userId, long machineId, Date lastModified, String eventType, String value) {
        this.eventID = eventID;
        this.userId = userId;
        this.machineId = machineId;
        this.lastModified = lastModified;
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

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
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
