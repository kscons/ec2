package receiver.asynchronous;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import configurations.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import configurations.servicesconfigurators.MessageReceiversConfigurator;
import receiver.Receiver;
import receiver.asynchronous.paralellcommands.MainProcessor;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class AsyncMessageReceiver implements Receiver {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncMessageReceiver.class);
    private String name;
    private boolean running = false;

    public AsyncMessageReceiver(String name) {
        this.name = name;
    }


    public void start() throws JMSException, InterruptedException {

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
            MessageConsumer consumer = session.createConsumer(session.createQueue(name));

            ReceiverCallback callback = new ReceiverCallback(this);
            consumer.setMessageListener(callback);

            // No messages will be processed until this is called
            connection.start();
            running = true;

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


    private class ReceiverCallback implements MessageListener {
        // Used to listen for message silence
        private volatile long timeOfLastMessage = System.nanoTime();
        private AsyncMessageReceiver receiver;

        public ReceiverCallback(AsyncMessageReceiver receiver) {
            this.receiver = receiver;
        }

        public void waitForOneMinuteOfSilence() throws InterruptedException {
            for (; ; ) {
                long timeSinceLastMessage = System.nanoTime() - timeOfLastMessage;
                long remainingTillOneMinuteOfSilence =
                        TimeUnit.MILLISECONDS.toNanos(100000) - timeSinceLastMessage;
                if (remainingTillOneMinuteOfSilence < 0) {
                    //   break;
                }
                if (!receiver.isRunning()){
                    break;
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
            } catch (JMSException e) {
                System.err.println("Error processing message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void stop() {
        running = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

