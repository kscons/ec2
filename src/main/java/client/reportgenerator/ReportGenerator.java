package client.reportgenerator;


import configurations.servicesconfigurators.MessageReceiversConfigurator;
import utils.s3.S3Util;

import java.io.IOException;


public class ReportGenerator {
    public static int REPORT_COUNT=5;

    public static void main(String[] a) {
        S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultOutputBucketName());
        S3Util.cleanBucket(MessageReceiversConfigurator.getDefaultInputBucketName());
        for (int i = 0; i < REPORT_COUNT; i++) {
            try {
                S3ClientHelper.putRandomData();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }


}