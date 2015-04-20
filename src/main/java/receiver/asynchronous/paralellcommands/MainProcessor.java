package receiver.asynchronous.paralellcommands;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import configurations.servicesconfigurators.DynamoDBConfiGurator;
import entities.Log;
import entities.Metadata;
import exceptions.ZIPFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import configurations.servicesconfigurators.LoggerConfigurator;
import pools.threadpools.LogSavingPool;
import receiver.synchronous.synchronouscommands.*;
import receiver.synchronous.synchronouscommands.SaveLogs;
import utils.S3Util;
import utils.archiever.Decompresser;
import utils.dynamodb.AsyncDynamoDBSaver;
import utils.dynamodb.DynamoDBUtil;
import utils.jsonprocessors.LogParser;
import utils.redshift.hibernate.RedshiftHibernateUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MainProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(MainProcessor.class);

    static {
        LoggerConfigurator.initLogger();
    }

    public static void doProcess(final String message) {

        String bucketName = null;
        String key = null;
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONArray jsonArray = jsonObject.getJSONArray("Records");
            for (int i = 0;
                 i < jsonArray.length(); i++) {
                bucketName = jsonArray.getJSONObject(i).getJSONObject("s3").getJSONObject("bucket").getString("name");
                key = jsonArray.getJSONObject(i).getJSONObject("s3").getJSONObject("object").getString("key");
                key = key.replace("%3A", ":");
            }

            if (key.contains("clientinfo")) {

                StudiesHandler.process(bucketName,key);
                LOG.error("ASyncMessageReceiver:  Studies got ");

            } else {

                try {
                    final S3Object s3Object = S3Util.getFileFromBucket(bucketName, key);

                    //MetadataLogger.outputAllMetadata(s3Object);
                    //insertion metadata on ec2
                    final Metadata metadata = new Metadata(s3Object.getObjectMetadata());
                    SaveMetadata.save(metadata);
                    //Add object to output bucket
                    final String newKey = key.replace(".gz", "");
                    String objectToBucket = "";
                    try {
                        objectToBucket = new String(
                                Decompresser.decompress(s3Object.getObjectContent()).toByteArray(), "UTF-8");
                    } catch (UnsupportedEncodingException uee) {
                        LOG.error("ASyncMessageReceiver:  Encoding problems \n" + uee.toString());
                    } catch (ZIPFormatException npe) {
                        LOG.error("ASyncMessageReceiver:  Decompression failed \n" + npe.toString());
                    }
                    S3Util.putFileOnBucket(MessageReceiversConfigurator.getDefaultOutputBucketName(), newKey, (InputStream) new ByteArrayInputStream(objectToBucket.getBytes(StandardCharsets.UTF_8)));

                    // insert logs into redshift and dynamo
                    //SaveLogs.save(objectToBucket, metadata.getEventID(), metadata.getUserId());
                    //receiver.synchronous.synchronouscommands.SaveLogs();
                    receiver.asynchronous.paralellcommands.SaveLogs.save(objectToBucket, metadata.getEventID(), metadata.getUserId());
                   // RedshiftHibernateUtil.getInstance().insertLogs(LogParser.parseLog(objectToBucket, metadata.getEventID(), metadata.getUserId()));
              /*  LogParser.parseLog(objectToBucket, metadata.getEventID(), metadata.getUserId())
                        .stream()
                        .parallel()
                        .unordered()
                        .forEach(log -> {
                            RedshiftHibernateUtil.getInstance().insertLog(log);
                            DynamoDBUtil.insertLogRecord(DynamoDBConfiGurator.getLogsDynamodbOutputTableName(), log);
                        });*/

                    S3Util.deleteFileFromBucket(bucketName, key);
                } catch (AmazonS3Exception as3e) {
                    LOG.error(" ASyncMessageReceiver: No such key in s3 " + as3e);
                }
            }
        } catch (JSONException jse) {
            LOG.error(jse.toString());
        }
    }

}
