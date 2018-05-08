
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Java_to_MongoDB extends Thread {

	private String message;
	
	public Java_to_MongoDB(String message){
		this.message=message;
	}
	
	//Transforma a mensagem num Document através de um hash - - NÂO FUNCIONA
//	private static Document stringToDocument(String message){
//		Document doc = new Document();
//		Type type = new TypeToken<Map<String,String>>(){}.getType();
//		Map<String, Object> messageMap = new Gson().fromJson(message, type);
//		doc.putAll(messageMap);
//		return doc;
//	}
	
	
	@Override
	public void run(){
		
		//Liga à base de dados MongoDB, e obtem a collection
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient();
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB("db_grupo11");
		DBCollection collection = db.getCollection("humidadeTemperatura");
		
		//Divide a mensagem nos 4 componentes e cada componente na key e value para o append
		String mensagem = message.toString();
		mensagem = mensagem.replaceAll("}", "" );
		mensagem = mensagem.substring(1);
		mensagem = mensagem.replace('“', ' ');
		mensagem = mensagem.replace('”', ' ');
		mensagem = mensagem.replaceAll(" ", "");
		String[] dados = mensagem.toString().split(",");
		String[] dados1 = dados[0].split(":");
		String[] dados2 = dados[1].split(":");
		String[] dados3 = dados[2].split(":");
		String[] dados4 = dados[3].split(":");
		
		//Cria o BasicDBObject que vai ser inserido na base de dados
		BasicDBObject hammer = new BasicDBObject(dados1[0],dados1[1])
											.append(dados2[0], dados2[1])
											.append(dados3[0], dados3[1])
											.append(dados4[0], dados4[1]);
		collection.insert(hammer);
		
		//Cria o BasicDBObject que vai ser inserido na base de dados - NÂO FUNCIONA
//		BasicDBObject dbObject = new BasicDBObject(stringToDocument(message.toString()));
//		collection.insert(dbObject);

		System.out.println("Imported");
		
	}
}