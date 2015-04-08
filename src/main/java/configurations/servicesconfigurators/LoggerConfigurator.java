package configurations.servicesconfigurators;

import org.apache.log4j.PropertyConfigurator;

/**
 * Created by serhii on 07.04.15.
 */
public class LoggerConfigurator {
    public static void initLogger() {
        try {
            PropertyConfigurator.configure(LoggerConfigurator.class.getClassLoader().getResourceAsStream("log4j.properties"));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

}
