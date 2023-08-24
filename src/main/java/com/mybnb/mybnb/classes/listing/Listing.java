package com.mybnb.mybnb.classes.listing;

import com.mybnb.mybnb.interfaces.TableFormat;
import javafx.scene.control.Tab;

import java.util.List;

public class Listing implements TableFormat {
    private int id;
    private String title;
    private int hostId;
    private int basePrice;
    private PropertyType propertyType;
    private double longitude;
    private double latitude;
    private String addressLineOne;
    private String addressLineTwo;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private boolean wifi;
    private boolean kitchen;
    private boolean washer;
    private boolean dryer;
    private boolean airConditioning;
    private boolean heating;
    private boolean dedicatedWorkspace;
    private boolean hairDryer;
    private boolean tv;
    private boolean iron;
    private boolean pool;
    private boolean freeParking;
    private boolean crib;
    private boolean bbqGrill;
    private boolean indoorFireplace;
    private boolean hotTub;
    private boolean evCharger;
    private boolean gym;
    private boolean breakfast;
    private boolean smokingAllowed;
    private boolean beachfront;
    private boolean waterfront;
    private boolean smokeAlarm;
    private boolean carbonMonoxideAlarm;


    public Listing(String title, int hostId, int basePrice, PropertyType propertyType,
                   double longitude, double latitude, String addressLineOne, String addressLineTwo,
                   String city, String state, String country, String postalCode,
                   boolean wifi, boolean kitchen, boolean washer, boolean dryer,
                   boolean airConditioning, boolean heating, boolean dedicatedWorkspace,
                   boolean hairDryer, boolean tv, boolean iron, boolean pool,
                   boolean freeParking, boolean crib, boolean bbqGrill,
                   boolean indoorFireplace, boolean hotTub, boolean evCharger,
                   boolean gym, boolean breakfast, boolean smokingAllowed,
                   boolean beachfront, boolean waterfront, boolean smokeAlarm,
                   boolean carbonMonoxideAlarm) {
        this.title = title;
        this.hostId = hostId;
        this.basePrice = basePrice;
        this.propertyType = propertyType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.wifi = wifi;
        this.kitchen = kitchen;
        this.washer = washer;
        this.dryer = dryer;
        this.airConditioning = airConditioning;
        this.heating = heating;
        this.dedicatedWorkspace = dedicatedWorkspace;
        this.hairDryer = hairDryer;
        this.tv = tv;
        this.iron = iron;
        this.pool = pool;
        this.freeParking = freeParking;
        this.crib = crib;
        this.bbqGrill = bbqGrill;
        this.indoorFireplace = indoorFireplace;
        this.hotTub = hotTub;
        this.evCharger = evCharger;
        this.gym = gym;
        this.breakfast = breakfast;
        this.smokingAllowed = smokingAllowed;
        this.beachfront = beachfront;
        this.waterfront = waterfront;
        this.smokeAlarm = smokeAlarm;
        this.carbonMonoxideAlarm = carbonMonoxideAlarm;
    };

    public Listing() {};

    public Listing(int id, String title, int hostId, int basePrice, PropertyType propertyType,
                   double longitude, double latitude, String addressLineOne, String addressLineTwo,
                   String city, String state, String country, String postalCode,
                   boolean wifi, boolean kitchen, boolean washer, boolean dryer,
                   boolean airConditioning, boolean heating, boolean dedicatedWorkspace,
                   boolean hairDryer, boolean tv, boolean iron, boolean pool,
                   boolean freeParking, boolean crib, boolean bbqGrill,
                   boolean indoorFireplace, boolean hotTub, boolean evCharger,
                   boolean gym, boolean breakfast, boolean smokingAllowed,
                   boolean beachfront, boolean waterfront, boolean smokeAlarm,
                   boolean carbonMonoxideAlarm) {
        this.id = id;
        this.title = title;
        this.hostId = hostId;
        this.basePrice = basePrice;
        this.propertyType = propertyType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.wifi = wifi;
        this.kitchen = kitchen;
        this.washer = washer;
        this.dryer = dryer;
        this.airConditioning = airConditioning;
        this.heating = heating;
        this.dedicatedWorkspace = dedicatedWorkspace;
        this.hairDryer = hairDryer;
        this.tv = tv;
        this.iron = iron;
        this.pool = pool;
        this.freeParking = freeParking;
        this.crib = crib;
        this.bbqGrill = bbqGrill;
        this.indoorFireplace = indoorFireplace;
        this.hotTub = hotTub;
        this.evCharger = evCharger;
        this.gym = gym;
        this.breakfast = breakfast;
        this.smokingAllowed = smokingAllowed;
        this.beachfront = beachfront;
        this.waterfront = waterfront;
        this.smokeAlarm = smokeAlarm;
        this.carbonMonoxideAlarm = carbonMonoxideAlarm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public boolean isWasher() {
        return washer;
    }

    public void setWasher(boolean washer) {
        this.washer = washer;
    }

    public boolean isDryer() {
        return dryer;
    }

    public void setDryer(boolean dryer) {
        this.dryer = dryer;
    }

    public boolean isAirConditioning() {
        return airConditioning;
    }

    public void setAirConditioning(boolean airConditioning) {
        this.airConditioning = airConditioning;
    }

    public boolean isHeating() {
        return heating;
    }

    public void setHeating(boolean heating) {
        this.heating = heating;
    }

    public boolean isDedicatedWorkspace() {
        return dedicatedWorkspace;
    }

    public void setDedicatedWorkspace(boolean dedicatedWorkspace) {
        this.dedicatedWorkspace = dedicatedWorkspace;
    }

    public boolean isHairDryer() {
        return hairDryer;
    }

    public void setHairDryer(boolean hairDryer) {
        this.hairDryer = hairDryer;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isIron() {
        return iron;
    }

    public void setIron(boolean iron) {
        this.iron = iron;
    }

    public boolean isPool() {
        return pool;
    }

    public void setPool(boolean pool) {
        this.pool = pool;
    }

    public boolean isFreeParking() {
        return freeParking;
    }

    public void setFreeParking(boolean freeParking) {
        this.freeParking = freeParking;
    }

    public boolean isCrib() {
        return crib;
    }

    public void setCrib(boolean crib) {
        this.crib = crib;
    }

    public boolean isBbqGrill() {
        return bbqGrill;
    }

    public void setBbqGrill(boolean bbqGrill) {
        this.bbqGrill = bbqGrill;
    }

    public boolean isIndoorFireplace() {
        return indoorFireplace;
    }

    public void setIndoorFireplace(boolean indoorFireplace) {
        this.indoorFireplace = indoorFireplace;
    }

    public boolean isHotTub() {
        return hotTub;
    }

    public void setHotTub(boolean hotTub) {
        this.hotTub = hotTub;
    }

    public boolean isEvCharger() {
        return evCharger;
    }

    public void setEvCharger(boolean evCharger) {
        this.evCharger = evCharger;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean isBeachfront() {
        return beachfront;
    }

    public void setBeachfront(boolean beachfront) {
        this.beachfront = beachfront;
    }

    public boolean isWaterfront() {
        return waterfront;
    }

    public void setWaterfront(boolean waterfront) {
        this.waterfront = waterfront;
    }

    public boolean isSmokeAlarm() {
        return smokeAlarm;
    }

    public void setSmokeAlarm(boolean smokeAlarm) {
        this.smokeAlarm = smokeAlarm;
    }

    public boolean isCarbonMonoxideAlarm() {
        return carbonMonoxideAlarm;
    }

    public void setCarbonMonoxideAlarm(boolean carbonMonoxideAlarm) {
        this.carbonMonoxideAlarm = carbonMonoxideAlarm;
    }

    public String[] getHeaders(){
        String[] headers = {
                "ID", "Title", "Host ID", "Base Price", "Property Type", "Longitude",
                "Latitude", "Address Line 1", "Address Line 2", "Postal Code", "City", "State", "Country",
                "wifi", "kitchen", "washer", "dryer", "airConditioning", "heating",
                "dedicatedWorkspace", "hairDryer", "tv", "iron", "pool", "freeParking",
                "crib", "bbqGrill", "indoorFireplace", "hotTub", "evCharger", "gym",
                "breakfast", "smokingAllowed", "beachfront", "waterfront", "smokeAlarm",
                "carbonMonoxideAlarm"
        };

        return headers;
    }

    public String[] getRowValues(){

        String[] data = {
                this.getId() != 0 ? Integer.toString(this.getId()) : "N/A",
                this.getTitle(),
                this.getHostId() != 0 ? Integer.toString(this.getHostId()) : "N/A",
                this.getBasePrice() != 0 ? Integer.toString(this.getBasePrice()) : "N/A",
                this.getPropertyType() != null ? this.getPropertyType().toString() : "N/A",
                this.getLongitude() != 0 ? Double.toString(this.getLongitude()) : "N/A",
                this.getLatitude() != 0 ? Double.toString(this.getLatitude()) : "N/A",
                this.getAddressLineOne(),
                this.getAddressLineTwo() != null ? this.getAddressLineTwo() : "N/A",
                this.getPostalCode() != null ? this.getPostalCode() : "N/A",
                this.getCity() != null ? this.getCity() : "N/A",
                this.getState() != null ? this.getState() : "N/A",
                this.getCountry() != null ? this.getCountry() : "N/A",
                this.isWifi() ? "Yes" : "No",
                this.isKitchen() ? "Yes" : "No",
                this.isWasher() ? "Yes" : "No",
                this.isDryer() ? "Yes" : "No",
                this.isAirConditioning() ? "Yes" : "No",
                this.isHeating() ? "Yes" : "No",
                this.isDedicatedWorkspace() ? "Yes" : "No",
                this.isHairDryer() ? "Yes" : "No",
                this.isTv() ? "Yes" : "No",
                this.isIron() ? "Yes" : "No",
                this.isPool() ? "Yes" : "No",
                this.isFreeParking() ? "Yes" : "No",
                this.isCrib() ? "Yes" : "No",
                this.isBbqGrill() ? "Yes" : "No",
                this.isIndoorFireplace() ? "Yes" : "No",
                this.isHotTub() ? "Yes" : "No",
                this.isEvCharger() ? "Yes" : "No",
                this.isGym() ? "Yes" : "No",
                this.isBreakfast() ? "Yes" : "No",
                this.isSmokingAllowed() ? "Yes" : "No",
                this.isBeachfront() ? "Yes" : "No",
                this.isWaterfront() ? "Yes" : "No",
                this.isSmokeAlarm() ? "Yes" : "No",
                this.isCarbonMonoxideAlarm() ? "Yes" : "No"
        };

        return data;
    }
}
