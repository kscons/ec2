package entities.datacollectionmap;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Logitech on 13.04.15.
 */
public class DCC {
    @JsonProperty(value = "upload_interval")
    private long uploadInterval=0;
    @JsonProperty(value = "retry_interval")
    private long retryInterval=0;
    private ArrayList<Source> sources;

    public DCC() {
    }

    public DCC(ArrayList<Source> sources) {
        this.sources = sources;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DCC dcc = (DCC) o;

        return !(sources != null ? !sources.equals(dcc.sources) : dcc.sources != null);

    }

    @Override
    public int hashCode() {
        return sources != null ? sources.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DCC{" +
                "sources=" + sources +
                '}';
    }
}
