package ee.ttu.idu0080.raamatupood.factory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ee.ttu.idu0080.raamatupood.server.EmbeddedBroker;


public class Consumer {
	private static final Logger log = Logger.getLogger(Consumer.class);
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = EmbeddedBroker.URL;
	private String SUBJECT = "Tellimus.edastamine";

	public static void main(String[] args) {
		Consumer consumerTool = new Consumer();
		consumerTool.run();
	}

	public void run() {
		Connection connection = null;
		try {
			log.info("Factory: Connecting to URL: " + url);
			log.info("Factory: Consuming queue : " + SUBJECT);

		
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.setExceptionListener(new ExceptionListenerImpl());
			connection.start();

			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(SUBJECT);
			MessageConsumer consumer = session.createConsumer(destination);

			
			consumer.setMessageListener(new MessageListenerImpl());

			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
	class MessageListenerImpl implements javax.jms.MessageListener {

		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
					TextMessage txtMsg = (TextMessage) message;
					String msg = txtMsg.getText();
					log.info("Factory: Received: " + msg);
				} else if (message instanceof ObjectMessage) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					String msg = objectMessage.getObject().toString();
					log.info("Factory: Tellimus vastuvõetud.");
					log.info("Factory: Received: " + msg);							
					Producer producerTool = new Producer();
					producerTool.run(msg);

				} else {
					log.info("Factory: Received: " + message);
				}

			} catch (JMSException e) {
				log.warn("Factory: Caught: " + e);
				e.printStackTrace();
			}
		}
	}

	
	class ExceptionListenerImpl implements javax.jms.ExceptionListener {

		public synchronized void onException(JMSException ex) {
			log.error("Factory: JMS Exception occured. Shutting down client.");
			ex.printStackTrace();
		}
	}

}