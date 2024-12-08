import java.util.List;

public class LogEntry {
    private List<String> user; // Liste contenant userId, name, age sous forme de chaîne de caractères
    private List<String> action; // Liste contenant les actions comme "read", "write", "search"

    public LogEntry(List<String> user, List<String> action) {
        this.user = user;
        this.action = action;
    }

    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
    }

    public List<String> getAction() {
        return action;
    }

    public void setAction(List<String> action) {
        this.action = action;
    }
}
