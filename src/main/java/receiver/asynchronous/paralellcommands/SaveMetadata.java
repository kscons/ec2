package receiver.asynchronous.paralellcommands;

import entities.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import receiver.synchronous.synchronouscommands.MainProcessor;
import utils.dynamodb.MapperDynamoDBUtil;


public class SaveMetadata {
    private static final Logger LOG = LoggerFactory.getLogger(MainProcessor.class);

    public static void save(final Metadata metadata){
        MapperDynamoDBUtil.insertRecord(metadata);
        /*
        MetadataSavingPool.runProcess(new Runnable() {
            @Override
            public void run() {
                try {
                    DynamoDBUtil.insertMetadataRecord(DynamoDBConfiGurator.getMetadataOutputTable(), metadata);
                } catch (IllegalArgumentException iae) {
                    LOG.info("SyncMessageReceiver: Metadata wasn't written. Some problems with Metadata.\n" + iae.toString());
                }
                LOG.info("SyncMessageReceiver: Metadata was written");

            }
        });*/

    }
}
