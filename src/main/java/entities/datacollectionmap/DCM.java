package entities.datacollectionmap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Logitech on 13.04.15.
 */
public class DCM {
    private String key;
    private String source;
    @JsonProperty("sampling_period")
    private String samplingPeriod;

    public DCM() {
    }

    public DCM(String key, String source, String samplingPeriod) {
        this.key = key;
        this.source = source;
        this.samplingPeriod = samplingPeriod;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSamplingPeriod() {
        return samplingPeriod;
    }

    public void setSamplingPeriod(String samplingPeriod) {
        this.samplingPeriod = samplingPeriod;
    }
}
