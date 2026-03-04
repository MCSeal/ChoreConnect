package models;

import java.sql.Timestamp;
import java.util.UUID;


public class Review {

    private String id;
    private String choreId;
    private String reviewerId;
    private String revieweeId;
    private Integer rating;
    private String comment;
    private Timestamp createdAt;

    // Empty constructor (for ResultSet mapping / frameworks)
    public Review() {}

    // Constructor for creating a new Review
    public Review(String id, String choreId, String reviewerId, String revieweeId, Integer rating, String comment) {
    	this.id = UUID.randomUUID().toString();
        this.choreId = choreId;
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChoreId() {
        return choreId;
    }

    public void setChoreId(String choreId) {
        this.choreId = choreId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getRevieweeId() {
        return revieweeId;
    }

    public void setRevieweeId(String revieweeId) {
        this.revieweeId = revieweeId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }
 
    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}