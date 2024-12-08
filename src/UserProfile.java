import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UserProfile {
    private String userId;
    private String name;
    private int age;
    private int readCount;
    private int writeCount;
    private int searchCount;

    public UserProfile(String userId, String name, int age) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.readCount = 0;
        this.writeCount = 0;
        this.searchCount = 0;
    }

    public void incrementReadCount() {
        readCount++;
    }

    public void incrementWriteCount() {
        writeCount++;
    }

    public void incrementSearchCount() {
        searchCount++;
    }

    public void writeToJsonFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filePath), this);
            System.out.println("User profile written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing user profile to file: " + e.getMessage());
        }
    }

    // Getters and setters 
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(int writeCount) {
        this.writeCount = writeCount;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", readCount=" + readCount +
                ", writeCount=" + writeCount +
                ", searchCount=" + searchCount +
                '}';
    }
    
    public String toJsonWithSpecificAttribute(String attribute) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("userId", this.userId);
        jsonMap.put("name", this.name);
        jsonMap.put("age", this.age);

        // Ajoute uniquement l'attribut demandé
        switch (attribute.toLowerCase()) {
            case "read":
                jsonMap.put("readCount", this.readCount);
                break;
            case "write":
                jsonMap.put("writeCount", this.writeCount);
                break;
            case "search":
                jsonMap.put("searchCount", this.searchCount);
                break;
            default:
                throw new IllegalArgumentException("Attribut inconnu : " + attribute);
        }

        return mapper.writeValueAsString(jsonMap);
    }

}
