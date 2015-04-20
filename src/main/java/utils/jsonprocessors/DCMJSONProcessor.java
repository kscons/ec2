package utils.jsonprocessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.datacollectionmap.DCC;
import entities.datacollectionmap.DCM;
import entities.datacollectionmap.DataCollectionMap;
import entities.datacollectionmap.Source;
import utils.S3Util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Logitech on 13.04.15.
 */
public class DCMJSONProcessor {
    public static String generateJSON(final DataCollectionMap dcm) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dcm);
    }

    public static DataCollectionMap parseJSON(final String content) throws IOException{
        ObjectMapper mapper =new ObjectMapper();
        return  mapper.readValue(content,DataCollectionMap.class);
    }

    public static void main(String a[]) {
        ArrayList<Source> sources=new ArrayList<>();
        sources.add(new Source("com.logitech.get_basic_caps","1.0.0"));
        DCC dcc=new DCC(sources);
        DCM dcm1=new DCM("client_version","com.logitech.get_basic_caps","0:0:1");
        DCM dcm2=new DCM("client_name","com.logitech.get_basic_caps","0:0:0:1");
        DCM dcm3=new DCM("os_type","com.logitech.get_basic_caps","0:0:0:0:0:0:200");
        DCM dcm4=new DCM("os_version","com.logitech.get_basic_caps","0:0:0:0:0:0:200");
        ArrayList<DCM> dcm =new ArrayList<>();
        dcm.add(dcm1);
        dcm.add(dcm2);
        dcm.add(dcm3);
        dcm.add(dcm4);
        DataCollectionMap dataCollectionMap=new DataCollectionMap(dcc,dcm);
        try {
            System.out.println(generateJSON(dataCollectionMap));
            DataCollectionMap dataCollectionMap1 = parseJSON(generateJSON(dataCollectionMap));

            String content ="{\n" +
                    "    \"dcc\":\n" +
                    "    {\n" +
                    "        \"sources\":\n" +
                    "        [\n" +
                    "            {\n" +
                    "                \"name\":\"com.logitech.get_basic_caps\",\n" +
                    "                \"version\":\"1.0.0\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"dcm\":\n" +
                    "    [\n" +
                    "        {\n" +
                    "            \"key\": \"client_version\",\n" +
                    "\t     \"source\": \"com.logitech.get_basic_caps\",\n" +
                    "            \"sampling_period\": \"0:0:1\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"key\": \"client_name\",\n" +
                    "            \"source\": \"com.logitech.get_basic_caps\",\n" +
                    "            \"sampling_period\": \"0:0:0:1\"    \n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"key\": \"os_type\",\n" +
                    "            \"source\": \"com.logitech.get_basic_caps\",\n" +
                    "            \"sampling_period\": \"0:0:0:0:0:0:200\"    \n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"key\": \"os_version\",\n" +
                    "            \"source\": \"com.logitech.get_basic_caps\",\n" +
                    "            \"sampling_period\": \"0:0:0:0:0:0:200\"    \n" +
                    "        }\n" +
                    "    ]\n" +
                    "}\n";
            DataCollectionMap dataCollectionMap2=parseJSON(content);
            System.out.println();

            String key = String.format("%s:%s%s", "eu-west1", String.valueOf(Math.random() * 1000000), "dcm" + ".json");

                ByteArrayOutputStream
                        baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);


                oos.writeObject(generateJSON(dataCollectionMap));

                oos.flush();
                oos.close();

                InputStream is = new ByteArrayInputStream(baos.toByteArray());
            S3Util.putFileOnBucket("logitech-analytics-ksolod-eu-west-1-dcm",key,is);

        } catch (IOException jp) {
            jp.printStackTrace();
        }
    }

}
