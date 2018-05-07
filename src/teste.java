        import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
        import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
        import org.eclipse.paho.client.mqttv3.MqttException;
        import org.eclipse.paho.client.mqttv3.MqttMessage;
        import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

        public class teste {
        	
        public static void main(String[] args) {
        	MqttClient client;
        	MqttMessage msg;
        	MemoryPersistence persistence;
        	MqttConnectOptions conn;
        	IMqttMessageListener listen;
        	
        	String broker = "tcp://iot.eclipse.org:1883";
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
       	        
        		if(client.isConnected()) {
        			System.out.println("Connected..");
        	    }else {
        	    	System.out.println("Unable to connect");
        	    	System.exit(0);
        	    }
        	        
        	         
        	        
        		client.setCallback(new MqttCallback() {
        			public void connectionLost(Throwable cause) {}
        	        	
        			public void messageArrived(String topic,
        					MqttMessage message)
        							throws Exception {
        				System.out.println(message.toString());
        			}
        	        	
        			public void deliveryComplete(IMqttDeliveryToken token) {
        			}
        		});

        		client.subscribe("sid_lab_2018");
        	        
        	        
        	    }catch(Exception x) {
        	        x.printStackTrace();
        	    }
        }
    }