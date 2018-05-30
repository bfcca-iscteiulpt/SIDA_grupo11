package sybase;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import extra.XMLReader;

public class MongoDB_to_Java_to_Sybase extends Thread {
	
	private static XMLReader xmlreader = new XMLReader();;
	private static ArrayList<Document> list = new ArrayList<Document>();
	
	private static double hum_var1 = -1000;
	private static double hum_var2 = -1000;
	
	@Override
	public void run(){	
		
		xmlreader.readXML1();
			
		//Liga à base de dados MongoDB, e obtem a collection
		@SuppressWarnings("resource")
//		MongoCredential credential = MongoCredential.createCredential("MigrationAgent", "db_grupo11", "mongotosybase".toCharArray());
//		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential));
//		MongoDatabase db = mongoClient.getDatabase("db_grupo11");
		
		MongoCredential credential = MongoCredential.createCredential(xmlreader.getMongo_user(), xmlreader.getMongo_database(), 
				  													  xmlreader.getMongo_password().toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(xmlreader.getMongo_ip_server() , Integer.parseInt(xmlreader.getMongo_port())), Arrays.asList(credential));
		MongoDatabase db = mongoClient.getDatabase(xmlreader.getMongo_database());
		
		MongoCollection<Document> col = db.getCollection("humidadeTemperatura");
		System.out.println("Connected to MongoDB");

		while(true) {
			
		
		
			for(Document doc : col.find()) {
				list.add(doc);
			}
		
			if(!list.isEmpty()) {
				
				//Cria a ligação com a bd do Sybase	
				try{
					Connection conn = DriverManager.getConnection (xmlreader.getSybase_ip_server() + xmlreader.getSybase_port() + "?eng=" + 
														   xmlreader.getSybase_database(), xmlreader.getSybase_user(),xmlreader.getSybase_password());
					conn.setAutoCommit(true);
					System.out.println("Connected to Sybase");
		
					Statement stat = conn.createStatement();
					for(Document doc : list) {
						if( getHumidity(doc.toJson()) != null) {
							stat.executeUpdate("INSERT INTO DBA.HumidadeTemperatura (DataMedicao, HoraMedicao, ValorMedicaoHumidade,ValorMedicaoTemperatura) " 
									+ "VALUES ("+ "convert(TIMESTAMP,'" + getDate(doc.toJson()) + "') , "
//																			+ getDate(doc.toJson()) + ", " 
												+ "convert(TIME,'" + getTime(doc.toJson()) + "') , "
//																			+ getTime(doc.toJson()) + ", "
																   + getHumidity(doc.toJson()) + ", "
																   + getTemperature(doc.toJson())
																   + ")");
						}
	
					}

//					System.out.println("All data was imported correctly");
					col.deleteMany(new Document());
					list.clear();
					System.out.println("All data from MongoDB was eliminated correctly");
				}catch(Exception e){
					System.out.println(e);
				}
			}
				
			try {
				Thread.sleep(Integer.parseInt(xmlreader.getPeriodicity()) * 1000);
			} catch (InterruptedException e) {}
			
		}
		
	}
	
	private static Double getHumidity(String dados){
		//Obter apenas os dados do tempo
		String[] total_humidity = dados.split(",");
		String[] humidity_1 = total_humidity[2].split(":");
		String humidity = humidity_1[1].replace('"', ' ');
		humidity = humidity.replaceAll(" ", "");
		
		Double humidade = Double.parseDouble(humidity);
//		System.out.println(humidade);
		
		if(isValidHumidity(humidade)){
//			System.out.println("Humidity is valid");
			return humidade;			
		}
//		System.out.println("Humidity is NOT valid");
		return null;
	}

	
	private static Double getTemperature(String dados){
		//Obter apenas os dados do tempo
		String[] total_temperature = dados.split(",");
		String[] temperature_1 = total_temperature[1].split(":");
		String temperature = temperature_1[1].replace('"', ' ');
		temperature = temperature.replaceAll(" ", "");
//		System.out.println(temperature);
		
		Double temperatura = Double.parseDouble(temperature);
		
//		if(isHumidityTime(time)){
//			System.out.println("Humidity is valid");
//			return time;			
//		}
//		System.out.println("Humidity is NOT valid");
		return temperatura;
	}
	
	private static String getTime(String dados){
		//Obter apenas os dados do tempo
		String[] total_time = dados.split(",");
		String[] time_1 = total_time[4].split(":",2);
		String time = time_1[1].replace('"', ' ');
		time = time.replaceAll(" ", "");
		time = time.substring(0, time.length()-1);

		if(isValidTime(time)){
//			System.out.println("Time is valid");
			return time;			
		} else {
//			System.out.println("Time is NOT valid");
			return null;
		}

	}
	
	private static boolean isValidTime(String value){
		Date date = null;
		String format = "HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format); //para comparação de formatos
		
		LocalTime current_time = LocalTime.now(); //cria a hora atual
		LocalTime time = LocalTime.parse(value); //dá a hora do value
		try {
            date = sdf.parse(value);
            if (value.equals(sdf.format(date)) && current_time.isAfter(time)) {
                return true;
            }
        } catch (ParseException ex) {}
		return false;
	}
	
	private static String getDate(String dados){
		//Obter apenas os dados da data
		String[] total_data = dados.split(",");
		String[] data_1 = total_data[3].split(":");
		String data = data_1[1].replace('"', ' ');
		data = data.replaceAll(" ", "");
		String [] lol = data.split("/");
		data = lol[2] + "-" + lol[1] + "-" + lol[0];

		if(isValidDate(data)){
//			System.out.println("Date is valid");
			return data;			
		}else {
//			System.out.println("Date is NOT valid");
			return null;
		}

	}
	
	private static boolean isValidDate(String value){
		Date date = null;
		Date current_date = new Date(); //dá a data atual
		String format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
	
        try {
        	date = sdf.parse(value);
            if (value.equals(sdf.format(date)) && current_date.after(date)) { //verifica se está no formato certo e se é anterior ou igual a hoje
                return true;
            }
        } catch (ParseException ex) {}
		return false;
	}

/* NÃO USO

	private static void import_to_Sybase(){
		//Cria a ligação com a bd do Sybase		
		try{
			Connection conn = DriverManager.getConnection (xmlreader.getSybase_ip_server() + xmlreader.getSybase_port() + "?eng=" + 
														   xmlreader.getSybase_database(), xmlreader.getSybase_user(),xmlreader.getSybase_password());
			conn.setAutoCommit(true);
			System.out.println("Connected to Sybase");
		
			Statement stat = conn.createStatement();
			for(Document doc : list) {
		    
				stat.executeUpdate("INSERT INTO DBA.HumidadeTemperatura2 (DataMedicao, HoraMedicao, ValorMedicaoHumidade,ValorMedicaoTemperatura) " 
																+ "VALUES ("+ "convert(TIMESTAMP,'" + getDate(doc.toJson()) + "') , "
//																			+ getDate(doc.toJson()) + ", " 
																			+ "convert(TIME,'" + getTime(doc.toJson()) + "') , "
//																			+ getTime(doc.toJson()) + ", "
																			+ getHumidity(doc.toJson()) + ", "
																			+ getTemperature(doc.toJson())
																			+ ")");
			}
			System.out.println("All data was imported correctly");
			cleanMongoDB();
		}catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	private static void cleanMongoDB(){
		@SuppressWarnings("resource")
		MongoCredential credential = MongoCredential.createCredential(xmlreader.getMongo_user(), xmlreader.getMongo_database(), 
																	  xmlreader.getMongo_password().toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(xmlreader.getMongo_ip_server() , Integer.parseInt(xmlreader.getMongo_port())), Arrays.asList(credential));
		MongoDatabase db = mongoClient.getDatabase(xmlreader.getMongo_database());
		MongoCollection<Document> col = db.getCollection("humidadeTemperatura");
		
		col.deleteMany(new Document());
		System.out.println("All data from MongoDB was eliminated correctly");
	}
		
 */
	
	private static boolean isValidHumidity(double nova_humidade) {
//		System.out.println("-----NOVOS DADOS-----");
//		System.out.println("hum_var1 = " + hum_var1);
//		System.out.println("hum_var2 = " + hum_var2);
//		System.out.println("nova_humidade = " + nova_humidade);
		if(hum_var1 == -1000 && hum_var2 == -1000) {
//			System.out.println("valid 1");
			hum_var1 = nova_humidade;
			return true;
		}else if(hum_var1 != -1000 && hum_var2 == -1000) {
			if(((nova_humidade - hum_var1 <= xmlreader.getDiference()) && (nova_humidade - hum_var1 >= 0)) || 
				((hum_var1 - nova_humidade <= -xmlreader.getDiference()) && (hum_var1 - nova_humidade >= 0))) {
//				System.out.println("valid 2");
				hum_var2 = hum_var1;
				hum_var1 = nova_humidade;
				return true;
			}else {
//				System.out.println("not valid 2");
				hum_var2 = hum_var1;
				hum_var1 = nova_humidade;
				return false;
			}
		}else{
			if(((nova_humidade - hum_var1 <= xmlreader.getDiference()) && (nova_humidade - hum_var1 >= 0)) || 
					((hum_var1 - nova_humidade <= -xmlreader.getDiference()) && (hum_var1 - nova_humidade >= 0))) {
//				System.out.println("valid 3.1");
				hum_var2 = hum_var1;
				hum_var1 = nova_humidade;
				return true;
			}else if(((nova_humidade - hum_var2 <= xmlreader.getDiference()) && (nova_humidade - hum_var2 >= 0)) || 
					((hum_var2 - nova_humidade <= -xmlreader.getDiference()) && (hum_var2 - nova_humidade >= 0))) {
//				System.out.println("valid 3.2");
				hum_var2 = hum_var1;
				hum_var1 = nova_humidade;
				return true;
			}else {
//				System.out.println("not valid 3");
				hum_var2 = hum_var1;
				hum_var1 = nova_humidade;
				return false;
			}
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		new MongoDB_to_Java_to_Sybase().run();
	}

}
