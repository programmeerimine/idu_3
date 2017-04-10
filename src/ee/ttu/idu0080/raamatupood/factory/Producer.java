package ee.ttu.idu0080.raamatupood.factory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ee.ttu.idu0080.raamatupood.server.EmbeddedBroker;



public class Producer {
	private static final Logger log = Logger.getLogger(Producer.class);
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	public static final String SUBJECT = "Tellimus.vastus"; 


	private long timeToLive = 1000000;
	private String url = EmbeddedBroker.URL;

	

	public void run(String msg) {
		Connection connection = null;
		try {
			log.info("Factory: Connecting to URL: " + url);
			if (timeToLive != 0) {
				log.debug("Factory: Messages time to live " + timeToLive + " ms");
			}

			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);	
			Destination destination = session.createQueue(SUBJECT);
			MessageProducer producer = session.createProducer(destination);
			producer.setTimeToLive(timeToLive);
			
			
			sendAnswer(session, producer, msg);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
	protected void sendAnswer(Session session, MessageProducer producer, String msg) throws Exception {
				
		TextMessage message = session.createTextMessage(msg);
		log.debug("Factory: Sending message: " + message.getText());
		producer.send(message);			
	}
	
}
