package utils.jsonprocessors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.DataSource;

import java.io.IOException;

/**
 * Created by Logitech on 15.04.15.
 */
public class ClientInfoProcessor {
    public static String generateJSON(final DataSource studies) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(studies);
    }

    public static DataSource parseJSON(final String content) throws IOException {
        ObjectMapper mapper =new ObjectMapper();
        return  mapper.readValue(content, DataSource.class);
    }
}
