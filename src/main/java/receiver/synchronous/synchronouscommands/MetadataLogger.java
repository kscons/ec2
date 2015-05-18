package receiver.synchronous.synchronouscommands;

import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by serhii on 07.04.15.
 */
@Deprecated
public class MetadataLogger {
    private static final Logger LOG = LoggerFactory.getLogger(MainProcessor.class);

    public static void outputAllMetadata(final S3Object s3Object) {

        String s = "SyncMessageReceiver:  ObjectMetadata";
        Map<String, Object> map = s3Object.getObjectMetadata().getRawMetadata();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key1 = entry.getKey().toString();
            ;
            Object value = entry.getValue();
            s += "\nkey== " + key1 + "| value ==" + value;
        }
        LOG.info(s);
        s = " SyncMessageReceiver: UserMetadata";
        Map<String, String> md = s3Object.getObjectMetadata().getUserMetadata();
        for (Map.Entry<String, String> entry : md.entrySet()) {
            String key1 = entry.getKey().toString();
            ;
            String value = entry.getValue();
            s += "\nkey== " + key1 + "| value ==" + value;
        }

        s += "\n----------------------------\n";
        ArrayList a = new ArrayList(md.values());
        for (Object el : a) {
            s += "\n" + el;
        }
        LOG.info(s);
    }

}
