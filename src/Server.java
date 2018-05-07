//C:/Users/bruno/Desktop/jackson-all-1.9.0.jar;
//C:/Users/bruno/Desktop/org.eclipse.paho.client.mqttv3-1.1.0.jar;



//Importações para Servidor
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

//Importações para JsonParser
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

//Importações para Sybase
import java.sql.*;
//import sybase.jdbc4.sqlanywhere.*;

//Importações Java
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class Server{

	
	public void start(){

		File ficheiro = new File("C:/Users/bruno/Desktop/HumidadeTemperatura.txt");
		
		Worker w = new Worker(ficheiro, "dados");
		w.run();
		w.interrupt();
		
		
		/*	BufferedReader in = new BufferedReader(in);
		
		while (true){
			String dados = in.readLine();
			if (dados != null ) {
				System.out.println("" + dados);
				
		       }
		} 
		*/
		


	}
	
}
