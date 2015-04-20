package entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.util.Date;
import java.util.Map;

/**
 * This is a simple POJO Object which uses to correct and comfortable transfering metadata into DynamoDB.
 */
@DynamoDBTable(tableName = "Logitech_test_eu-west-1")
public class Metadata {
    @DynamoDBHashKey
    private String eventID;
    @DynamoDBAttribute
    private long userId;
    @DynamoDBAttribute
    private long machineId;
    @DynamoDBAttribute
    private Date lastModified;
    @DynamoDBAttribute
    private String eventType;
    @DynamoDBAttribute
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
    public Metadata(Map<String,AttributeValue> md) {
        eventID = md.get("eventid").toString();
        userId = Long.parseLong(md.get("userid").toString());
        machineId = Long.parseLong(md.get("machineid").toString());
        lastModified = new Date(md.get("lastmodified").toString());
        eventType = md.get("eventtype").toString();
        value = md.get("value").toString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metadata metadata = (Metadata) o;

        if (userId != metadata.userId) return false;
        if (machineId != metadata.machineId) return false;
        if (allFieldsNotNull != metadata.allFieldsNotNull) return false;
        if (eventID != null ? !eventID.equals(metadata.eventID) : metadata.eventID != null) return false;
        if (lastModified != null ? !lastModified.equals(metadata.lastModified) : metadata.lastModified != null)
            return false;
        if (eventType != null ? !eventType.equals(metadata.eventType) : metadata.eventType != null) return false;
        return !(value != null ? !value.equals(metadata.value) : metadata.value != null);

    }

    @Override
    public int hashCode() {
        int result = eventID != null ? eventID.hashCode() : 0;
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (machineId ^ (machineId >>> 32));
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (allFieldsNotNull ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "eventID='" + eventID + '\'' +
                ", userId=" + userId +
                ", machineId=" + machineId +
                ", lastModified=" + lastModified +
                ", eventType='" + eventType + '\'' +
                ", value='" + value + '\'' +
                ", allFieldsNotNull=" + allFieldsNotNull +
                '}';
    }
}
