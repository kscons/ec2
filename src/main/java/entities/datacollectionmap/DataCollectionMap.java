package entities.datacollectionmap;

import java.util.ArrayList;

/**
 * Created by Logitech on 13.04.15.
 */
public class DataCollectionMap {
    private DCC dcc;
    private ArrayList<DCM> dcm;

    public DataCollectionMap() {
    }

    public DataCollectionMap(DCC dcc, ArrayList<DCM> dcm) {
        this.dcc = dcc;
        this.dcm = dcm;
    }

    public DCC getDcc() {
        return dcc;
    }

    public void setDcc(DCC dcc) {
        this.dcc = dcc;
    }

    public ArrayList<DCM> getDcm() {
        return dcm;
    }

    public void setDcm(ArrayList<DCM> dcm) {
        this.dcm = dcm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataCollectionMap that = (DataCollectionMap) o;

        if (dcc != null ? !dcc.equals(that.dcc) : that.dcc != null) return false;
        return !(dcm != null ? !dcm.equals(that.dcm) : that.dcm != null);

    }

    @Override
    public int hashCode() {
        int result = dcc != null ? dcc.hashCode() : 0;
        result = 31 * result + (dcm != null ? dcm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataCollectionMap{" +
                "dcc=" + dcc +
                ", dcm=" + dcm +
                '}';
    }
}
