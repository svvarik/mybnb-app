package com.mybnb.mybnb.classes.listing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchListings {

    private final Connection connection;

    public SearchListings(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Listing> searchByLocationWithDistanceAndRanking(
            double searchLat, double searchLng, double maxDistance,
            boolean sortByDistance, boolean sortByPrice, boolean ascending,
            Date startDate, Date endDate,
            Integer minPrice, Integer maxPrice,
            List<String> amenities) {

        ArrayList<Listing> listings = new ArrayList<>();

        StringBuilder selectListings = new StringBuilder("SELECT * FROM listing, (" +
                "SELECT listing.id, GEO_DISTANCE(?, ?, latitude, longitude) AS calculated_distance "
                + "FROM listing, available_dates "
                + "WHERE listing.id = available_dates.listing_id "
                + "AND GEO_DISTANCE(?, ?, latitude, longitude) <= ? ");

        List<Object> parameters = new ArrayList<>();
        parameters.add(searchLat);
        parameters.add(searchLng);
        parameters.add(searchLat);
        parameters.add(searchLng);
        parameters.add(maxDistance != 0 ? maxDistance : 10);



        // Add price range filter
        if (minPrice != null && minPrice != 0|| maxPrice != null && maxPrice != 0) {
            selectListings.append("AND base_price ");

            if (minPrice != null && maxPrice != null && minPrice != 0 && maxPrice != 0) {
                selectListings.append("BETWEEN ? AND ? ");
                parameters.add(minPrice);
                parameters.add(maxPrice);
            } else if (minPrice != null && minPrice != 0) {
                selectListings.append(">= ? ");
                parameters.add(minPrice);
            } else {
                selectListings.append("<= ? ");
                parameters.add(maxPrice);
            }
        }
        // Add amenities filter
        if (!amenities.isEmpty()) {
            selectListings.append("AND (");
            for (int i = 0; i < amenities.size(); i++) {
                selectListings.append(i == 0 ? "" : " AND ");
                selectListings.append(amenities.get(i)).append(" = true");
            }
            selectListings.append(") ");
        }

        selectListings.append("GROUP BY listing.id, calculated_distance ");


        // Add temporal filter
        if (startDate != null && endDate != null) {
            selectListings.append("HAVING COUNT(date) = DATEDIFF(?, ?) + 1 ");
            parameters.add(new java.sql.Date(startDate.getTime()));
            parameters.add(new java.sql.Date(endDate.getTime()));
        }

        selectListings.append(") as potential WHERE listing.id = potential.id ");

        if (sortByDistance) {
            selectListings.append(ascending ? "ORDER BY listing.calculated_distance ASC" : "ORDER BY calculated_distance");
        } else if (sortByPrice) {
            selectListings.append(ascending ? "ORDER BY listing.base_price ASC" : "ORDER BY base_price");
        } else {
            selectListings.append("ORDER BY listing.base_price DESC"); // Default sorting by price descending
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectListings.toString())) {
            int parameterIndex = 1;
            for (Object parameter : parameters) {
                preparedStatement.setObject(parameterIndex++, parameter);
            }

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

    public ArrayList<Listing> searchBySimilarPostalCode(
            String postalCode,
            boolean sortByDistance, boolean sortByPrice, boolean ascending,
            Date startDate, Date endDate,
            Integer minPrice, Integer maxPrice,
            List<String> amenities) {
        ArrayList<Listing> listings = new ArrayList<>();
        String firstThreeLetters = postalCode.substring(0, 3);

        StringBuilder selectListings = new StringBuilder("SELECT * FROM listing, (" +
                "SELECT listing.id"
                + "FROM listing, available_dates "
                + "WHERE listing.id = available_dates.listing_id "
                + "AND LEFT(listing.postal_code, 3) = ? ");

        List<Object> parameters = new ArrayList<>();
        parameters.add(firstThreeLetters);

        // Add price range filter
        if (minPrice != null && minPrice != 0|| maxPrice != null && maxPrice != 0) {
            selectListings.append("AND base_price ");

            if (minPrice != null && maxPrice != null && minPrice != 0 && maxPrice != 0) {
                selectListings.append("BETWEEN ? AND ? ");
                parameters.add(minPrice);
                parameters.add(maxPrice);
            } else if (minPrice != null && minPrice != 0) {
                selectListings.append(">= ? ");
                parameters.add(minPrice);
            } else {
                selectListings.append("<= ? ");
                parameters.add(maxPrice);
            }
        }
        // Add amenities filter
        if (!amenities.isEmpty()) {
            selectListings.append("AND (");
            for (int i = 0; i < amenities.size(); i++) {
                selectListings.append(i == 0 ? "" : " AND ");
                selectListings.append(amenities.get(i)).append(" = true");
            }
            selectListings.append(") ");
        }

        selectListings.append("GROUP BY listing.id, calculated_distance ");


        // Add temporal filter
        if (startDate != null && endDate != null) {
            selectListings.append("HAVING COUNT(date) = DATEDIFF(?, ?) + 1 ");
            parameters.add(new java.sql.Date(startDate.getTime()));
            parameters.add(new java.sql.Date(endDate.getTime()));
        }

        selectListings.append(") as potential WHERE listing.id = potential.id ");

        if (sortByDistance) {
            selectListings.append(ascending ? "ORDER BY listing.calculated_distance ASC" : "ORDER BY calculated_distance");
        } else if (sortByPrice) {
            selectListings.append(ascending ? "ORDER BY listing.base_price ASC" : "ORDER BY base_price");
        } else {
            selectListings.append("ORDER BY listing.base_price DESC"); // Default sorting by price descending
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectListings.toString())) {
            int parameterIndex = 1;
            for (Object parameter : parameters) {
                preparedStatement.setObject(parameterIndex++, parameter);
            }

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

    public ArrayList<Listing> searchByAddressParameters(
            String addressLineOne, String addressLineTwo,
            String city, String state, String country, boolean sortByDistance, boolean sortByPrice, boolean ascending,
            Date startDate, Date endDate,
            Integer minPrice, Integer maxPrice,
            List<String> amenities) {

        ArrayList<Listing> listings = new ArrayList<>();

        StringBuilder selectListings = new StringBuilder("SELECT * FROM listing, (" +
                "SELECT listing.id"
                + "FROM listing, available_dates "
                + "WHERE listing.id = available_dates.listing_id "
                + "address_line_one = ? AND " +
                "(address_line_two = ? OR address_line_two IS NULL) AND " +
                "city = ? AND " +
                "state = ? AND " +
                "country = ? AND ");

        List<Object> parameters = new ArrayList<>();
        parameters.add(addressLineOne);
        parameters.add(addressLineTwo);
        parameters.add(city);
        parameters.add(state);
        parameters.add(country);

        // Add price range filter
        if (minPrice != null && minPrice != 0|| maxPrice != null && maxPrice != 0) {
            selectListings.append("AND base_price ");

            if (minPrice != null && maxPrice != null && minPrice != 0 && maxPrice != 0) {
                selectListings.append("BETWEEN ? AND ? ");
                parameters.add(minPrice);
                parameters.add(maxPrice);
            } else if (minPrice != null && minPrice != 0) {
                selectListings.append(">= ? ");
                parameters.add(minPrice);
            } else {
                selectListings.append("<= ? ");
                parameters.add(maxPrice);
            }
        }
        // Add amenities filter
        if (!amenities.isEmpty()) {
            selectListings.append("AND (");
            for (int i = 0; i < amenities.size(); i++) {
                selectListings.append(i == 0 ? "" : " AND ");
                selectListings.append(amenities.get(i)).append(" = true");
            }
            selectListings.append(") ");
        }

        selectListings.append("GROUP BY listing.id, calculated_distance ");


        // Add temporal filter
        if (startDate != null && endDate != null) {
            selectListings.append("HAVING COUNT(date) = DATEDIFF(?, ?) + 1 ");
            parameters.add(new java.sql.Date(startDate.getTime()));
            parameters.add(new java.sql.Date(endDate.getTime()));
        }

        selectListings.append(") as potential WHERE listing.id = potential.id ");

        if (sortByDistance) {
            selectListings.append(ascending ? "ORDER BY listing.calculated_distance ASC" : "ORDER BY calculated_distance");
        } else if (sortByPrice) {
            selectListings.append(ascending ? "ORDER BY listing.base_price ASC" : "ORDER BY base_price");
        } else {
            selectListings.append("ORDER BY listing.base_price DESC"); // Default sorting by price descending
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectListings.toString())) {
            int parameterIndex = 1;
            for (Object parameter : parameters) {
                preparedStatement.setObject(parameterIndex++, parameter);
            }

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
}
