package MongoDB;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Java_to_MongoDB extends Thread {

	private String message;
	
	public Java_to_MongoDB(String message){
		this.message=message;
	}
	
	@Override
	public void run(){
		
		//Liga à base de dados MongoDB, e obtem a collection
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase("db_grupo11");
		MongoCollection<Document> col = db.getCollection("humidadeTemperatura");
		
		//Certificar que as aspas são as corretas
		String mensagem = message.toString();
		mensagem = mensagem.replace('“', '"');
		mensagem = mensagem.replace('”', '"');
//		mensagem = mensagem.replaceAll(" ", "");
		Document doc = Document.parse(mensagem);
		
		col.insertOne(doc);

		System.out.println("Imported");
		this.interrupt();
	}
}