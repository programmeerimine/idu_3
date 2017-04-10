package ee.ttu.idu0080.raamatupood.shop;



import java.util.Arrays;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ee.ttu.idu0080.raamatupood.server.EmbeddedBroker;
import ee.ttu.idu0080.raamatupood.types.Tellimus;
import ee.ttu.idu0080.raamatupood.types.Toode;


public class Producer {
	private static final Logger log = Logger.getLogger(Producer.class);
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	public static final String SUBJECT = "Tellimus.edastamine";


	private long timeToLive = 1000000;
	private String url = EmbeddedBroker.URL;

	

	public void run() {
		Connection connection = null;
		try {
			log.info("Shop: Connecting to URL: " + url);
			if (timeToLive != 0) {
				log.debug("Shop: Messages time to live " + timeToLive + " ms");
			}

			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(SUBJECT);
			MessageProducer producer = session.createProducer(destination);
			producer.setTimeToLive(timeToLive);

			
			sendTellimus(session, producer);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
	protected void sendTellimus(Session session, MessageProducer producer) throws Exception {
		
		ObjectMessage objectMessage = session.createObjectMessage();
		objectMessage.setObject(looTellimus()); 
		producer.send(objectMessage);
		log.debug("Shop: Sending message: " + objectMessage.getObject().toString());
	
	}
	
	protected Tellimus looTellimus(){
		Toode a = new Toode("Leib", 10);
		Toode b = new Toode("Piim", 5);
		Toode c = new Toode("Sai", 15);
		
		List<Toode> asd = Arrays.asList(a, b, c);
		
		return new Tellimus(asd);
		
	}

	
}


