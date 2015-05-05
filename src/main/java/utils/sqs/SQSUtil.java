package utils.sqs;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.AddPermissionRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import org.infinispan.commands.read.EntrySetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.s3.S3Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Logitech on 05.05.15.
 */
public class SQSUtil {
    private static final Logger LOG = LoggerFactory.getLogger(S3Util.class);
    private static final Regions region = Regions.EU_WEST_1;
    private static final AmazonSQS sqs;

    /**
     * The initialization of AmazonS3client.
     */
    static {
        sqs = new AmazonSQSClient(new ProfileCredentialsProvider());
        sqs.setRegion(Region.getRegion(region));
    }
    public static void createQueue(final String sqsName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest().withQueueName(sqsName);
        String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        LOG.info("SQSUtil: queue " + sqsName + " was created.");
    }

    public static void deleteMessage(final String sqsName){
        System.out.println("Deleting a message.\n");
        String messageReceiptHandle =null;
                //messages.get(0).getReceiptHandle();
       final String queueUrl=getQueueUrl(sqsName);
        sqs.deleteMessage(new DeleteMessageRequest()
                .withQueueUrl(queueUrl)
                .withReceiptHandle(messageReceiptHandle));
    }

    public static void deleteQueue(final String sqsName){
        sqs.deleteQueue(getQueueUrl(sqsName));
    }
    public static String getQueueUrl(final String sqsName){
       return sqs.getQueueUrl(sqsName).getQueueUrl();
    }

    public static boolean isExist(final String sqsName){
        final String queueUrl=getQueueUrl(sqsName);
      return   sqs.listQueues().getQueueUrls()
              .stream()
              .filter(url-> url==queueUrl)
              .collect(Collectors.toList())
              .size()==1;

    }

   public static Map<String,String> getQueueAttributes(final String sqsName){
       final String queueUrl=getQueueUrl(sqsName);
       GetQueueAttributesRequest getQueueAttributesRequest=new GetQueueAttributesRequest().withQueueUrl(queueUrl)
               .withAttributeNames("All");

      return  sqs.getQueueAttributes(getQueueAttributesRequest).getAttributes();
   }

    public static String getQueueArn(final String sqsName){
       return getQueueAttributes(sqsName).get("QueueArn");
    }


    public static void addPermissionForS3Bucket(final String sqsName,final  String bucketName){
    //    AddPermissionRequest addPermissionRequest=new AddPermissionRequest().w;
   //     sqs.addPermission(addPermissionRequest);
    }
}
