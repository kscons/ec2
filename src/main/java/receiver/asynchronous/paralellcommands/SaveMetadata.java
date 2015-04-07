package receiver.asynchronous.paralellcommands;

import configurations.MessageReceiversConfigurator;
import entities.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pools.threadpools.MetadataSavingPool;
import receiver.synchronous.synchronouscommands.MainProcessor;
import utils.dynamodb.DynamoDBUtil;


public class SaveMetadata {
    private static final Logger LOG = LoggerFactory.getLogger(MainProcessor.class);

    public static void save(final Metadata metadata){
        MetadataSavingPool.runProcess(new Runnable() {
            @Override
            public void run() {
                try {
                    DynamoDBUtil.insertMetadataRecord(MessageReceiversConfigurator.getMetadataOutputTable(), metadata);
                } catch (IllegalArgumentException iae) {
                    LOG.info("SyncMessageReceiver: Metadata wasn't written. Some problems with Metadata.\n" + iae.toString());
                }
                LOG.info("SyncMessageReceiver: Metadata was written");

            }
        });

    }
}
