package entities.datacollectionmap;

import java.util.ArrayList;

/**
 * Created by Logitech on 13.04.15.
 */
public class DCC {
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
}
