import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    private static final UserRepository userRepository = new UserRepository();
    private static final ProductRepository productRepository = new ProductRepository();
    private static final Random random = new Random();

    // Liste de produits réels avec leurs prix
    private static final List<ProductData> realProducts = List.of(
            new ProductData("Apple iPhone 15", 799),
            new ProductData("Samsung Galaxy S23", 999),
            new ProductData("Sony WH-1000XM5 Headphones", 349),
            new ProductData("MacBook Pro 14-inch", 1999),
            new ProductData("Dell XPS 13", 1199),
            new ProductData("Nike Air Max 270", 150),
            new ProductData("Adidas Ultraboost 22", 180),
            new ProductData("Canon EOS R5", 3899),
            new ProductData("LG OLED TV", 1299),
            new ProductData("GoPro HERO11 Black", 499)
    );

    // Liste de données utilisateurs prédéfinies
    private static final List<UserData> predefinedUsers = List.of(
            new UserData("John Doe", 28, "john.doe@example.com", "password123"),
            new UserData("Jane Smith", 35, "jane.smith@example.com", "securePass1"),
            new UserData("Alice Brown", 22, "alice.brown@example.com", "12345abc"),
            new UserData("Bob Johnson", 40, "bob.johnson@example.com", "bobSecure@123"),
            new UserData("Charlie Davis", 30, "charlie.davis@example.com", "charliePass321"),
            new UserData("Eva Williams", 27, "eva.williams@example.com", "evaPass1234"),
            new UserData("Tom Clark", 33, "tom.clark@example.com", "tommyPassword"),
            new UserData("Lucy Walker", 25, "lucy.walker@example.com", "lucyWsecure9"),
            new UserData("Oliver Scott", 38, "oliver.scott@example.com", "oliver@2024"),
            new UserData("Sophia Lee", 32, "sophia.lee@example.com", "sophiaPass456")
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenue!");

        // *** Partie de simulation commentée ***
        /*
        // Créer les utilisateurs à partir des données prédéfinies
        for (int i = 0; i < predefinedUsers.size(); i++) {
            UserData userData = predefinedUsers.get(i);
            User user = new User(i + 1, userData.getName(), userData.getAge(), userData.getEmail(), userData.getPassword());
            userRepository.addUser(user);
        }

        // Simulation des opérations pour chaque utilisateur
        for (User user : userRepository.getAllUsers()) {
            System.out.println("Simulation des opérations pour " + user.getName());
            for (int i = 0; i < 20; i++) {
                simulateRandomOperation(user);
            }
        }
        */

        // *** Partie de traitement des logs ***
        try {
            ProfileService profileService = new ProfileService();
            profileService.processLogs("logs/application.log");

            // Sauvegarde des profils par type d'opération
            profileService.saveProfilesByOperation("profiles");
        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde des profils : " + e.getMessage());
        }

        scanner.close();
    }

    // Classes utilitaires
    static class ProductData {
        String name;
        double price;

        public ProductData(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    static class UserData {
        private String name;
        private int age;
        private String email;
        private String password;

        public UserData(String name, int age, String email, String password) {
            this.name = name;
            this.age = age;
            this.email = email;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
