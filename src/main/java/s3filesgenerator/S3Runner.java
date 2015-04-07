package s3filesgenerator;


import utils.S3Util;

import java.io.IOException;


public class S3Runner {
    public static void main(String[] args) {
     // S3Util.cleanBucket("logitech-analytics-ksolod-eu-west-1-sqsinput");
       // S3Util.cleanBucket("logitech-analytics-ksolod-eu-west-1-sqsoutput");

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            try {
                S3.putRandomData();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }
}