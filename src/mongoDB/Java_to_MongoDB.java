package mongoDB;

import java.io.IOException;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import extra.XMLReader;


public class Java_to_MongoDB extends Thread {

	private static XMLReader xmlreader = new XMLReader();
	private String message;
	
	public Java_to_MongoDB(String message){
		this.message=message;
	}
	
	@Override
	public void run(){
		
		xmlreader.readXML2();
		
		try {
			
		//Liga à base de dados MongoDB, e obtem a collection
			@SuppressWarnings("resource")
			MongoCredential credential = MongoCredential.createCredential(xmlreader.getMongo_user(), xmlreader.getMongo_database(), 
																		  xmlreader.getMongo_password().toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(xmlreader.getMongo_ip_server() , Integer.parseInt(xmlreader.getMongo_port())), Arrays.asList(credential));
			MongoDatabase db = mongoClient.getDatabase(xmlreader.getMongo_database());
			MongoCollection<Document> col = db.getCollection("humidadeTemperatura");
			MongoCollection<Document> col_backup = db.getCollection("humidadeTemperatura_backup");
		//Certificar que as aspas são as corretas
			String mensagem = message.toString();
			mensagem = mensagem.replace('“', '"');
			mensagem = mensagem.replace('”', '"');
			mensagem = mensagem.replaceAll(" ", "");
			Document doc = Document.parse(mensagem);
		
			col.insertOne(doc);
			col_backup.insertOne(doc);

			System.out.println("Imported");
		}catch( Exception e ) {
			System.out.println("Not imported");
		}
		this.interrupt();
	}
}