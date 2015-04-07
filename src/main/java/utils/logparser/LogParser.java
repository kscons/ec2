package utils.logparser;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entities.Log;

import java.util.ArrayList;


public class LogParser {
    private static final Logger LOG = LoggerFactory.getLogger(LogParser.class);

    public static ArrayList<Log> parseLog( final String logs, final String id,final long userId){
        ArrayList<Log> logsList=new ArrayList<Log>();
        try{
        JSONArray jsonArray=new JSONArray(logs);
            for(int i=0;i<jsonArray.length();i++){
                 Log log=new Log();
               log.setTime( jsonArray.getJSONObject(i).get("timestamp").toString());
                log.setKey(jsonArray.getJSONObject(i).get("key").toString());
                log.setValue(jsonArray.getJSONObject(i).get("value").toString());
                log.setUserId(userId);
                log.setId(Math.abs((int)(Math.random()*10000))+"");
                logsList.add(log);

            }

        }catch (JSONException jsne){
        LOG.error("/t LogParser : wrong format"+jsne.toString());
        }
        return logsList;
    }
}