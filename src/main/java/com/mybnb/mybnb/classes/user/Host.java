package com.mybnb.mybnb.classes.user;

import java.util.Date;

public class Host extends User {
    private int sin;
    private Date dateOfBirth;
    private String addressLineOne;
    private String addressLineTwo;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String occupation;
    private String firstname;
    private String lastname;

    public Host(int id, String email, String password,
                int sin, String firstname, String lastname, Date dateOfBirth, String addressLineOne, String addressLineTwo,
                String city, String state, String country, String postalCode, String occupation) {
        super(id, email, password);
        this.sin = sin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.occupation = occupation;
    }

    // Getters and setters for Host-specific attributes
    public int getSin() {
        return sin;
    }

    public void setSin(int sin) {
        this.sin = sin;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }


    @Override
    public String toString() {
        return "Host{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", sin=" + sin +
                ", dateOfBirth=" + dateOfBirth +
                ", addressLineOne='" + addressLineOne + '\'' +
                ", addressLineTwo='" + addressLineTwo + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", occupation='" + occupation + '\'' +
                '}';
    }
}