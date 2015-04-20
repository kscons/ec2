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
}
