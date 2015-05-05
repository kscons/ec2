package studies.s3datasourcegenerator;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entities.DataSource;
import utils.s3.S3Util;
import utils.jsonprocessors.ClientInfoProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class S3DataSourceGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(S3DataSourceGenerator.class);

    private static final String bucket = "logitech-analytics-ksolod-eu-west-1-sqsinput";
    private static final Regions region = Regions.EU_WEST_1;
    private static final String reportName = "report.json";
    private static final Random random = new Random();
    private static final AmazonS3 s3;

    static {
        s3 = new AmazonS3Client(new ProfileCredentialsProvider());
        s3.setRegion(Region.getRegion(region));
    }

    public static void putRandomData(DataSource studies) throws IOException {


        ByteArrayOutputStream
                baos = new ByteArrayOutputStream();
        baos.write(ClientInfoProcessor.generateJSON(studies).getBytes());


        InputStream report = new ByteArrayInputStream(baos.toByteArray());

        ObjectMetadata metadata = new ObjectMetadata();


        metadata.setContentType("application/json");
        metadata.setContentLength(report.available());

        String key = String.format("%s:%s/%s", region.getName(), studies.getId(), "clientinfo" + ".json");


        metadata.setContentLength(report.available());
        LOG.info(key);
        s3.putObject(bucket, key, report, metadata);
    }

    public static void main(String[] args) {
          S3Util.cleanBucket("logitech-analytics-ksolod-eu-west-1-sqsinput");
          S3Util.cleanBucket("logitech-analytics-ksolod-eu-west-1-sqsoutput");
        ArrayList<DataSource> studiesArrayList = new ArrayList<>();
        studiesArrayList.add(new DataSource(UUID.randomUUID().toString(), "mouse", "os x", "eu-west-1"));
        studiesArrayList.add(new DataSource(UUID.randomUUID().toString(), "keyboard", "windows", "eu-west-2"));
        studiesArrayList.add(new DataSource(UUID.randomUUID().toString(), "mouse", "ubuntu", "eu-west-1"));
        studiesArrayList.add(new DataSource(UUID.randomUUID().toString(), "camera", "os x", "eu-west-2"));
        for (DataSource studies : studiesArrayList) {
            try {
                putRandomData(studies);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
