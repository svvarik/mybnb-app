-- My BNB schema

DROP SCHEMA IF EXISTS mybnb;
CREATE SCHEMA mybnb;
USE mybnb;

-- Renter who rents listings
CREATE TABLE renter
(
    id               INT          NOT NULL AUTO_INCREMENT,
    sin              INT          NOT NULL,
    email            VARCHAR(190) NOT NULL,
    password         TEXT         NOT NULL,
    first_name       TEXT         NOT NULL,
    last_name        TEXT         NOT NULL,
    date_of_birth    DATE         NOT NULL,
    address_line_one TEXT         NOT NULL,
    address_line_two TEXT,
    city             TEXT         NOT NULL,
    state            TEXT         NOT NULL,
    country          TEXT         NOT NULL,
    postal_code      TEXT         NOT NULL,
    occupation       TEXT         NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (sin),
    CONSTRAINT renter_sin_length CHECK (CHAR_LENGTH(sin) = 9),
    CONSTRAINT renter_pass_min_length CHECK (CHAR_LENGTH(password) >= 10),
    CONSTRAINT renter_pass_max_length CHECK (CHAR_LENGTH(password) <= 15)
);

-- A host who manages listings
CREATE TABLE host
(
    id               INT          NOT NULL AUTO_INCREMENT,
    sin              INT          NOT NULL,
    email            VARCHAR(190) NOT NULL,
    password         TEXT         NOT NULL,
    first_name       TEXT         NOT NULL,
    last_name        TEXT         NOT NULL,
    date_of_birth    DATE         NOT NULL,
    address_line_one TEXT         NOT NULL,
    address_line_two TEXT,
    city             TEXT         NOT NULL,
    state            TEXT         NOT NULL,
    country          TEXT         NOT NULL,
    postal_code      TEXT         NOT NULL,
    occupation       TEXT         NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (sin),
    CONSTRAINT host_sin_length CHECK (CHAR_LENGTH(sin) = 9),
    CONSTRAINT host_pass_min_length CHECK (CHAR_LENGTH(password) >= 10),
    CONSTRAINT host_pass_max_length CHECK (CHAR_LENGTH(password) <= 15)
);

-- A listing created by a host
CREATE TABLE listing
(
    id                    INT           NOT NULL AUTO_INCREMENT,
    title                 TEXT          NOT NULL,
    host_id               INT,
    base_price            INT           NOT NULL,
    property_type         ENUM('House', 'Apartment', 'Guesthouse', 'Hotel'),
    longitude             DECIMAL(9, 6) NOT NULL,
    latitude              DECIMAL(8, 6) NOT NULL,
    address_line_one      TEXT          NOT NULL,
    address_line_two      TEXT,
    city                  TEXT          NOT NULL,
    state                 TEXT          NOT NULL,
    country               TEXT          NOT NULL,
    postal_code           TEXT          NOT NULL,
    wifi                  BOOLEAN       NOT NULL,
    kitchen               BOOLEAN       NOT NULL,
    washer                BOOLEAN       NOT NULL,
    dryer                 BOOLEAN       NOT NULL,
    air_conditioning      BOOLEAN       NOT NULL,
    heating               BOOLEAN       NOT NULL,
    dedicated_workspace   BOOLEAN       NOT NULL,
    hair_dryer            BOOLEAN       NOT NULL,
    tv                    BOOLEAN       NOT NULL,
    iron                  BOOLEAN       NOT NULL,
    pool                  BOOLEAN       NOT NULL,
    free_parking          BOOLEAN       NOT NULL,
    crib                  BOOLEAN       NOT NULL,
    bbq_grill             BOOLEAN       NOT NULL,
    indoor_fireplace      BOOLEAN       NOT NULL,
    hot_tub               BOOLEAN       NOT NULL,
    ev_charger            BOOLEAN       NOT NULL,
    gym                   BOOLEAN       NOT NULL,
    breakfast             BOOLEAN       NOT NULL,
    smoking_allowed       BOOLEAN       NOT NULL,
    beachfront            BOOLEAN       NOT NULL,
    waterfront            BOOLEAN       NOT NULL,
    smoke_alarm           BOOLEAN       NOT NULL,
    carbon_monoxide_alarm BOOLEAN       NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (host_id)
        REFERENCES host (id)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- A booking created by a renter
CREATE TABLE booking
(
    id                   INT NOT NULL AUTO_INCREMENT,
    renter_id            INT,
    listing_id           INT,
    status               ENUM('completed', 'inprogress', 'cancelled'),
    cancelled_by         ENUM('host', 'renter') DEFAULT NULL,
    created_at           DATETIME,
    start_date           DATE,
    end_date             DATE,
    last_updated         DATETIME,

    PRIMARY KEY (id),

    FOREIGN KEY (renter_id)
        REFERENCES renter (id)
        ON UPDATE CASCADE ON DELETE SET NULL,

    FOREIGN KEY (listing_id)
        REFERENCES listing (id)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- Review of host by renter
CREATE TABLE review_of_host
(
    id          INT      NOT NULL AUTO_INCREMENT,
    booking_id  INT      NOT NULL,
    reviewer_id INT      NOT NULL,
    host_id     INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    comment     TEXT,
    rating      INT,

    PRIMARY KEY (id),

    FOREIGN KEY (booking_id)
        REFERENCES booking (id)
        ON DELETE CASCADE,

    FOREIGN KEY (reviewer_id)
        REFERENCES renter (id),

    FOREIGN KEY (host_id)
        REFERENCES host (id)
);

-- Review of listing by renter
CREATE TABLE review_of_listing
(
    id          INT      NOT NULL AUTO_INCREMENT,
    booking_id  INT      NOT NULL,
    reviewer_id INT      NOT NULL,
    listing_id  INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    comment     TEXT,
    rating      INT,

    PRIMARY KEY (id),

    FOREIGN KEY (booking_id)
        REFERENCES booking (id)
        ON DELETE CASCADE,

    FOREIGN KEY (reviewer_id)
        REFERENCES renter (id),

    FOREIGN KEY (listing_id)
        REFERENCES listing (id)
);

-- Review of renter by host
CREATE TABLE review_of_renter
(
    id          INT      NOT NULL AUTO_INCREMENT,
    booking_id  INT      NOT NULL,
    reviewer_id INT      NOT NULL,
    renter_id   INT      NOT NULL,
    created_at  DATETIME NOT NULL,
    comment     TEXT,
    rating      INT,

    PRIMARY KEY (id),

    FOREIGN KEY (booking_id)
        REFERENCES booking (id)
        ON DELETE CASCADE,


    FOREIGN KEY (reviewer_id)
        REFERENCES host (id),

    FOREIGN KEY (renter_id)
        REFERENCES renter (id)
);

-- All the available dates per listing
CREATE TABLE available_dates
(
    date       DATE,
    listing_id INT,
    price      INT,
    available  BOOLEAN,

    PRIMARY KEY (date, listing_id),

    FOREIGN KEY (listing_id)
        REFERENCES listing (id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- Payment information for the users
CREATE TABLE payment_information
(
    id            INT NOT NULL AUTO_INCREMENT,
    renter_id     INT NOT NULL,
    card_num      INT NOT NULL,
    exp_date      INT NOT NULL,
    security_code INT NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (renter_id)
        REFERENCES renter (id)
        ON DELETE CASCADE
);

-- Function for calculating distance between geocoordinates
DROP FUNCTION IF EXISTS GEO_DISTANCE;
DELIMITER $$
CREATE FUNCTION GEO_DISTANCE(`lat1` VARCHAR(200), `lng1` VARCHAR(200), `lat2` VARCHAR(200),
                             `lng2` VARCHAR(200)) RETURNS varchar(10)
    DETERMINISTIC
BEGIN
    DECLARE distance varchar(10);

    IF lat1 = lat2 AND lng1 = lng2 THEN
        SET distance = 0.0;

    ELSE
        SET distance = 6371 * ACOS(
                        COS(RADIANS(lat2))
                        * COS(RADIANS(lat1))
                        * COS(RADIANS(lng1) - RADIANS(lng2))
                    + SIN(RADIANS(lat2))
                            * SIN(RADIANS(lat1))
            );
    END IF;

RETURN distance;
END$$
DELIMITER ;

-- Load host.csv
LOAD DATA LOCAL INFILE './host.csv'
    INTO TABLE host
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

-- Load renter.csv
LOAD DATA LOCAL INFILE 'renter.csv'
    INTO TABLE renter
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

-- Load listing.csv
LOAD DATA LOCAL INFILE 'listing.csv'
    INTO TABLE listing
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

-- Load booking.csv
LOAD DATA LOCAL INFILE './booking.csv'
    INTO TABLE booking
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

-- Load available_dates.csv
LOAD DATA LOCAL INFILE './available_dates.csv'
    INTO TABLE available_dates
    FIELDS TERMINATED BY ',' ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

-- Load review_of_listing.csv
LOAD DATA LOCAL INFILE './review_of_listing.csv'
    INTO TABLE review_of_listing
    FIELDS TERMINATED BY ',' ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

-- Load review_of_host.csv
LOAD DATA LOCAL INFILE './review_of_host.csv'
    INTO TABLE review_of_host
    FIELDS TERMINATED BY ',' ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

-- Load review_of_renter.csv
LOAD DATA LOCAL INFILE './review_of_renter.csv'
    INTO TABLE review_of_renter
    FIELDS TERMINATED BY ',' ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;