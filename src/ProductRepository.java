import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
    private final MongoDatabase database;
    private final MongoCollection<Document> productCollection;
    private static final LogService logService = new LogService();

    public ProductRepository() {
        this.database = MongoDBConnection.getDatabase();
        this.productCollection = database.getCollection("products");
    }

    public void addProduct(Product product, User user) {
        // Loguer l'action d'ajout
        logService.processLog(String.valueOf(user.getId()), "write", user.getName(), user.getAge());

        logger.info("Attempting to add product with ID: {}", product.getId());

        if (productCollection.find(eq("id", product.getId())).first() != null) {
            logger.error("Product with ID {} already exists.", product.getId());
            throw new IllegalArgumentException("Product with ID " + product.getId() + " already exists.");
        }

        Document productDoc = new Document("id", product.getId())
                .append("name", product.getName())
                .append("price", product.getPrice())
                .append("expirationDate", product.getExpirationDate().toString());

        productCollection.insertOne(productDoc);
        logger.info("Product with ID {} added successfully.", product.getId());
    }

    public Product getProductById(int id, User user) {
        // Loguer l'action de recherche
        logService.processLog(String.valueOf(user.getId()), "search", user.getName(), user.getAge());

        logger.info("Fetching product with ID: {}", id);

        Document productDoc = productCollection.find(eq("id", id)).first();
        if (productDoc == null) {
            logger.warn("No product with ID {} found.", id);
            throw new IllegalArgumentException("No product with ID " + id + " found.");
        }

        logger.info("Product with ID {} fetched successfully.", id);

        return new Product(
                productDoc.getInteger("id"),
                productDoc.getString("name"),
                productDoc.getDouble("price"),
                LocalDate.parse(productDoc.getString("expirationDate"))
        );
    }

    public void deleteProduct(int id, User user) {
        // Loguer l'action de suppression
        logService.processLog(String.valueOf(user.getId()), "write", user.getName(), user.getAge());

        logger.info("Attempting to delete product with ID: {}", id);

        if (productCollection.find(eq("id", id)).first() == null) {
            logger.warn("No product with ID {} found.", id);
            throw new IllegalArgumentException("No product with ID " + id + " found.");
        }

        productCollection.deleteOne(eq("id", id));
        logger.info("Product with ID {} deleted successfully.", id);
    }

    public void updateProduct(Product product, User user) {
        // Loguer l'action de mise à jour
        logService.processLog(String.valueOf(user.getId()), "write", user.getName(), user.getAge());

        logger.info("Attempting to update product with ID: {}", product.getId());

        if (productCollection.find(eq("id", product.getId())).first() == null) {
            logger.warn("No product with ID {} found.", product.getId());
            throw new IllegalArgumentException("No product with ID " + product.getId() + " found.");
        }

        Document updatedProductDoc = new Document("name", product.getName())
                .append("price", product.getPrice())
                .append("expirationDate", product.getExpirationDate().toString());

        productCollection.updateOne(eq("id", product.getId()), new Document("$set", updatedProductDoc));
        logger.info("Product with ID {} updated successfully.", product.getId());
    }

    public List<Product> getAllProducts(User user) {
        // Loguer l'action de lecture
        logService.processLog(String.valueOf(user.getId()), "read", user.getName(), user.getAge());

        logger.info("Fetching all products.");

        List<Product> productList = new ArrayList<>();
        for (Document doc : productCollection.find()) {
            Product product = new Product(
                    doc.getInteger("id"),
                    doc.getString("name"),
                    doc.getDouble("price"),
                    LocalDate.parse(doc.getString("expirationDate"))
            );
            productList.add(product);
        }

        logger.info("{} products fetched successfully.", productList.size());
        return productList;
    }
}
