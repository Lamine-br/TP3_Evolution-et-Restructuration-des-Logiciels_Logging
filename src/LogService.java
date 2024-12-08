import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(LogService.class);

    public void processLog(String userId, String operationType, String userName, int userAge) {
        // Ajouter des informations dans MDC
        MDC.put("userId", userId);
        MDC.put("operationType", operationType);

        List<String> user = new ArrayList<>();
        user.add("ID: " + userId);
        user.add("Name: " + userName);
        user.add("Age: " + userAge);

        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);

        List<String> action = new ArrayList<>();
        action.add(operationType); // Type d'opération : "read", "write", "search"

        logger.info("Timestamp: {}, User Info: {}, Action: {}", formattedTimestamp, user, action);

        // Retirer les informations du MDC après l'utilisation
        MDC.clear();
    }
}
