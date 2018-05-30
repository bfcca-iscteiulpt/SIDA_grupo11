package sensor;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import extra.XMLReader;

public class Sensor_simulacao extends Thread{

	private XMLReader xmlreader = new XMLReader();
	
	@Override
	public void run() {
		
		xmlreader.readXML2();
		
    	int qos             = Integer.parseInt(xmlreader.getSensor_qos());
    	String topic        = xmlreader.getSensor_topic();
    	String broker       = xmlreader.getSensor_broker();
    	String clientId     = "JavaSample";
    	MemoryPersistence persistence = new MemoryPersistence();
		
		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: "+broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			
			while(true) {
			
				Random r = new Random();
				double temperature = 15 + (16 - 15) * r.nextDouble();
				double humidity = 30 + (35 - 30) * r.nextDouble();;
	    
				SimpleDateFormat format_date = new SimpleDateFormat("dd/MM/yyyy");  
				String date = format_date.format(new Date());
	    
				SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");  
				String time = format_time.format(new Time(System.currentTimeMillis()));
	    	
	    
	    
//        					   {“temperature”:”XX.X”, “humidity”: “XX.X”, “date”: “DD/MM/AAAA”, “time”: “hh:mm:ss”}
				String content      = "{" + '"' + "temperature" + '"' + ":" + '"' + temperature + '"' + ", " + 
        								'"' + "humidity" + '"' + ":" + '"' + humidity + '"' + ", " + 
        								'"' + "date" + '"' + ":" + '"' + date + '"' + ", " +
        								'"' + "time" + '"' + ":" + '"' + time + '"' + "}" ;
        								

            		System.out.println("Publishing message: "+content);
            		MqttMessage message = new MqttMessage(content.getBytes());
            		message.setQos(qos);
            		sampleClient.publish(topic, message);
            		System.out.println("Message published");
//            		sampleClient.disconnect();
//            		System.out.println("Disconnected");
//            		System.exit(0);
            	
            		Thread.sleep(5000);
        	
			}
        } catch(MqttException me) {
           	System.out.println("reason "+me.getReasonCode());
           	System.out.println("msg "+me.getMessage());
           	System.out.println("loc "+me.getLocalizedMessage());
           	System.out.println("cause "+me.getCause());
           	System.out.println("excep "+me);
           	me.printStackTrace();
       } catch (InterruptedException e) {} 
        
		
	}
	
	public static void main(String[] args) {
		new Sensor_simulacao().run();

	}

}
