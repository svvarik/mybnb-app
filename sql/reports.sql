
# total number of bookings in a specific date range by city
SELECT listing.city, COUNT(*) AS total_bookings
FROM booking, listing
WHERE booking.listing_id = listing.id
  AND booking.start_date = '2023-08-27'
  AND booking.end_date = '2023-08-30'
GROUP BY listing.city;

#same report by zip code within a city.
SELECT listing.city, listing.postal_code, COUNT(*) AS total_bookings
FROM booking, listing
WHERE booking.listing_id = listing.id
  AND booking.start_date = '2023-08-27'
  AND booking.end_date = '2023-08-30'
GROUP BY listing.city, listing.postal_code;

# provide the total number of listings per country
SELECT country, COUNT(*) AS total_listings
FROM listing
GROUP BY country;

#provide the total number of listings per country, city
SELECT country, city, COUNT(*) AS total_listings
FROM listing
GROUP BY country, city;

#provide the total number of listings per country,city, psotal code
SELECT country, city, postal_code, COUNT(*) AS total_listings
FROM listing
GROUP BY country, city, postal_code;

# Rank the hosts by the total number of listings they have overall per country,
SELECT * FROM
    ( SELECT host.id, host.first_name, host.last_name, listing.country, COUNT(listing.id) as listing_num
      FROM host, listing
      WHERE host.id = listing.host_id
      GROUP BY host.id, host.first_name, host.last_name, listing.country) as listings
ORDER BY listings.listing_num DESC;

# Hosts based on the number of listings they have by city.
SELECT * FROM
    ( SELECT host.id, host.first_name, host.last_name, listing.country, listing.city, COUNT(listing.id) as listing_num
      FROM host, listing
      WHERE host.id = listing.host_id
      GROUP BY host.id, host.first_name, host.last_name, listing.country, listing.city) as listings
ORDER BY listings.listing_num, listings.id DESC;

# Percent of ownership
SELECT host_listings.id, first_name, last_name, host_listings.country, host_listings.city, host_num / total_num AS percent
FROM
    ( SELECT host.id, host.first_name, host.last_name, listing.country, listing.city, COUNT(listing.id) as host_num
      FROM host, listing
      WHERE host.id = listing.host_id
      GROUP BY host.id, host.first_name, host.last_name, listing.country, listing.city) as host_listings,
    (SELECT country, city, COUNT(*) AS total_num
     FROM listing
     GROUP BY country, city) as total_listings
WHERE host_listings.country = total_listings.country AND host_listings.city = total_listings.city
HAVING percent > 0.10;


# Rank renters by bookings in date range
SELECT r.id AS renter_id, r.first_name, r.last_name, COUNT(b.id) AS total_bookings
FROM renter r, booking b
WHERE r.id = b.renter_id
  AND b.start_date >= '2023-08-27' AND b.end_date <= '2023-08-30'
GROUP BY r.id, r.first_name, r.last_name
ORDER BY total_bookings DESC;

# Rank cancellations for hosts
SELECT host.id, host.first_name, host.last_name, COUNT(booking.id) as cancellations
FROM booking, listing, host
WHERE status = 'cancelled' AND cancelled_by='host' AND booking.listing_id = listing.id AND
        host.id = listing.host_id AND YEAR(booking.start_date) = ?
GROUP BY host.id, host.first_name, host.last_name
ORDER BY cancellations DESC;

# Rank cancellations for renters
SELECT renter.id, renter.first_name, renter.last_name, COUNT(booking.id) as cancellations
FROM booking, renter
WHERE status = 'cancelled' AND cancelled_by='renter' AND
        renter.id = booking.renter_id AND YEAR(booking.start_date) = ?
GROUP BY renter.id, renter.first_name, renter.last_name
ORDER BY cancellations DESC;



# Toolkit for pricing
SELECT listing.country, listing.city, AVG(listing.base_price) as suggested_price
FROM listing
GROUP BY listing.country, listing.city;


# Toolkft for amenities
SELECT * FROM (
                  SELECT listing.id, COUNT(booking.id) as num_bookings
                  FROM booking, listing
                  WHERE booking.listing_id = listing.id
                  GROUP BY listing.id
                  ORDER BY num_bookings
                  LIMIT 1) as top, listing
where top.id = listing.id;
