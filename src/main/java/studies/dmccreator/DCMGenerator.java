package studies.dmccreator;

import entities.datacollectionmap.DCC;
import entities.datacollectionmap.DCM;
import entities.datacollectionmap.DataCollectionMap;
import entities.datacollectionmap.Source;
import studies.Studies;

import java.util.ArrayList;

/**
 * Created by Logitech on 15.04.15.
 */
public class DCMGenerator {
    public static DataCollectionMap generate(Studies studies){
        ArrayList<Source> sources=new ArrayList<>();
        sources.add(new Source("com.logitech.get_basic_caps","1.0.0"));
        DCC dcc=new DCC(sources);
        DCM dcm1=new DCM("client_version","com.logitech.get_basic_caps","0:0:1");
        String somerule="";
        if (studies.getRegion().equals("eu-west-1")){
            dcm1.setSamplingPeriod("sampling for region eu-west-1");
            somerule="region1 rule";

        }
        if (studies.getRegion().equals("eu-west-2")){
            dcm1.setSamplingPeriod("sampling for region eu-west-2");
            somerule="region2 rule";
        }
        DCM dcm2=new DCM("client_name",studies.getDeviceType(),"0:0:0:1");
        DCM dcm3=new DCM("os_type",studies.getOs(),somerule);
        DCM dcm4=new DCM("os_version",somerule,"0:0:0:0:0:0:200");
        ArrayList<DCM> dcm =new ArrayList<>();
        dcm.add(dcm1);
        dcm.add(dcm2);
        dcm.add(dcm3);
        dcm.add(dcm4);
        DataCollectionMap dataCollectionMap=new DataCollectionMap(dcc,dcm);
        return dataCollectionMap;
    }
}
