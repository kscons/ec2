package receiver.asynchronous.paralellcommands;

import com.amazonaws.services.s3.model.S3Object;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import entities.datacollectionmap.DataCollectionMap;
import exceptions.s3.NoFileInBucketException;
import s3filesgenerator.S3;
import entities.DataSource;
import studies.Studies;
import utils.s3.S3Util;
import utils.jsonprocessors.DCMJSONProcessor;
import utils.jsonprocessors.ClientInfoProcessor;

import java.io.*;

/**
 * Created by Logitech on 15.04.15.
 */
public class StudiesHandler {
    public static void process(String bucket, String key) {

        try {
            S3Object s3Object = S3Util.getFileFromBucket(bucket, key);
            DataSource studies = ClientInfoProcessor.parseJSON(getStringFromInputStream(s3Object.getObjectContent()));
         DataCollectionMap dataCollectionMap= Studies.generate(studies);
        sendDcmIntoS3(dataCollectionMap,key);
            //generate client behaviour
          String  newKey=key.replace("clientinfo.json","report.gz");
            S3.pushReportByClientID(newKey);
        } catch (IOException io) {
            io.printStackTrace();
        }catch (NoFileInBucketException nfbe){
            nfbe.printStackTrace();
        }
    }

    public static void sendDcmIntoS3(DataCollectionMap dcm,String key) throws IOException{
        ByteArrayOutputStream
                baos = new ByteArrayOutputStream();
        baos.write(DCMJSONProcessor.generateJSON(dcm).getBytes());
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        String newKey=key.replace("clientinfo","dcm");
        S3Util.putFileOnBucket(MessageReceiversConfigurator.getDefaultOutputBucketName(), newKey, is);



    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
