package receiver.synchronous;

import receiver.synchronous.synchronouscommands.MainProcessor;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import configurations.servicesconfigurators.LoggerConfigurator;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * These class is the main class in project that listen the updates in SQS queue.
 */

public class SyncMessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(SyncMessageReceiver.class);


    public static void main(String args[]) {
        // Create the connection factory based on the config
        LoggerConfigurator.initLogger();
        // Create connection with SQS
        SQSConnectionFactory connectionFactory =
                SQSConnectionFactory.builder()
                        .withRegion(MessageReceiversConfigurator.getDefaultRegion())
                        .withAWSCredentialsProvider(new ProfileCredentialsProvider())
                        .build();
        // Create the connection
        try {

            SQSConnection connection = connectionFactory.createConnection();
            // Create the session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(session.createQueue(MessageReceiversConfigurator.getDefaultQueueName()));
            connection.start();
            receiveMessages(session, consumer);
            // Close the connection. This will close the session automatically
            connection.close();
            LOG.info(" SyncMessageReceiver: Connection close");
        } catch (JMSException jmse) {
            LOG.error(" SyncMessageReceiver: Can't create connection");
        }


    }


    //
    private static void receiveMessages(Session session, final MessageConsumer consumer) {
        try {
            while (true) {
                LOG.debug("SyncMessageReceiver: Waiting for messages");
                // Wait 1 minute for a message
                javax.jms.Message message = consumer.receive(TimeUnit.DAYS.toMillis(MessageReceiversConfigurator.getAliveDaysCount()));
                if (message != null) {
                    String messageBody = ((TextMessage) message).getText();
                    MainProcessor.doProcess(messageBody);
                    message = null;
                }
            }
        } catch (JMSException e) {
            LOG.error("SyncMessageReceiver: Error receiving from SQS: " + e.getMessage());

        }
    }


}

    