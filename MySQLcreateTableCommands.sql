CREATE TABLE `addresses` (
  `addressID` int(11) NOT NULL AUTO_INCREMENT,
  `streetAddress` varchar(255) NOT NULL,
  `city` varchar(50) NOT NULL,
  `zipCode` varchar(5) NOT NULL,
  PRIMARY KEY (`addressID`),
  UNIQUE KEY `unique_address` (`streetAddress`,`zipCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `bodyStyles` (
  `styleID` int(11) NOT NULL AUTO_INCREMENT,
  `styleName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`styleID`),
  UNIQUE KEY `unique_style` (`styleName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `campuses` (
  `campusID` int(11) NOT NULL AUTO_INCREMENT,
  `campusName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`campusID`),
  UNIQUE KEY `unique_campus` (`campusName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `cars` (
  `carID` int(11) NOT NULL AUTO_INCREMENT,
  `driver` int(11) NOT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `make` varchar(50) NOT NULL,
  `bodyStyle` int(11) NOT NULL,
  `color` int(11) NOT NULL,
  `seats` int(11) DEFAULT NULL,
  PRIMARY KEY (`carID`),
  KEY `driver` (`driver`),
  KEY `bodyStyle` (`bodyStyle`),
  KEY `color` (`color`),
  CONSTRAINT `cars_ibfk_1` FOREIGN KEY (`driver`) REFERENCES `users` (`userID`),
  CONSTRAINT `cars_ibfk_2` FOREIGN KEY (`bodyStyle`) REFERENCES `bodyStyles` (`styleID`),
  CONSTRAINT `cars_ibfk_3` FOREIGN KEY (`color`) REFERENCES `colors` (`colorID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `colors` (
  `colorID` int(11) NOT NULL AUTO_INCREMENT,
  `colorName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`colorID`),
  UNIQUE KEY `unique_color` (`colorName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `locations` (
  `locationID` int(11) NOT NULL AUTO_INCREMENT,
  `locationName` varchar(50) DEFAULT NULL,
  `locationAddress` int(11) NOT NULL,
  `campus` int(11) DEFAULT NULL,
  `xCoordinate` varchar(45) NOT NULL,
  `yCoordinate` varchar(45) NOT NULL,
  PRIMARY KEY (`locationID`),
  UNIQUE KEY `unique_location` (`locationAddress`),
  UNIQUE KEY `name_unique_on_campus` (`locationName`,`campus`),
  KEY `campus` (`campus`),
  CONSTRAINT `locations_ibfk_1` FOREIGN KEY (`locationAddress`) REFERENCES `addresses` (`addressID`),
  CONSTRAINT `locations_ibfk_2` FOREIGN KEY (`campus`) REFERENCES `campuses` (`campusID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `requestNotifications` (
  `notificationID` int(11) NOT NULL AUTO_INCREMENT,
  `request` int(11) NOT NULL,
  `notificationtype` char(1) NOT NULL,
  PRIMARY KEY (`notificationID`),
  UNIQUE KEY `unique_request` (`request`),
  CONSTRAINT `requestNotifications_ibfk_1` FOREIGN KEY (`request`) REFERENCES `requests` (`requestID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `requests` (
  `requestID` int(11) NOT NULL AUTO_INCREMENT,
  `requestingUser` int(11) NOT NULL,
  `ride` int(11) NOT NULL,
  `sent` time NOT NULL,
  `requestComment` text,
  `requestStatus` char(1) NOT NULL,
  PRIMARY KEY (`requestID`),
  UNIQUE KEY `unique_request` (`requestingUser`,`ride`),
  KEY `ride` (`ride`),
  CONSTRAINT `requests_ibfk_1` FOREIGN KEY (`requestingUser`) REFERENCES `users` (`userID`),
  CONSTRAINT `requests_ibfk_2` FOREIGN KEY (`ride`) REFERENCES `rides` (`rideID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `rideNotifications` (
  `notificationID` int(11) NOT NULL AUTO_INCREMENT,
  `ride` int(11) NOT NULL,
  `notificationType` char(1) NOT NULL,
  PRIMARY KEY (`notificationID`),
  UNIQUE KEY `unique_ride` (`ride`),
  CONSTRAINT `rideNotifications_ibfk_1` FOREIGN KEY (`ride`) REFERENCES `rides` (`rideID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `rides` (
  `rideID` int(11) NOT NULL AUTO_INCREMENT,
  `car` int(11) NOT NULL,
  `origin` int(11) NOT NULL,
  `destination` int(11) NOT NULL,
  `toCampus` tinyint(1) DEFAULT NULL,
  `departure` time DEFAULT NULL,
  `availableSeats` int(11) DEFAULT NULL,
  PRIMARY KEY (`rideID`),
  KEY `car` (`car`),
  KEY `origin` (`origin`),
  KEY `destination` (`destination`),
  CONSTRAINT `rides_ibfk_1` FOREIGN KEY (`car`) REFERENCES `cars` (`carID`),
  CONSTRAINT `rides_ibfk_2` FOREIGN KEY (`origin`) REFERENCES `locations` (`locationID`),
  CONSTRAINT `rides_ibfk_3` FOREIGN KEY (`destination`) REFERENCES `locations` (`locationID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `users` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `memberName` varchar(50) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `memberPassword` char(64) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `address` int(11) DEFAULT NULL,
  `mobileNumber` char(13) DEFAULT NULL,
  `receiveEmailNotification` tinyint(1) DEFAULT NULL,
  `receiveSMSNotification` tinyint(1) DEFAULT NULL,
  `carrier` char(1) DEFAULT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `unique_userName` (`userName`),
  UNIQUE KEY `unique_email` (`email`),
  UNIQUE KEY `unique_mobile_number` (`mobileNumber`),
  KEY `address` (`address`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`address`) REFERENCES `addresses` (`addressID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
