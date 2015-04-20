package s3filesgenerator;


import configurations.servicesconfigurators.MessageReceiversConfigurator;
import utils.S3Util;

import java.io.IOException;


public class S3Runner {
    public static int REPORT_COUNT=5;

    public static void main(String[] a) {
        S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultOutputBucketName());
        S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultInputBucketName());
        for (int i = 0; i < REPORT_COUNT; i++) {
            try {
                S3.putRandomData();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }


}