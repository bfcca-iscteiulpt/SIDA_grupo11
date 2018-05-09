package Sybase;

import java.sql.*;
import sybase.jdbc4.sqlanywhere.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Sybase_Connector {
	
	private static List list = new ArrayList<DBObject>();
	
	private static void export_to_Java(){	
		
		//Liga à base de dados MongoDB, exporta os dados para a "list"
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient();
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB("db_grupo11");
		DBCollection collection = db.getCollection("humidadeTemperatura");
		
		System.out.println("Connected to MongoDB");
		
		for(DBObject dbo: collection.find())
			list.add(dbo);
		
		getHumidity(list.get(4).toString());
	}
	
	private static String getHumidity(String dados){
		//Obter apenas os dados do tempo
		String[] total_humidity = dados.split(",");
		String[] humidity_1 = total_humidity[2].split(":");
		String humidity = humidity_1[1].replace('"', ' ');
		humidity = humidity.replaceAll(" ", "");
		System.out.println(humidity);
		if(isValidHumidity(humidity).equals(humidity)){
			System.out.println("Humidity is valid");
			return humidity;			
		}
		System.out.println("Humidity is NOT valid");
		return null;
	}
	
	//NÂO FUNCIONA
	private static String isValidHumidity(String value){
//		value  = value.
		for (char c : value.toCharArray()){
			if(!Character.isDigit(c))
				return value = null;
			
		}
		return value;
	}
	
	private static String getTemperature(String dados){
		//Obter apenas os dados do tempo
		String[] total_temperature = dados.split(",");
		String[] temperature_1 = total_temperature[1].split(":");
		String temperature = temperature_1[1].replace('"', ' ');
		temperature = temperature.replaceAll(" ", "");
		System.out.println(temperature);
//		if(isHumidityTime(time)){
//			System.out.println("Humidity is valid");
//			return time;			
//		}
//		System.out.println("Humidity is NOT valid");
		return null;
	}
	
	private static String getTime(String dados){
		//Obter apenas os dados do tempo
		String[] total_time = dados.split(",");
		String[] time_1 = total_time[4].split(":",2);
		String time = time_1[1].replace('"', ' ');
		time = time.replaceAll(" ", "");
		time = time.substring(0, time.length()-1);
		System.out.println(time);
		if(isValidTime(time)){
			System.out.println("Time is valid");
			return time;			
		}
		System.out.println("Time is NOT valid");
		return null;
	}
	
	private static boolean isValidTime(String value){
		Date date = null;
		String format = "hh:mm:ss";
		
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {}
		return date != null;
	}
	
	private static String getDate(String dados){
		//Obter apenas os dados da data
		String[] total_data = dados.split(",");
		String[] data_1 = total_data[3].split(":");
		String data = data_1[1].replace('"', ' ');
		data = data.replaceAll(" ", "");
		System.out.println(data);
		if(isValidDate(data)){
			System.out.println("Date is valid");
			return data;			
		}
		System.out.println("Date is NOT valid");
		return null;
	}
	
	private static boolean isValidDate(String value){
		Date date = null;
		String format = "dd/MM/yyyy";
		
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {}
		return date != null;
	}
	
	private static void import_to_Sybase(){
		//Cria a ligação com a bd do Sybase
		try{
			Connection conn = DriverManager.getConnection (" jdbc:sqlanywhere:Tds:localhost:2638?eng=livro","dba", "sql");
			conn.setAutoCommit(false);
			System.out.println("Connected to Sybase");
			
		}catch(Exception e){
			System.out.println("NOT Connected to Sybase");
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		import_to_Sybase();

	}

}
