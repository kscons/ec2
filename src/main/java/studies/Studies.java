package studies;

/**
 * Created by Logitech on 15.04.15.
 */
public class Studies {
    private String id;
    private String deviceType;
    private String os;
    private String region;


    public Studies(String id, String deviceType, String os, String region) {
        this.id = id;
        this.deviceType = deviceType;
        this.os = os;
        this.region = region;
    }
    public  Studies(){}

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
