package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="logs")
public class Log {
    @Id
    private String id;
    private String time;
    private long userId;
    private String key;
    private String value;

    public Log(String id, String time, long userId, String key, String value) {
        this.id = id;
        this.time = time;
        this.userId = userId;
        this.key = key;
        this.value = value;
    }

    public Log() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Log:  id =" + id + "\ttime =" + time.toString() + "\tuserid =" + userId + "\tkey =" + key + "\tvalue =" + value.toString();
    }
}
