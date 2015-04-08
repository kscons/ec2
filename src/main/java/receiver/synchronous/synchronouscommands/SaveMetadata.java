package receiver.synchronous.synchronouscommands;

import configurations.servicesconfigurators.DynamoDBConfiGurator;
import entities.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.dynamodb.DynamoDBUtil;

/**
 * Created by serhii on 07.04.15.
 */
public class SaveMetadata {
    private static final Logger LOG = LoggerFactory.getLogger(MainProcessor.class);

    public static void save(final Metadata metadata){

        try {
            DynamoDBUtil.insertMetadataRecord(DynamoDBConfiGurator.getMetadataOutputTable(), metadata);
        } catch (IllegalArgumentException iae) {
            LOG.info("SyncMessageReceiver: Metadata wasn't written. Some problems with Metadata.\n" + iae.toString());
        }
        LOG.info("SyncMessageReceiver: Metadata was written");

    }
}
