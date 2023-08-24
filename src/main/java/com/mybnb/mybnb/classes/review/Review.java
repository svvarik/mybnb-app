package com.mybnb.mybnb.classes.review;

import java.util.Date;

public class Review {
    private int id;
    private int bookingId;
    private int reviewerId;
    private int reviewedId; // The ID of the reviewed entity (host, renter, listing)
    private Date createdAt;
    private String comment;
    private int rating;

    public Review(int bookingId, int reviewerId, int reviewedId, String comment, int rating) {
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.createdAt = new Date(System.currentTimeMillis());
        this.comment = comment;
        this.rating = rating;
    }

    public Review(int id, int bookingId, int reviewerId, int reviewedId, String comment, int rating) {
        this.id = id;
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.createdAt = new Date(System.currentTimeMillis());
        this.comment = comment;
        this.rating = rating;
    }

    public Review(int id, int bookingId, int reviewerId, int reviewedId, String comment, int rating, Date created_at) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
        this.createdAt = created_at;
        this.comment = comment;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getReviewedId() {
        return reviewedId;
    }

    public void setReviewedId(int reviewedId) {
        this.reviewedId = reviewedId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}