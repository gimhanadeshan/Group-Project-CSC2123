-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 25, 2024 at 08:13 AM
-- Server version: 8.0.31
-- PHP Version: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wildlifehms`
--

-- --------------------------------------------------------

--
-- Table structure for table `alertstatus`
--

DROP TABLE IF EXISTS `alertstatus`;
CREATE TABLE IF NOT EXISTS `alertstatus` (
  `AlertStatusID` int NOT NULL AUTO_INCREMENT,
  `AlertStatus` varchar(50) NOT NULL,
  `Remarks` text NOT NULL,
  PRIMARY KEY (`AlertStatusID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `alertstatus`
--

INSERT INTO `alertstatus` (`AlertStatusID`, `AlertStatus`, `Remarks`) VALUES
(1, 'Under Review', 'Indicates that the alert is currently being assessed or evaluated.'),
(3, 'Pending', 'Suggests that action is pending or awaiting further information before resolution.'),
(4, 'Closed', 'Signifies that the alert has been resolved and no further action is required.');

-- --------------------------------------------------------

--
-- Table structure for table `alerttypes`
--

DROP TABLE IF EXISTS `alerttypes`;
CREATE TABLE IF NOT EXISTS `alerttypes` (
  `AlertTypeID` int NOT NULL AUTO_INCREMENT,
  `AlertType` varchar(50) NOT NULL,
  `Remarks` text NOT NULL,
  PRIMARY KEY (`AlertTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `conservationstatus`
--

DROP TABLE IF EXISTS `conservationstatus`;
CREATE TABLE IF NOT EXISTS `conservationstatus` (
  `ConservationStatusID` int NOT NULL AUTO_INCREMENT,
  `ConservationStatus` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Remark` text NOT NULL,
  PRIMARY KEY (`ConservationStatusID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `environmentalconditions`
--

DROP TABLE IF EXISTS `environmentalconditions`;
CREATE TABLE IF NOT EXISTS `environmentalconditions` (
  `ConditionID` int NOT NULL AUTO_INCREMENT,
  `EC_ID` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ConditionType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ConditionValue` decimal(9,2) DEFAULT NULL,
  `HabitatID` int NOT NULL,
  `PostingDate` date DEFAULT NULL,
  PRIMARY KEY (`ConditionID`),
  UNIQUE KEY `EC_ID` (`EC_ID`),
  KEY `HabitatID` (`HabitatID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `environmentalconditions`
--

INSERT INTO `environmentalconditions` (`ConditionID`, `EC_ID`, `ConditionType`, `ConditionValue`, `HabitatID`, `PostingDate`) VALUES
(9, 'EC00009', 'Temperature', '25.50', 83, '2024-04-23'),
(10, 'EC00010', 'Humidity', '70.20', 83, '2024-04-23'),
(11, 'EC00011', 'pH', '6.80', 85, '2024-04-23');

-- --------------------------------------------------------

--
-- Table structure for table `habitats`
--

DROP TABLE IF EXISTS `habitats`;
CREATE TABLE IF NOT EXISTS `habitats` (
  `HabitatID` int NOT NULL AUTO_INCREMENT,
  `HA_ID` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `HabitatName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Location` varchar(50) DEFAULT NULL,
  `Size` decimal(9,2) NOT NULL,
  `Description` text,
  PRIMARY KEY (`HabitatID`),
  UNIQUE KEY `HabitatID` (`HabitatID`),
  UNIQUE KEY `HA_ID` (`HA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `habitats`
--

INSERT INTO `habitats` (`HabitatID`, `HA_ID`, `HabitatName`, `Location`, `Size`, `Description`) VALUES
(83, 'HA00083', 'Forest Reserve', '35.6895, -78.9196', '50.00', 'Lush green forest with diverse flora and fauna'),
(84, 'HA00084', 'Wetland Preserve', '40.7128, -74.0060', '300.00', 'Rich in aquatic life and waterfowl'),
(85, 'HA00085', 'Grassland Habitat', '36.7783, -119.4179', '700.00', 'Open grassy landscape supporting herbivores');

-- --------------------------------------------------------

--
-- Table structure for table `monitoringalerts`
--

DROP TABLE IF EXISTS `monitoringalerts`;
CREATE TABLE IF NOT EXISTS `monitoringalerts` (
  `AlertID` int NOT NULL AUTO_INCREMENT,
  `AL_ID` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `AlertType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `AlertDescription` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `AlertDate` date NOT NULL,
  `HabitatID` int NOT NULL,
  `AlertStatus` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`AlertID`),
  UNIQUE KEY `AL_ID` (`AL_ID`),
  KEY `HabitatID` (`HabitatID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `monitoringalerts`
--

INSERT INTO `monitoringalerts` (`AlertID`, `AL_ID`, `AlertType`, `AlertDescription`, `AlertDate`, `HabitatID`, `AlertStatus`) VALUES
(15, 'AL00015', 'HabitatDegradation', 'Increased logging activity in the area', '2024-04-23', 83, NULL),
(16, 'AL00016', 'InvasiveSpecies', 'Presence of invasive plants observed', '2024-04-16', 84, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `observations`
--

DROP TABLE IF EXISTS `observations`;
CREATE TABLE IF NOT EXISTS `observations` (
  `ObservationID` int NOT NULL AUTO_INCREMENT,
  `OB_ID` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `HabitatID` int NOT NULL,
  `SpeciesID` int NOT NULL,
  `ObserverName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ObservationDate` date NOT NULL,
  `Notes` text,
  `Attachments` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  PRIMARY KEY (`ObservationID`),
  KEY `SpeciesID` (`SpeciesID`),
  KEY `HabitatID` (`HabitatID`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `observations`
--

INSERT INTO `observations` (`ObservationID`, `OB_ID`, `HabitatID`, `SpeciesID`, `ObserverName`, `ObservationDate`, `Notes`, `Attachments`) VALUES
(24, 'OB00024', 83, 11, 'John Doe', '2024-04-23', 'Spotted a Bengal Tiger near the river', '/D:/Wildlife_HMS/src/main/resources/observationAttachments/welcome.jpeg\n/D:/Wildlife_HMS/src/main/resources/observationAttachments/welcome.jpeg\n/C:/Users/Gimhan/Pictures/Screenshots/Screenshot 2023-09-15 121733.png\n'),
(25, 'OB00025', 84, 12, 'Jane Smith', '2024-04-22', 'Red Fox family observed near the marsh', '/C:/Users/Gimhan/Pictures/Screenshots/Screenshot 2023-06-17 114141.png\n/C:/Users/Gimhan/Pictures/Screenshots/Screenshot 2023-11-18 230102.png\n');

-- --------------------------------------------------------

--
-- Table structure for table `species`
--

DROP TABLE IF EXISTS `species`;
CREATE TABLE IF NOT EXISTS `species` (
  `SpeciesID` int NOT NULL AUTO_INCREMENT,
  `SP_ID` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `CommonName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ScientificName` varchar(255) DEFAULT NULL,
  `ConservationStatus` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`SpeciesID`),
  UNIQUE KEY `SP_ID` (`SP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `species`
--

INSERT INTO `species` (`SpeciesID`, `SP_ID`, `CommonName`, `ScientificName`, `ConservationStatus`, `Description`) VALUES
(11, 'SP00011', 'Bengal Tiger', 'Panthera tigris tigris', 'Endangered', 'Large carnivorous mammal native to Asia'),
(12, 'SP00012', 'Red Fox', 'Vulpes vulpes', 'Least Concern', 'Small omnivorous mammal with a distinctive coat'),
(13, 'SP00013', 'Oak Tree', 'Quercus robur', 'Not Evaluated', 'Deciduous tree species common in temperate regions');

-- --------------------------------------------------------

--
-- Table structure for table `useraccounts`
--

DROP TABLE IF EXISTS `useraccounts`;
CREATE TABLE IF NOT EXISTS `useraccounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `RegisterDate` date DEFAULT NULL,
  `FirstName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `LastName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Roll` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `DP` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `useraccounts`
--

INSERT INTO `useraccounts` (`id`, `username`, `password`, `RegisterDate`, `FirstName`, `LastName`, `Email`, `Roll`, `Gender`, `DP`, `Active`) VALUES
(10, 'admin', '123', '2024-04-20', 'Gimhana', 'Deshan', '', 'Admin', 'Male', 'file:/D:/Wildlife_HMS/src/main/resources/img/icons8-user-100-1.png', 1),
(9, 'gimhana', '8008', '2024-04-20', 'Gimhana', 'Deshan', '', 'Researcher', 'Male', 'file:/D:/Wildlife_HMS/src/main/resources/userProfile/Photograph.jpg', 1),
(11, 'janith', '123', '2024-04-23', 'Janith', 'Sandaruwan', '', 'Researcher', 'Male', 'file:/D:/My_Photo/Camera/IMG_20230708_124730.jpg', 0);

-- --------------------------------------------------------

--
-- Table structure for table `vegetationtypes`
--

DROP TABLE IF EXISTS `vegetationtypes`;
CREATE TABLE IF NOT EXISTS `vegetationtypes` (
  `VegetationID` int NOT NULL AUTO_INCREMENT,
  `VG_ID` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `VegetationName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Description` text,
  `HabitatID` int NOT NULL,
  PRIMARY KEY (`VegetationID`),
  UNIQUE KEY `VG_ID` (`VG_ID`),
  KEY `HabitatID` (`HabitatID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `vegetationtypes`
--

INSERT INTO `vegetationtypes` (`VegetationID`, `VG_ID`, `VegetationName`, `Description`, `HabitatID`) VALUES
(9, 'VG00009', 'Evergreen Forest', 'Dense forest with year-round greenery', 83),
(10, 'VG00010', 'Marshland', 'Wetland area with water-tolerant vegetation', 84),
(11, 'VG00011', 'Savanna', 'Grassland with scattered trees', 85);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `environmentalconditions`
--
ALTER TABLE `environmentalconditions`
  ADD CONSTRAINT `environmentalconditions_ibfk_1` FOREIGN KEY (`HabitatID`) REFERENCES `habitats` (`HabitatID`);

--
-- Constraints for table `monitoringalerts`
--
ALTER TABLE `monitoringalerts`
  ADD CONSTRAINT `monitoringalerts_ibfk_1` FOREIGN KEY (`HabitatID`) REFERENCES `habitats` (`HabitatID`);

--
-- Constraints for table `observations`
--
ALTER TABLE `observations`
  ADD CONSTRAINT `observations_ibfk_1` FOREIGN KEY (`SpeciesID`) REFERENCES `species` (`SpeciesID`),
  ADD CONSTRAINT `observations_ibfk_2` FOREIGN KEY (`HabitatID`) REFERENCES `habitats` (`HabitatID`);

--
-- Constraints for table `vegetationtypes`
--
ALTER TABLE `vegetationtypes`
  ADD CONSTRAINT `vegetationtypes_ibfk_1` FOREIGN KEY (`HabitatID`) REFERENCES `habitats` (`HabitatID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
