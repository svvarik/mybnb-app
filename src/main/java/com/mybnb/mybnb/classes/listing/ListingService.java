package com.mybnb.mybnb.classes.listing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ListingService {
    private Connection connection;

    private List<String> allPossibleAmenities = new ArrayList<>(Arrays.asList(
            "wifi", "kitchen", "washer", "dryer", "airConditioning", "heating",
            "dedicatedWorkspace", "hairDryer", "tv", "iron", "pool", "freeParking",
            "crib", "bbqGrill", "indoorFireplace", "hotTub", "evCharger", "gym",
            "breakfast", "smokingAllowed", "beachfront", "waterfront", "smokeAlarm",
            "carbonMonoxideAlarm"
    ));

    public ListingService(Connection connection) {
        this.connection = connection;
    }

    // Method to insert a new listing into the database
    public boolean insertListing(Listing listing) {
        String insertSQL = """
                INSERT INTO listing (
                    title, host_id, base_price, property_type,
                    longitude, latitude, address_line_one, address_line_two,
                    city, state, country, postal_code,
                    wifi, kitchen, washer, dryer, air_conditioning, heating,
                    dedicated_workspace, hair_dryer, tv, iron, pool,
                    free_parking, crib, bbq_grill, indoor_fireplace, hot_tub,
                    ev_charger, gym, breakfast, smoking_allowed, beachfront,
                    waterfront, smoke_alarm, carbon_monoxide_alarm
                ) VALUES (
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?)""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Set parameters based on the Listing object
            preparedStatement.setString(1, listing.getTitle());
            preparedStatement.setInt(2, listing.getHostId());
            preparedStatement.setInt(3, listing.getBasePrice());
            preparedStatement.setString(4, listing.getPropertyType().name());
            preparedStatement.setDouble(5, listing.getLongitude());
            preparedStatement.setDouble(6, listing.getLatitude());
            preparedStatement.setString(7, listing.getAddressLineOne());
            preparedStatement.setString(8, listing.getAddressLineTwo());
            preparedStatement.setString(9, listing.getCity());
            preparedStatement.setString(10, listing.getState());
            preparedStatement.setString(11, listing.getCountry());
            preparedStatement.setString(12, listing.getPostalCode());
            preparedStatement.setBoolean(13, listing.isWifi());
            preparedStatement.setBoolean(14, listing.isKitchen());
            preparedStatement.setBoolean(15, listing.isWasher());
            preparedStatement.setBoolean(16, listing.isDryer());
            preparedStatement.setBoolean(17, listing.isAirConditioning());
            preparedStatement.setBoolean(18, listing.isHeating());
            preparedStatement.setBoolean(19, listing.isDedicatedWorkspace());
            preparedStatement.setBoolean(20, listing.isHairDryer());
            preparedStatement.setBoolean(21, listing.isTv());
            preparedStatement.setBoolean(22, listing.isIron());
            preparedStatement.setBoolean(23, listing.isPool());
            preparedStatement.setBoolean(24, listing.isFreeParking());
            preparedStatement.setBoolean(25, listing.isCrib());
            preparedStatement.setBoolean(26, listing.isBbqGrill());
            preparedStatement.setBoolean(27, listing.isIndoorFireplace());
            preparedStatement.setBoolean(28, listing.isHotTub());
            preparedStatement.setBoolean(29, listing.isEvCharger());
            preparedStatement.setBoolean(30, listing.isGym());
            preparedStatement.setBoolean(31, listing.isBreakfast());
            preparedStatement.setBoolean(32, listing.isSmokingAllowed());
            preparedStatement.setBoolean(33, listing.isBeachfront());
            preparedStatement.setBoolean(34, listing.isWaterfront());
            preparedStatement.setBoolean(35, listing.isSmokeAlarm());
            preparedStatement.setBoolean(36, listing.isCarbonMonoxideAlarm());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve a listing by ID from the database
    public Listing getListingById(int id) {
        String selectSQL = "SELECT * FROM listing WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractListingFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to retrieve all listings from the database
    public List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM listing";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Listing listing = extractListingFromResultSet(resultSet);
                listings.add(listing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    // Method to update a listing in the database
    public boolean updateListing(Listing listing) {
        String updateSQL = """
                UPDATE listing
                SET
                    title = ?,
                    host_id = ?,
                    base_price = ?,
                    property_type = ?,
                    longitude = ?,
                    latitude = ?,
                    address_line_one = ?,
                    address_line_two = ?,
                    city = ?,
                    state = ?,
                    country = ?,
                    postal_code = ?,
                    wifi = ?,
                    kitchen = ?,
                    washer = ?,
                    dryer = ?,
                    air_conditioning = ?,
                    heating = ?,
                    dedicated_workspace = ?,
                    hair_dryer = ?,
                    tv = ?,
                    iron = ?,
                    pool = ?,
                    free_parking = ?,
                    crib = ?,
                    bbq_grill = ?,
                    indoor_fireplace = ?,
                    hot_tub = ?,
                    ev_charger = ?,
                    gym = ?,
                    breakfast = ?,
                    smoking_allowed = ?,
                    beachfront = ?,
                    waterfront = ?,
                    smoke_alarm = ?,
                    carbon_monoxide_alarm = ?
                WHERE
                    id = ?;""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            // Set parameters based on the Listing object
            preparedStatement.setString(1, listing.getTitle());
            preparedStatement.setInt(2, listing.getHostId());
            preparedStatement.setInt(3, listing.getBasePrice());
            preparedStatement.setString(4, listing.getPropertyType().name());
            preparedStatement.setDouble(5, listing.getLongitude());
            preparedStatement.setDouble(6, listing.getLatitude());
            preparedStatement.setString(7, listing.getAddressLineOne());
            preparedStatement.setString(8, listing.getAddressLineTwo());
            preparedStatement.setString(9, listing.getCity());
            preparedStatement.setString(10, listing.getState());
            preparedStatement.setString(11, listing.getCountry());
            preparedStatement.setString(12, listing.getPostalCode());
            preparedStatement.setBoolean(13, listing.isWifi());
            preparedStatement.setBoolean(14, listing.isKitchen());
            preparedStatement.setBoolean(15, listing.isWasher());
            preparedStatement.setBoolean(16, listing.isDryer());
            preparedStatement.setBoolean(17, listing.isAirConditioning());
            preparedStatement.setBoolean(18, listing.isHeating());
            preparedStatement.setBoolean(19, listing.isDedicatedWorkspace());
            preparedStatement.setBoolean(20, listing.isHairDryer());
            preparedStatement.setBoolean(21, listing.isTv());
            preparedStatement.setBoolean(22, listing.isIron());
            preparedStatement.setBoolean(23, listing.isPool());
            preparedStatement.setBoolean(24, listing.isFreeParking());
            preparedStatement.setBoolean(25, listing.isCrib());
            preparedStatement.setBoolean(26, listing.isBbqGrill());
            preparedStatement.setBoolean(27, listing.isIndoorFireplace());
            preparedStatement.setBoolean(28, listing.isHotTub());
            preparedStatement.setBoolean(29, listing.isEvCharger());
            preparedStatement.setBoolean(30, listing.isGym());
            preparedStatement.setBoolean(31, listing.isBreakfast());
            preparedStatement.setBoolean(32, listing.isSmokingAllowed());
            preparedStatement.setBoolean(33, listing.isBeachfront());
            preparedStatement.setBoolean(34, listing.isWaterfront());
            preparedStatement.setBoolean(35, listing.isSmokeAlarm());
            preparedStatement.setBoolean(36, listing.isCarbonMonoxideAlarm());
            preparedStatement.setInt(37, listing.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a listing from the database by ID
    public boolean deleteListingById(int id) {
        String deleteSQL = "DELETE FROM listing WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateAvailability(int listingId, Date date, boolean available, Integer price) {
        StringBuilder updateSQL = new StringBuilder("UPDATE availability SET available = ?");

        if (price != null) {
            updateSQL.append(", price = ?");
        }

        updateSQL.append(" WHERE listing_id = ? AND calendar_date = ?");

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL.toString())) {
            preparedStatement.setBoolean(1, available);

            int parameterIndex = 2;

            if (price != null) {
                preparedStatement.setInt(parameterIndex++, price);
            }

            preparedStatement.setInt(parameterIndex++, listingId);
            preparedStatement.setDate(parameterIndex, new java.sql.Date(date.getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAvailabilityForDateRange(int listingId, Date startDate, Date endDate, boolean available, int price) {
        ArrayList<Date> dateRange = generateDateRange(startDate, endDate);
        String insertSQL = "INSERT INTO available_dates (listing_id, date, available, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            for (Date date : dateRange) {
                preparedStatement.setInt(1, listingId);
                preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
                preparedStatement.setBoolean(3, available);
                preparedStatement.setInt(4, price);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Date> generateDateRange(Date startDate, Date endDate) {
        ArrayList<Date> dateRange = new ArrayList<>();
        Date current = startDate;
        while (!current.after(endDate)) {
            dateRange.add(current);
            current = new Date(current.getTime() + 24 * 60 * 60 * 1000); // Add one day in milliseconds
        }
        return dateRange;
    }

    public ArrayList<Listing> getByAvailability(Date startDate, Date endDate) {
        String query = "SELECT DISTINCT listing_id " +
                "FROM available_dates " +
                "WHERE date BETWEEN ? AND ? " +
                "  AND available = TRUE";

        ArrayList<Listing> listings = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Listing listing = extractListingFromResultSet(resultSet);
                    listings.add(listing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    public ArrayList<Listing> getAllListingsByHost(int hostId) {
        ArrayList<Listing> listings = new ArrayList<>();
        String query = "SELECT * FROM listing WHERE host_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, hostId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Listing listing = extractListingFromResultSet(resultSet);
                    listings.add(listing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    private Listing extractListingFromResultSet(ResultSet resultSet) throws SQLException {
        Listing listing = new Listing();

        // Set attributes of the Listing object based on the ResultSet
        listing.setId(resultSet.getInt("id"));
        listing.setTitle(resultSet.getString("title"));
        listing.setHostId(resultSet.getInt("host_id"));
        listing.setBasePrice(resultSet.getInt("base_price"));
        listing.setPropertyType(PropertyType.valueOf(resultSet.getString("property_type")));
        listing.setLongitude(resultSet.getDouble("longitude"));
        listing.setLatitude(resultSet.getDouble("latitude"));
        listing.setAddressLineOne(resultSet.getString("address_line_one"));
        listing.setAddressLineTwo(resultSet.getString("address_line_two"));
        listing.setCity(resultSet.getString("city"));
        listing.setState(resultSet.getString("state"));
        listing.setCountry(resultSet.getString("country"));
        listing.setPostalCode(resultSet.getString("postal_code"));
        listing.setWifi(resultSet.getBoolean("wifi"));
        listing.setKitchen(resultSet.getBoolean("kitchen"));
        listing.setWasher(resultSet.getBoolean("washer"));
        listing.setDryer(resultSet.getBoolean("dryer"));
        listing.setAirConditioning(resultSet.getBoolean("air_conditioning"));
        listing.setHeating(resultSet.getBoolean("heating"));
        listing.setDedicatedWorkspace(resultSet.getBoolean("dedicated_workspace"));
        listing.setHairDryer(resultSet.getBoolean("hair_dryer"));
        listing.setTv(resultSet.getBoolean("tv"));
        listing.setIron(resultSet.getBoolean("iron"));
        listing.setPool(resultSet.getBoolean("pool"));
        listing.setFreeParking(resultSet.getBoolean("free_parking"));
        listing.setCrib(resultSet.getBoolean("crib"));
        listing.setBbqGrill(resultSet.getBoolean("bbq_grill"));
        listing.setIndoorFireplace(resultSet.getBoolean("indoor_fireplace"));
        listing.setHotTub(resultSet.getBoolean("hot_tub"));
        listing.setEvCharger(resultSet.getBoolean("ev_charger"));
        listing.setGym(resultSet.getBoolean("gym"));
        listing.setBreakfast(resultSet.getBoolean("breakfast"));
        listing.setSmokingAllowed(resultSet.getBoolean("smoking_allowed"));
        listing.setBeachfront(resultSet.getBoolean("beachfront"));
        listing.setWaterfront(resultSet.getBoolean("waterfront"));
        listing.setSmokeAlarm(resultSet.getBoolean("smoke_alarm"));
        listing.setCarbonMonoxideAlarm(resultSet.getBoolean("carbon_monoxide_alarm"));

        return listing;
    }

    public Listing createListingFromAmenities(String title, int hostId, int basePrice,
                                              PropertyType propertyType, double longitude, double latitude,
                                              String addressLineOne, String addressLineTwo, String city, String state,
                                              String country, String postalCode, List<String> amenityList) {

        Map<String, Boolean> amenityMap = new HashMap<>();

        // Initialize the amenity map with all amenities set to false
        for (String amenity : allPossibleAmenities) {
            amenityMap.put(amenity, false);
        }

        // Loop through the amenityList and update the amenity map
        for (String amenity : amenityList) {
            amenityMap.put(amenity, true);
        }

        // Create and return the Listing object using the constructor
        return new Listing(
                title, hostId, basePrice, propertyType,
                longitude, latitude, addressLineOne, addressLineTwo,
                city, state, country, postalCode,
                amenityMap.get("wifi"), amenityMap.get("kitchen"), amenityMap.get("washer"), amenityMap.get("dryer"),
                amenityMap.get("airConditioning"), amenityMap.get("heating"), amenityMap.get("dedicatedWorkspace"),
                amenityMap.get("hairDryer"), amenityMap.get("tv"), amenityMap.get("iron"), amenityMap.get("pool"),
                amenityMap.get("freeParking"), amenityMap.get("crib"), amenityMap.get("bbqGrill"),
                amenityMap.get("indoorFireplace"), amenityMap.get("hotTub"), amenityMap.get("evCharger"),
                amenityMap.get("gym"), amenityMap.get("breakfast"), amenityMap.get("smokingAllowed"),
                amenityMap.get("beachfront"), amenityMap.get("waterfront"), amenityMap.get("smokeAlarm"),
                amenityMap.get("carbonMonoxideAlarm")
        );
    }
}


