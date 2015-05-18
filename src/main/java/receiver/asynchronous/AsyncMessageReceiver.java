package receiver.asynchronous;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import configurations.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import receiver.asynchronous.paralellcommands.MainProcessor;
import utils.redshift.hibernate.RedshiftHibernateUtil;

import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncMessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageReceiver.class);
    private static final long autoCloseTime = 1;

    public static void main(String args[]) throws JMSException, InterruptedException {
        // ExampleConfiguration config = ExampleConfiguration.parseConfig("AsyncMessageReceiver", args);
        Configurator.configureAll("config.properties");
        //RedshiftHibernateUtil redshiftHibernateUtil=RedshiftHibernateUtil.getInstance();

        // ExampleCommon.setupLogging();
        try {

            //
            // Create connection with SQS
            SQSConnectionFactory connectionFactory =
                    SQSConnectionFactory.builder()
                            .withRegion(MessageReceiversConfigurator.getDefaultRegion())
                            .withAWSCredentialsProvider(new ProfileCredentialsProvider())
                            .build();
            // Create the connection
            SQSConnection connection = connectionFactory.createConnection();

            // Create the session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(session.createQueue(MessageReceiversConfigurator.getDefaultQueueName()));

            ReceiverCallback callback = new ReceiverCallback();
            consumer.setMessageListener(callback);

            // No messages will be processed until this is called
            connection.start();

            callback.waitForOneMinuteOfSilence();

            System.out.println("Returning after one minute of silence");

            // Close the connection. This will close the session automatically
            connection.close();
            System.out.println("Connection closed");
            LOG.info(" ASyncMessageReceiver: Connection close");
        } catch (JMSException jmse) {
            LOG.error(" ASyncMessageReceiver: Can't create connection " + jmse.toString());
        }

    }


    private static class ReceiverCallback implements MessageListener {
        // Used to listen for message silence
        private volatile long timeOfLastMessage = System.nanoTime();

        public void waitForOneMinuteOfSilence() throws InterruptedException {
            for (; ; ) {
                long timeSinceLastMessage = System.nanoTime() - timeOfLastMessage;
                long remainingTillOneMinuteOfSilence =
                        TimeUnit.MILLISECONDS.toNanos(100000) - timeSinceLastMessage;
                if (remainingTillOneMinuteOfSilence < 0) {
                 //   break;
                }
              //  TimeUnit.NANOSECONDS.sleep(remainingTillOneMinuteOfSilence);
            }
        }


        @Override
        public void onMessage(Message message) {
            try {
                MainProcessor.doProcess(((TextMessage) message).getText());
                message.acknowledge();
            // System.out.println("Acknowledged message " + message.getJMSMessageID());
            timeOfLastMessage = System.nanoTime();
        }catch(JMSException e){
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
}

