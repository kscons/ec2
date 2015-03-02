package ec2.dynamodb.listener;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec2.dynamodb.Record;


public class JSONParser {
    private static final Logger LOG = LoggerFactory.getLogger(JSONParser.class);

    static Record getRecord(String json) {
        Record record = new Record();

        record.setEventID("1");
        record.setUserId(1);
        record.setMachineId(6);
        record.setTime(1);
        record.setEventType("1");
        record.setValue("1");
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Records");
            for (int i = 0;
                 i < jsonArray.length(); i++) {

                record.setValue(jsonArray.getJSONObject(i).getString("eventName"));
                record.setEventType(jsonArray.getJSONObject(i).getString("eventTime"));
            }

        } catch (JSONException jse) {
            LOG.error(jse.toString());
        }

        return record;
    }
}
