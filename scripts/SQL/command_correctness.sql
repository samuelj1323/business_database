/****** PULL DATA FROM ONE TABLE ******/
-- How many businesses are in the state of Texas?
SELECT COUNT(*) FROM locations

    WHERE loc_state LIKE 'TX';



-- What users are there with 5000 or more reviews?
SELECT usr_fname, usr_reviewcount FROM users

    WHERE usr_reviewcount > 5000 LIMIT 25;



-- What are all of the reviews during or after the year 2011?
SELECT rev_id, rev_rating, rev_date FROM reviews

    WHERE rev_date > '2011-01-01 00:00:00' LIMIT 25;



-- What are all of the tips during or after the year 2012?
SELECT tip_date, tip_text FROM tips

    WHERE tip_date > '2012-01-01 00:00:00' LIMIT 25;



-- What are all of the states?
SELECT DISTINCT loc_state FROM locations

    ORDER BY loc_state ASC LIMIT 25;



/****** PULL AND AGGREGATE DATA FROM ONE TABLE ******/
-- What reviews are 2 star ratings and have more than 150 compliments?
SELECT rev_id, rev_rating, rev_complimentsrecv, rev_date FROM reviews

    WHERE rev_rating = 2

        AND rev_complimentsrecv > 150 LIMIT 25;



-- What are the users that joined during or after 2015 and have more than 1000 reviews?
SELECT usr_fname, usr_joindate, usr_reviewcount FROM users

    WHERE usr_joindate > '2015-01-01 00:00:00'

        AND usr_reviewcount > 1000 LIMIT 25;



-- What are the cities in the state of Nevada, except for Las Vegas?
SELECT DISTINCT loc_city FROM locations

    WHERE loc_state LIKE 'NV'

        AND loc_city NOT LIKE '%Las Vegas%' LIMIT 25;



-- What are the tips that were made during or after the year 2015 and have over 5 compliments?
SELECT tip_date, tip_complimentsrecv FROM tips

    WHERE tip_date > '2015-01-01 00:00:00'

        AND tip_complimentsrecv > 5 LIMIT 25;



/****** QUERY MORE THAN ONE TABLE ******/
-- What are the USPS locations in North Carolina?
SELECT bus_name, loc_address, loc_city, loc_state, loc_zip FROM businesses

    JOIN locations ON businesses.loc_id = locations.loc_id

        WHERE bus_name LIKE '%USPS%'

            AND loc_state LIKE 'NC' LIMIT 25;



-- What businesses are in the category “Active Life”?
SELECT bus_name, businesses.cat_id FROM businesses

    JOIN categories ON businesses.cat_id = categories.cat_id

        WHERE cat_name LIKE '%Active Life%' LIMIT 25;



-- What are all of the reviews for the business "Panda Express"?
SELECT bus_name, businesses.bus_id, rev_id, rev_rating, rev_date FROM reviews

    JOIN businesses ON reviews.bus_id = businesses.bus_id

        WHERE bus_name LIKE '%Panda Express%' LIMIT 25;



-- How many tips are there for the business McDonald’s?
SELECT COUNT(*) FROM businesses

    JOIN tips ON businesses.bus_id = tips.bus_id

        WHERE bus_name LIKE '%McDonald''s%';



-- What is the elite history for all users named John?
SELECT users.usr_id, usr_fname, hist_year FROM users

    JOIN elitehistory ON users.usr_id = elitehistory.usr_id

        WHERE usr_fname LIKE '%John%' LIMIT 25;



-- What are all of the businesses in “Nevada” that are not in “Las Vegas”?
SELECT bus_name FROM businesses

    JOIN locations ON businesses.loc_id = locations.loc_id

        WHERE loc_state LIKE 'NV'

            AND loc_city NOT LIKE '%Las Vegas%' LIMIT 25;



/****** COMBINE QUERYING MULTIPLE TABLES AND AGGREGATING DATA FROM EACH ******/
-- What are all of the tips for all businesses located in zip code 89109?
SELECT bus_name, loc_zip, tip_id FROM businesses

    JOIN tips ON  businesses.bus_id = tips.bus_id

        JOIN locations ON businesses.loc_id = locations.loc_id

            WHERE loc_zip LIKE '89109' LIMIT 25;



-- What users have completed reviews for the business "Chick-fil-A"?
SELECT usr_fname, bus_name, rev_id, rev_rating, rev_date FROM reviews

    JOIN users ON reviews.usr_id = users.usr_id

        JOIN businesses ON reviews.bus_id = businesses.bus_id

            WHERE bus_name LIKE '%Chick-fil-A%' LIMIT 25;



-- What users, who were elite during or before 2013, have reviews of 5 stars?
SELECT DISTINCT users.usr_id, usr_fname, hist_year, rev_rating FROM users

    JOIN elitehistory ON elitehistory.usr_id = users.usr_id

        JOIN reviews ON users.usr_id = reviews.usr_id

            WHERE hist_year < '2014-01-01 00:00:00'

                AND rev_rating = 5 LIMIT 25;



-- What businesses in the “Food” category are in the city “Tempe”?
SELECT bus_name, loc_city, loc_address, businesses.cat_id FROM  businesses

    JOIN locations ON businesses.loc_id = locations.loc_id

        JOIN categories ON businesses.cat_id = categories.cat_id

            WHERE loc_city LIKE '%Tempe%'

                AND cat_name LIKE '%Food%' LIMIT 25;



-- Create a temporary view of all names and review counts for all businesses.
CREATE TEMPORARY VIEW bus_rev

    AS SELECT bus_name, bus_reviewcount FROM businesses;

SELECT * FROM bus_rev LIMIT 25;