import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfileService {
    private final Map<String, UserProfile> userProfiles = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Traite un fichier .log contenant les logs et met à jour les profils utilisateurs.
     *
     * @param filePath Le chemin du fichier .log contenant les logs.
     */
    public void processLogs(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Traiter uniquement les lignes commençant par LogService
                if (line.contains("LogService")) {
                    processLogLine(line);
                }
            }
            System.out.println("Logs processed successfully.");
        } catch (IOException e) {
            System.err.println("Error reading or processing logs from .log file: " + e.getMessage());
        }
    }

    /**
     * Traite une seule ligne de log et met à jour le profil utilisateur correspondant.
     *
     * @param logLine Une ligne de log au format texte.
     */
    private void processLogLine(String logLine) {
        try {
            // Vérifier si les champs nécessaires existent
            int userInfoStart = logLine.indexOf("User Info: [");
            int userInfoEnd = logLine.indexOf("], Action:");
            int actionStart = logLine.indexOf("Action: [");
            int actionEnd = logLine.indexOf("]", actionStart);

            if (userInfoStart == -1 || userInfoEnd == -1 || actionStart == -1 || actionEnd == -1) {
                System.err.println("Log line mal formatée, ignorée : " + logLine);
                return;
            }

            // Extraire les informations utilisateur et l'action
            String userInfo = logLine.substring(userInfoStart + 12, userInfoEnd).trim();
            String action = logLine.substring(actionStart + 9, actionEnd).trim();

            // Analyser les informations utilisateur
            String[] userParts = userInfo.split(", ");
            if (userParts.length < 3) {
                System.err.println("Informations utilisateur incomplètes dans la ligne de log : " + logLine);
                return;
            }

            String userId = userParts[0].split(": ")[1].trim();
            String userName = userParts[1].split(": ")[1].trim();
            int userAge;
            try {
                userAge = Integer.parseInt(userParts[2].split(": ")[1].trim());
            } catch (NumberFormatException e) {
                System.err.println("Âge utilisateur invalide dans la ligne de log : " + logLine);
                return;
            }

            // Mettre à jour ou créer un profil utilisateur
            UserProfile profile = userProfiles.computeIfAbsent(userId, id -> new UserProfile(userId, userName, userAge));

            // Incrémenter le bon compteur selon le type d'opération
            switch (action.toLowerCase()) {
                case "read":
                    profile.incrementReadCount();
                    break;
                case "write":
                    profile.incrementWriteCount();
                    break;
                case "search":
                    profile.incrementSearchCount();
                    break;
                default:
                    System.err.println("Opération inconnue ignorée : " + action);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement de la ligne de log : " + logLine + " - " + e.getMessage());
        }
    }


    /**
     * Sauvegarde les profils dans trois fichiers JSON selon le type d'action.
     *
     * @param baseFilePath Le chemin de base pour les fichiers JSON (exemple : "profiles").
     */
    public void saveProfilesByOperation(String baseFilePath) {
        try {
            // Filtrer et trier les profils pour chaque type d'action
            List<ProfileWithAction> readProfiles = userProfiles.values().stream()
                    .filter(profile -> profile.getReadCount() > 0) // Filtrer uniquement les profils ayant des lectures
                    .map(profile -> new ProfileWithAction(profile.getUserId(), profile.getName(), profile.getAge(), profile.getReadCount(), 0, 0)) // Inclure les infos utilisateurs et les counts
                    .sorted((p1, p2) -> Integer.compare(p2.getReadCount(), p1.getReadCount())) // Trier par readCount décroissant
                    .collect(Collectors.toList());

            List<ProfileWithAction> writeProfiles = userProfiles.values().stream()
                    .filter(profile -> profile.getWriteCount() > 0)
                    .map(profile -> new ProfileWithAction(profile.getUserId(), profile.getName(), profile.getAge(), 0, profile.getWriteCount(), 0)) // Inclure writeCount
                    .sorted((p1, p2) -> Integer.compare(p2.getWriteCount(), p1.getWriteCount())) // Trier par writeCount décroissant
                    .collect(Collectors.toList());

            List<ProfileWithAction> searchProfiles = userProfiles.values().stream()
                    .filter(profile -> profile.getSearchCount() > 0)
                    .map(profile -> new ProfileWithAction(profile.getUserId(), profile.getName(), profile.getAge(), 0, 0, profile.getSearchCount())) // Inclure searchCount
                    .sorted((p1, p2) -> Integer.compare(p2.getSearchCount(), p1.getSearchCount())) // Trier par searchCount décroissant
                    .collect(Collectors.toList());

            // Sauvegarder chaque groupe de profils dans des fichiers séparés
            objectMapper.writeValue(new File(baseFilePath + "_read.json"), readProfiles);
            objectMapper.writeValue(new File(baseFilePath + "_write.json"), writeProfiles);
            objectMapper.writeValue(new File(baseFilePath + "_search.json"), searchProfiles);

            System.out.println("User profiles saved successfully to separate files by operation type.");
        } catch (IOException e) {
            System.err.println("Error saving user profiles to JSON files: " + e.getMessage());
        }
    }

    /**
     * Classe pour représenter un profil avec les informations de l'utilisateur et les comptes d'actions.
     */
    private static class ProfileWithAction {
        private final String userId;
        private final String userName;
        private final int userAge;
        private final int readCount;
        private final int writeCount;
        private final int searchCount;

        public ProfileWithAction(String userId, String userName, int userAge, int readCount, int writeCount, int searchCount) {
            this.userId = userId;
            this.userName = userName;
            this.userAge = userAge;
            this.readCount = readCount;
            this.writeCount = writeCount;
            this.searchCount = searchCount;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public int getUserAge() {
            return userAge;
        }

        public int getReadCount() {
            return readCount;
        }

        public int getWriteCount() {
            return writeCount;
        }

        public int getSearchCount() {
            return searchCount;
        }
    }


    /**
     * Récupérer les profils des utilisateurs.
     *
     * @return Une carte des profils des utilisateurs.
     */
    public Map<String, UserProfile> getUserProfiles() {
        return userProfiles;
    }
}
