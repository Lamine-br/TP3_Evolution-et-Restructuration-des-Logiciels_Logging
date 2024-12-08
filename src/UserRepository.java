import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final MongoCollection<Document> userCollection;

    public UserRepository() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.userCollection = database.getCollection("users");
        logger.info("UserRepository initialized with collection: users");
    }

    public void addUser(User user) {
        logger.info("Attempting to add user with ID: {}", user.getId());
        if (userExists(user.getId())) {
            logger.error("User with ID {} already exists.", user.getId());
            throw new IllegalArgumentException("User with ID " + user.getId() + " already exists.");
        }
        Document userDoc = new Document()
                .append("id", user.getId())
                .append("name", user.getName())
                .append("age", user.getAge())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        userCollection.insertOne(userDoc);
        logger.info("User with ID {} added successfully.", user.getId());
    }

    public User getUserById(int id) {
        logger.info("Fetching user with ID: {}", id);
        Document userDoc = userCollection.find(eq("id", id)).first();
        if (userDoc == null) {
            logger.warn("No user with ID {} found.", id);
            throw new IllegalArgumentException("No user with ID " + id + " found.");
        }
        logger.info("User with ID {} fetched successfully.", id);
        return new User(
                userDoc.getInteger("id"),
                userDoc.getString("name"),
                userDoc.getInteger("age"),
                userDoc.getString("email"),
                userDoc.getString("password")
        );
    }

    private boolean userExists(int id) {
        boolean exists = userCollection.find(eq("id", id)).first() != null;
        logger.debug("User existence check for ID {}: {}", id, exists);
        return exists;
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = new ArrayList<>();
        for (Document userDoc : userCollection.find()) {
            users.add(new User(
                    userDoc.getInteger("id"),
                    userDoc.getString("name"),
                    userDoc.getInteger("age"),
                    userDoc.getString("email"),
                    userDoc.getString("password")
            ));
        }
        logger.info("Fetched {} users.", users.size());
        return users;
    }
}
