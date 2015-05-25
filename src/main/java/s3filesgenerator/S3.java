package s3filesgenerator;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class S3 {

    private static final Logger LOG = LoggerFactory.getLogger(S3.class);

    private static final String bucket = "logitech-analytics-ksolod-eu-west-1-sqsinput";
    private static final Regions region = Regions.EU_WEST_1;
    private static final String reportName = "report.json";
    private static final Random random = new Random();
    private static final AmazonS3 s3;

    static {
        s3 = new AmazonS3Client(new ProfileCredentialsProvider());
        s3.setRegion(Region.getRegion(region));
    }

    public static void putRandomData() throws IOException {

        InputStream report = S3.class.getClassLoader().getResourceAsStream("archive_sample.gz");
        String eventID = UUID.randomUUID().toString();
        ObjectMetadata metadata = new ObjectMetadata();

        //adding user metadata
        metadata.addUserMetadata("eventId", Math.abs(random.nextInt()) +"");
        metadata.addUserMetadata("userId", random.nextInt() + "");
        metadata.addUserMetadata("time", "" + random.nextInt());
        metadata.addUserMetadata("eventType", "type" + random.nextInt());
        metadata.addUserMetadata("value", "true");
        metadata.addUserMetadata("machineId", "" + random.nextInt());

        metadata.setContentType("application/json");
        metadata.setContentLength(report.available());
        metadata.setHeader("x-amz-meta-custom", String.valueOf(random.nextInt(1000000)));
        String key = String.format("%s:%s/%s", region.getName(), eventID, reportName + ".gz");


        metadata.setContentLength(report.available());
        LOG.info(key);
        s3.putObject(bucket, key, report, metadata);
    }

    public static void pushReportByClientID(String key) throws  IOException{
        InputStream report = S3.class.getClassLoader().getResourceAsStream("archive_sample.gz");

        ObjectMetadata metadata = new ObjectMetadata();

        //adding user metadata
        metadata.addUserMetadata("eventid", Math.abs(random.nextInt()) +"");
        metadata.addUserMetadata("userid", random.nextInt() + "");
        metadata.addUserMetadata("time", "" + random.nextInt());
        metadata.addUserMetadata("eventType", "type" + random.nextInt());
        metadata.addUserMetadata("value", "true");
        metadata.addUserMetadata("machineid", "" + random.nextInt());

        metadata.setContentType("application/json");
        metadata.setContentLength(report.available());
        metadata.setHeader("x-amz-meta-custom", String.valueOf(random.nextInt(1000000)));
        String newKey = key+".json.gz";
        s3.putObject(bucket, newKey, report, metadata);
    }
}
