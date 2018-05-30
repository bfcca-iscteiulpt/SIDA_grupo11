package mongoDB;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import extra.XMLReader;

public class Sensor_to_Java extends Thread{

	private static XMLReader xmlreader = new XMLReader();
	
	@Override
	public void run(){
		
		xmlreader.readXML2();
		
		MqttClient client;
		MemoryPersistence persistence;
		MqttConnectOptions conn;
		
    	final int qos = Integer.parseInt(xmlreader.getSensor_qos());
    	String topic = xmlreader.getSensor_topic();
    	String broker= xmlreader.getSensor_broker();
		String str = "password";
		char[] accessKey = str.toCharArray();
		String clientID = "userID";


		try {
			persistence = new MemoryPersistence();
			client = new MqttClient(broker, clientID, persistence);
			conn = new MqttConnectOptions();
			conn.setCleanSession(true);
			conn.setPassword(accessKey);
			conn.setUserName(clientID);
			client.connect(conn);
			client.subscribe(topic);
			
			if(client.isConnected()) {
				System.out.println("Connected..");
	    	}else {
	    		System.out.println("Unable to connect");
	    		System.exit(0);
	    	}
	    	           
	    		
	    	while(true){	
			client.setCallback(new MqttCallback() {
				public void connectionLost(Throwable cause) {}
	    	        	
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					if(message.toString().startsWith("{")){
						System.out.println("Message received: " + message.toString());
						message.setQos(qos);
						new Java_to_MongoDB(message.toString()).run();

	    			}
				}
	    	        	
				public void deliveryComplete(IMqttDeliveryToken token) {
				}
			});

	    	}        
	    		
		}catch(Exception x) {
			x.printStackTrace();
		}
			
			
	}		
			
}