package configurations.properties.io;

import exceptions.NoPropertyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by serhii on 08.04.15.
 */
public class PropertiesLoader {
    public static Properties load(final String filename)throws NoPropertyException{
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(filename)) {

          // load a properties file
            prop.load(input);
            return prop;
            // get the property value and print it out
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        throw new NoPropertyException("Can't load properties");
    }
}
