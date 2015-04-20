package utils.jsonprocessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.datacollectionmap.DataCollectionMap;

import java.io.IOException;

/**
 * Created by Logitech on 14.04.15.
 */
public class JSONProcessor {

    public static <T>String generateJSON(final T t) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(t);
    }

    public static <T> T parseJSON(Class<T> cls,final String content) throws IOException {
        ObjectMapper mapper =new ObjectMapper();
        return  mapper.readValue(content,cls);
    }
}
