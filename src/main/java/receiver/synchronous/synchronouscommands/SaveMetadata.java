package receiver.synchronous.synchronouscommands;

import configurations.servicesconfigurators.DynamoDBConfiGurator;
import entities.Metadata;
import exceptions.dynamodb.MetadataFieldNullException;
import exceptions.dynamodb.NonExistTableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.dynamodb.DynamoDBUtil;

/**
 * Created by serhii on 07.04.15.
 */
@Deprecated
public class SaveMetadata {
    private static final Logger LOG = LoggerFactory.getLogger(MainProcessor.class);

    public static void save(final Metadata metadata){

        try {
            DynamoDBUtil.insertMetadataRecord(DynamoDBConfiGurator.getMetadataOutputTable(), metadata);
            LOG.info("SyncMessageReceiver: Metadata was written");
        } catch (NonExistTableException nte) {
            LOG.info("SyncMessageReceiver: Metadata wasn't written. Some problems with Metadata.\n" + nte.toString());
        }catch (MetadataFieldNullException mfne){
            LOG.info("SyncMessageReceiver: Metadata wasn't written. Incorrect table name.\n" + mfne.toString());

        }



    }
}
