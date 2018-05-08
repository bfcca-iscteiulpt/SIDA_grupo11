
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
	
	//Transforma a mensagem num Document através de um hash
	private static Document StringToDocument(String message){
		Type type = new TypeToken<HashMap<String,String>>(){}.getType();
		Map<String, Object> messageMap = new Gson().fromJson(message, type);
		Document doc = new Document(messageMap);
		return doc;
	}
	
	@Override
	public void run(){
		
		//Liga à base de dados MongoDB, e obtem a collection
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient();
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB("db_grupo11");
		DBCollection collection = db.getCollection("humidadeTemperatura");
		
		//Cria o BasicDBObject que vai ser inserido na base de dados
		BasicDBObject dbObject = new BasicDBObject(StringToDocument(message.toString()));
						
		collection.insert(dbObject);

		System.out.println("Imported");
		
	}
}