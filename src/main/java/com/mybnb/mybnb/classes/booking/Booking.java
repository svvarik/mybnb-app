package com.mybnb.mybnb.classes.booking;

import com.mybnb.mybnb.interfaces.TableFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Booking implements TableFormat {
    private int id;
    private int renterId;
    private int listingId;
    private String status;
    private String cancelledBy;
    private LocalDateTime createdAt;
    private Date startDate;
    private Date endDate;
    private LocalDateTime lastUpdated;

    // Constructor
    public Booking() {
        // Default constructor
    }

    public Booking(int id, int renterId, int listingId, String status, String cancelledBy,
                   LocalDateTime createdAt, LocalDateTime startDate, LocalDateTime endDate,
                   LocalDateTime lastUpdated) {
        this.id = id;
        this.renterId = renterId;
        this.listingId = listingId;
        this.status = status;
        this.cancelledBy = cancelledBy;
        this.createdAt = createdAt;
        this.startDate = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        this.endDate = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        this.lastUpdated = lastUpdated;
    }

    public Booking(int renterId, int listingId, String status, String cancelledBy,
                   LocalDateTime createdAt, LocalDateTime startDate, LocalDateTime endDate,
                   LocalDateTime lastUpdated) {
        this.renterId = renterId;
        this.listingId = listingId;
        this.status = status;
        this.cancelledBy = cancelledBy;
        this.createdAt = createdAt;
        this.startDate = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        this.endDate = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRenterId() {
        return renterId;
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public String[] getHeaders() {
        String[] headers = {
                "ID", "RenterID", "ListingID", "Status", "Cancelled By", "Created At", "Start Date", "End Date",
                "Last Updated"
        };
        return headers;
    }

    public String[] getRowValues() {
        String[] values = {
                this.id != 0 ? Integer.toString(this.id) : "N/A",
                this.renterId != 0 ? Integer.toString(this.renterId) : "N/A",
                this.listingId != 0 ? Integer.toString(this.listingId) : "N/A",
                this.status != null ? this.status : "N/A",
                this.cancelledBy != null ? this.cancelledBy : "N/A",
                this.createdAt != null ? this.createdAt.toString() : "N/A",
                this.startDate != null ? this.startDate.toString() : "N/A",
                this.endDate != null ? this.endDate.toString() : "N/A",
                this.lastUpdated != null ? this.lastUpdated.toString() : "N/A"
        };
        return values;
    }

}
