// MongoDBConnection.java
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String URI = "mongodb://localhost:27017"; // Update as needed
    private static MongoClient mongoClient = MongoClients.create(URI);
    private static MongoDatabase database = mongoClient.getDatabase("tp3");

    public static MongoDatabase getDatabase() {
        return database;
    }
}
