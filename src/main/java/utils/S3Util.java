package utils;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.List;

/** 
 * This class consists exclusively of static methods that send,
 * get and delete S3Objects from S3 bucket.
 * It also create  AmazonS3client that enables us to do this manipulations.
 * There is a method cleanBucket that deletes all object in bucket.
 * It doesn'n use in data processing.
 * But it is very useful for testing.
 */
public class S3Util {

    private static final Logger LOG = LoggerFactory.getLogger(S3Util.class);
    private static final Regions region = Regions.EU_WEST_1;
    private static final AmazonS3 s3;

    /**
     * The initialization of AmazonS3client.
     */
    static {
        s3 = new AmazonS3Client(new ProfileCredentialsProvider());
        s3.setRegion(Region.getRegion(region));
    }

    /**
     * This method use in SyncMessageReceiver.It gets S3Object from bucket.
     * @param bucketName - bucket from which we want get object
     * @param key-key that enables us to get this object
     * @return S3Object which include data and metadata
     */
    public static S3Object getFileFromBucket(final String bucketName, final String key) {

        S3Object s3object = s3.getObject(new GetObjectRequest(
                bucketName, key));
        LOG.info("EC2--  Object from " + bucketName + " " + key + "  got");
        return s3object;


    }

    /**
     *This method uses to save decompressed JSON in another bucket.
     * @param bucketName- destination bucket.
     * @param key- the S3Object will saved with this key in bucketName bucket
     * @param in - data that saves on S3
     */
    public static void putFileOnBucket(final String bucketName, final String key,final InputStream in) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/json");
        s3.putObject(bucketName, key, in, metadata);
        LOG.info("EC2--  Object to " + bucketName + " " + key + "  sent");
    }

    /**
     *Which this method we would be able to delete object that we selected.
     * Method is used to delete archives that have already been extracted in message listener
     * @param bucketName -the name of S3 bucket from which you need delete object
     * @param key- destination to object that will be removed from S3 bucketName
     */
    public static void deleteFileFromBucket(final String bucketName, final String key) {
        s3.deleteObject(new DeleteObjectRequest(bucketName, key));
        LOG.error("S3Util:  file deleted from\n  " + "bucket= " + bucketName + ",  key=" + key);

    }

    /**
     * Method uses for develompent convenience.
     * But it can be used in the future to solve some problems.
     * Which this method you can delete all objects from bucket and makes it empty.
     * @param bucketName -bucket which will be cleaned
     */
    public static void cleanBucket(final String bucketName) {
        ObjectListing listing = s3.listObjects(bucketName);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        while (listing.isTruncated()) {
            listing = s3.listNextBatchOfObjects(listing);
            summaries.addAll(listing.getObjectSummaries());
        }
        for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
            deleteFileFromBucket(objectSummary.getBucketName(), objectSummary.getKey());
        }
        LOG.error("S3Util:All elements deleted from " + bucketName);
    }

    public static List<S3ObjectSummary> getAllObjectSummaries(final String bucketName){
        ObjectListing listing = s3.listObjects( bucketName );
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }
        return summaries;
    }

}