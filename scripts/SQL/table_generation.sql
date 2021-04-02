DROP TABLE IF EXISTS Locations CASCADE;
CREATE TABLE IF NOT EXISTS Locations (
	Loc_ID int GENERATED ALWAYS AS IDENTITY (START WITH 1000000 INCREMENT BY 1),
	Loc_Address varchar(1027) NOT NULL,
	Loc_City varchar(1027) NOT NULL,
	Loc_State varchar(5) NOT NULL,
	Loc_Zip varchar(10) NOT NULL,
	Loc_Latitude float NOT NULL,
	Loc_Longitude float NOT NULL,
	PRIMARY KEY(Loc_ID)
);
GRANT ALL PRIVILEGES ON Locations TO ultrabox, treydinges, scouter1323;

DROP TABLE IF EXISTS Categories CASCADE;
CREATE TABLE IF NOT EXISTS Categories (
	Cat_ID int GENERATED ALWAYS AS IDENTITY (START WITH 2000000 INCREMENT BY 1),
	Cat_Name varchar(1027) NOT NULL,
	PRIMARY KEY(Cat_ID)
);
GRANT ALL PRIVILEGES ON Categories TO ultrabox, treydinges, scouter1323;

DROP TABLE IF EXISTS Users CASCADE;
CREATE TABLE IF NOT EXISTS Users (
	Usr_ID varchar(22) NOT NULL,
	Usr_FName varchar(1027) NOT NULL,
	Usr_ReviewCount int NOT NULL,
	Usr_JoinDate timestamp NOT NULL,
	Usr_AverageRating float NOT NULL CHECK (Usr_AverageRating between 0.00 and 5.00),
	Usr_CurrentlyElite bit NOT NULL,
	Usr_ComplimentsRecv int NOT NULL,
	PRIMARY KEY(Usr_ID)
);
GRANT ALL PRIVILEGES ON Users TO ultrabox, treydinges, scouter1323;

DROP TABLE IF EXISTS Businesses CASCADE;
CREATE TABLE IF NOT EXISTS Businesses (
	Bus_ID varchar(22) NOT NULL,
	Loc_ID int,
	Cat_ID int,
	Bus_Name varchar(1027) NOT NULL,
	Bus_Rating float NOT NULL CHECK (Bus_Rating between 0.00 and 5.00),
	Bus_ReviewCount int NOT NULL,
	PRIMARY KEY(Bus_ID),
	CONSTRAINT fk_Location
		FOREIGN KEY(Loc_ID)
			REFERENCES Locations(Loc_ID)
			ON DELETE SET NULL,
	CONSTRAINT fk_Category
		FOREIGN KEY(Cat_ID)
			REFERENCES Categories(Cat_ID)
			ON DELETE SET NULL
);
GRANT ALL PRIVILEGES ON Businesses TO ultrabox, treydinges, scouter1323;

DROP TABLE IF EXISTS EliteHistory;
CREATE TABLE IF NOT EXISTS EliteHistory (
	Hist_ID int GENERATED ALWAYS AS IDENTITY (START WITH 3000000 INCREMENT BY 1),
	Usr_ID varchar(22),
	Hist_Year date NOT NULL,
	Hist_Current bit NOT NULL,
	PRIMARY KEY(Hist_ID),
	CONSTRAINT fk_User
		FOREIGN KEY(Usr_ID)
			REFERENCES Users(Usr_ID)
			ON DELETE SET NULL
);
GRANT ALL PRIVILEGES ON EliteHistory TO ultrabox, treydinges, scouter1323;

DROP TABLE IF EXISTS Reviews;
CREATE TABLE IF NOT EXISTS Reviews (
	Rev_ID varchar(22) NOT NULL,
	Usr_ID varchar(22),
	Bus_ID varchar(22),
	Rev_Rating int NOT NULL,
	Rev_Date timestamp NOT NULL,
	Rev_Text varchar(10000) NOT NULL,
	Rev_ComplimentsRecv int NOT NULL,
	PRIMARY KEY(Rev_ID),
	CONSTRAINT fk_User
		FOREIGN KEY(Usr_ID)
			REFERENCES Users(Usr_ID)
			ON DELETE SET NULL,
	CONSTRAINT fk_Business
		FOREIGN KEY(Bus_ID)
			REFERENCES Businesses(Bus_ID)
			ON DELETE SET NULL
);
GRANT ALL PRIVILEGES ON Reviews TO ultrabox, treydinges, scouter1323;

DROP TABLE IF EXISTS Tips;
CREATE TABLE IF NOT EXISTS Tips (
	Tip_ID int GENERATED ALWAYS AS IDENTITY (START WITH 4000000 INCREMENT BY 1),
	Usr_ID varchar(22),
	Bus_ID varchar(22),
	Tip_Date timestamp NOT NULL,
	Tip_Text varchar(1000) NOT NULL,
	Tip_ComplimentsRecv int NOT NULL,
	PRIMARY KEY(Tip_ID),
	CONSTRAINT fk_User
		FOREIGN KEY(Usr_ID)
			REFERENCES Users(Usr_ID)
			ON DELETE SET NULL,
	CONSTRAINT fk_Business
		FOREIGN KEY(Bus_ID)
			REFERENCES Businesses(Bus_ID)
			ON DELETE SET NULL
);
GRANT ALL PRIVILEGES ON Tips TO ultrabox, treydinges, scouter1323;
