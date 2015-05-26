package configurations;

import configurations.properties.io.PropertiesLoader;
import configurations.servicesconfigurators.DynamoDBConfiGurator;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import configurations.servicesconfigurators.RedshiftConfigurator;
import exceptions.NoPropertyException;

import java.util.Properties;

/**
 * Created by serhii on 08.04.15.
 */
@Deprecated
public class Configurator {
    public static void configureAll(String fileName) {
        try {
            Properties properties = PropertiesLoader.load(fileName);
            MessageReceiversConfigurator.configure(properties);
            DynamoDBConfiGurator.configure(properties);
            RedshiftConfigurator.configure(properties);
        } catch (NoPropertyException nop) {
            nop.printStackTrace();
        }
    }

    public static void main(String[] ar) {
        configureAll("config.properties");
    }
}
