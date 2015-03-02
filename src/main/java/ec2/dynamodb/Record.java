package ec2.dynamodb;


public class Record {
    private String eventID;
    private long userId;
    private long machineId;
    private long time;
    private String eventType;
    private String value;

    public Record() {
    }

    public Record(String eventID, long userId, long machineId, long time, String eventType, String value) {
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
