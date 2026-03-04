package models;

import java.sql.Timestamp;
import java.util.UUID;

public class Chore {

    private String id;
    private String title;
    private String description;
    private String createdBy;
    private String acceptedBy;
    private String status;      // OPEN, ACCEPTED, DONE
    private boolean isPublic;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private double latitude;
    private double longitude;

    
    
    
    
    
    
    
    // Constructor
    public Chore(String title, String description, String createdBy, boolean isPublic) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.isPublic = isPublic;
        this.status = "OPEN";
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }

    // Empty constructor incase
    public Chore() {}

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    
    
    public double getLatitude() {
        return latitude;
    }
 
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
 
    public double getLongitude() {
        return longitude;
    }
 
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPublic() {
        return isPublic;
    }
 
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
 
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
 
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}