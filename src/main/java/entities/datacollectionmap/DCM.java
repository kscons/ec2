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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DCM dcm = (DCM) o;

        if (key != null ? !key.equals(dcm.key) : dcm.key != null) return false;
        if (source != null ? !source.equals(dcm.source) : dcm.source != null) return false;
        return !(samplingPeriod != null ? !samplingPeriod.equals(dcm.samplingPeriod) : dcm.samplingPeriod != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (samplingPeriod != null ? samplingPeriod.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DCM{" +
                "key='" + key + '\'' +
                ", source='" + source + '\'' +
                ", samplingPeriod='" + samplingPeriod + '\'' +
                '}';
    }
}
