import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Connect_MongoDB {

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
    	        	
    			public void messageArrived(String topic, MqttMessage message) throws Exception {
    				if(!message.toString().equals("teste")){
    					System.out.println(message.toString());
    				
    				File ficheiro = new File("C:/Users/bruno/Desktop/HumidadeTemperatura.json");
    				FileWriter fw = new FileWriter(ficheiro,true);
    				fw.append(message.toString() + "\r\n");
    				fw.close();
    					
    				Process p = Runtime.getRuntime().exec("mongoimport --db db_grupo11 -- collection humidadeTemperatura --drop --file C:/Users/bruno/Desktop/HumidadeTemperatura.json");
  /*  				
    				//Divide a mensagem nos 4 componentes e cada componente na key e value para o append
    				String[] dados = message.toString().split(",");
    				String[] dados1 = dados[0].split(":");
    				String[] dados2 = dados[1].split(":");
    				String[] dados3 = dados[2].split(":");
    				String[] dados4 = dados[3].split(":");
    				
    				
    				//Liga e envia dados para o Mongo
    				@SuppressWarnings("resource")
    				MongoClient mongoClient = new MongoClient();
    				@SuppressWarnings("deprecation")
    				DB db = mongoClient.getDB("db_grupo11");
    				DBCollection collection = db.getCollection("humidadeTemperatura");
    				
    				System.out.println("Connected to " + db.getName());
    				
    				DBObject humidadeTemperatura = new BasicDBObject(dados1[0],dados1[1])
    																.append(dados2[0], dados2[1])
    																.append(dados3[0], dados3[1])
    																.append(dados4[0], dados4[1]);
    				
    				collection.insert(humidadeTemperatura);
   */
    				}
    			}
    	        	
    			public void deliveryComplete(IMqttDeliveryToken token) {
    			}
    		});

    		client.subscribe("sid_lab_2018");
    	        
    		
    	    }catch(Exception x) {
    	        x.printStackTrace();
    	    }
		
    	
		
		
		
		
		
		
		
		
//		//Liga e envia dados para o Mongo
//		@SuppressWarnings("resource")
//		MongoClient mongoClient = new MongoClient();
//		@SuppressWarnings("deprecation")
//		DB db = mongoClient.getDB("db_grupo11");
//		DBCollection collection = db.getCollection("humidadeTemperatura");
//		
//		System.out.println("Connected to " + db.getName());
//		
//		DBObject humidadeTemperatura = new BasicDBObject("temperature","10")
//														.append("humidity", "11")
//														.append("date", "11/11/1111")
//														.append("time", "11:11:11");
//		
//		collection.insert(humidadeTemperatura);
		
	}

}
