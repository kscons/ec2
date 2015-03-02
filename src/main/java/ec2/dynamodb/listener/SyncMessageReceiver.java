package ec2.dynamodb.listener;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec2.dynamodb.DynamoDBUtil;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class SyncMessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(SyncMessageReceiver.class);
    private static final String DEFAULT_QUEUE_NAME = "TestQueue";
    private static final Region DEFAULT_REGION = Region.getRegion(Regions.US_WEST_2);
    final static DynamoDBUtil dbUtil = DynamoDBUtil.getInstance();

    public static void main(String args[]) {
        // Create the connection factory based on the config
        SQSConnectionFactory connectionFactory =
                SQSConnectionFactory.builder()
                        .withRegion(DEFAULT_REGION)
                        .withAWSCredentialsProvider(new ProfileCredentialsProvider())
                        .build();
        // Create the connection
        try {
            SQSConnection connection = connectionFactory.createConnection();
            // Create the session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(session.createQueue(DEFAULT_QUEUE_NAME));
            connection.start();
            receiveMessages(session, consumer);
            // Close the connection. This will close the session automatically
            connection.close();
            LOG.info("Connection close");
        } catch (JMSException jmse) {
            LOG.error("Can't create connection");
        }
    }

    private static void receiveMessages(Session session, final MessageConsumer consumer) {
        try {
            while (true) {
                System.out.println("Waiting for messages");
                // Wait 1 minute for a message
                javax.jms.Message message = consumer.receive(TimeUnit.MINUTES.toMillis(1));
                if (message != null) {
                    String mesageBody = ((TextMessage) message).getText();
                    dbUtil.insertRecord(JSONParser.getRecord(mesageBody));
                    message = null;
                    JOptionPane.showMessageDialog(null,"priyom");
                }

            }
        } catch (JMSException e) {
            System.err.println("Error receiving from SQS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
