package models;

import java.time.Instant;
import java.util.UUID;

public class Log {

    private String id;
    private String title;
    private String content;
    private Instant timestamp;

    public Log(String title, String content) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.timestamp = Instant.now();
    }
    public Log() {}
    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    // Setters (needed for edit/update)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    // Setters (needed for edit/update)
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public void setId(String id) {
    	  this.id = id;
    }

}
