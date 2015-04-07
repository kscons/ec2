package receiver.asynchronous;


import com.amazonaws.util.Base64;
import pools.threadpools.MainProcessorThreadPool;
import receiver.asynchronous.paralellcommands.MainProcessor;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;


public class MessageHandler {
    public static void handleMessage(final Message message) throws JMSException {
        System.out.println("Got message " + message.getJMSMessageID());
        System.out.println("Content: ");
        MainProcessorThreadPool.runProcess(new Runnable() {
            @Override
            public void run() {
                try {
                    MainProcessor.doProcess(((TextMessage) message).getText());
                } catch (JMSException jmse) {
                    jmse.printStackTrace();
                }
            }
        });

    }
}
