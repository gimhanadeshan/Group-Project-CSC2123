-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 13, 2024 at 08:11 AM
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
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `alertstatus`
--

INSERT INTO `alertstatus` (`AlertStatusID`, `AlertStatus`, `Remarks`) VALUES
(1, 'Under Review', 'Indicates that the alert is currently being assessed or evaluated.'),
(2, 'Pending', 'Suggests that action is pending or awaiting further information before resolution.'),
(3, 'In Progress', 'Shows that steps are being taken to address the alert but it\'s not yet resolved.'),
(4, 'Closed', 'Signifies that the alert has been resolved and no further action is required.'),
(5, 'Deferred', 'Indicates that the alert resolution has been postponed or delayed for some reason.'),
(6, 'Cancelled', 'Shows that the alert is no longer valid or relevant and has been cancelled.'),
(7, 'Awaiting Approval', 'Indicates that the proposed resolution is awaiting approval from a higher authority.'),
(8, 'Escalated', 'Suggests that the alert has been escalated to higher levels of management or expertise for resolution.'),
(9, 'On Hold', ' Signifies that the alert resolution process has been temporarily halted.'),
(10, 'Reopened', ' Indicates that a previously closed alert has been reopened due to recurrence or unresolved issues.');

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
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `alerttypes`
--

INSERT INTO `alerttypes` (`AlertTypeID`, `AlertType`, `Remarks`) VALUES
(1, 'Poaching', 'Indicates illegal hunting or capturing of wildlife in the habitat.'),
(2, 'Habitat Destruction', 'Signifies significant damage or destruction to the natural habitat.'),
(3, 'Pollution', 'Indicates contamination of the habitat by harmful substances or pollutants.'),
(4, 'Human Activity', ' Suggests disturbance caused by human activities such as construction, logging, or farming.'),
(5, 'Climate Change', 'Indicates adverse effects on the habitat due to changes in climate patterns.'),
(6, 'Predator Presence', 'Signifies the presence of predators that pose a threat to the habitat\'s species.'),
(7, 'Disease Outbreak', 'Indicates the outbreak of a contagious disease affecting wildlife in the habitat.'),
(8, 'Invasive Plants', 'Signifies the presence of non-native plant species that may harm the habitat\'s ecosystem.'),
(9, 'Invasive Animals', 'Indicates the presence of non-native animal species that may disrupt the habitat\'s balance.'),
(10, 'Habitat Fragmentation', 'Suggests the breaking up of habitat into smaller, isolated patches due to human activities or natural processes.');

-- --------------------------------------------------------

--
-- Table structure for table `companydetails`
--

DROP TABLE IF EXISTS `companydetails`;
CREATE TABLE IF NOT EXISTS `companydetails` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `TelNo` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `companydetails`
--

INSERT INTO `companydetails` (`ID`, `Name`, `Address`, `Email`, `TelNo`, `Logo`) VALUES
(1, 'HabitatGuard Solutions', '123 Greenway Ave,Wildlife Park City.', 'info@habitatguardsolutions.com', '+1 (555) 123-4', 'file:/C:/Users/Gimhan/Desktop/logo.png');

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
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `conservationstatus`
--

INSERT INTO `conservationstatus` (`ConservationStatusID`, `ConservationStatus`, `Remark`) VALUES
(1, 'Least Concern (LC)', 'Species that are abundant and widespread, with no immediate threat of extinction.'),
(2, 'Near Threatened (NT)', 'Species that are close to qualifying for a threatened status but are not yet considered threatened.'),
(3, 'Vulnerable (VU)', 'Species that are facing a high risk of extinction in the wild in the near future.'),
(4, 'Endangered (EN)', 'Species that are at a very high risk of extinction in the wild.'),
(5, 'Critically Endangered (CR)', 'Species that are at an extremely high risk of extinction in the wild, with very few individuals remaining.'),
(6, 'Extinct in the Wild (EW)', 'Species that exist only in captivity or in a controlled environment, with no remaining wild populations.'),
(7, 'Extinct (EX)', 'Species that no longer exist, with no surviving individuals.');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `environmentalconditions`
--

INSERT INTO `environmentalconditions` (`ConditionID`, `EC_ID`, `ConditionType`, `ConditionValue`, `HabitatID`, `PostingDate`) VALUES
(1, 'EC00001', 'Temperature', '25.50', 1, '2024-05-13'),
(2, 'EC00002', 'Humidity', '70.20', 1, '2024-05-13');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `habitats`
--

INSERT INTO `habitats` (`HabitatID`, `HA_ID`, `HabitatName`, `Location`, `Size`, `Description`) VALUES
(1, 'HA00001', 'Forest Reserve', '(35.6895, -78.9196)', '500.00', 'Lush green forest with diverse flora and fauna'),
(2, 'HA00002', 'Wetland Preserve', '(40.7128, -74.0060)', '300.00', 'Rich in aquatic life and waterfowl'),
(3, 'HA00003', 'Grassland Habitat', '(36.7783, -119.4179)', '700.00', 'Open grassy landscape supporting herbivores');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `monitoringalerts`
--

INSERT INTO `monitoringalerts` (`AlertID`, `AL_ID`, `AlertType`, `AlertDescription`, `AlertDate`, `HabitatID`, `AlertStatus`) VALUES
(1, 'AL00001', 'Habitat Destruction', 'Increased logging activity in the area', '2024-05-13', 1, 'Pending');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `observations`
--

INSERT INTO `observations` (`ObservationID`, `OB_ID`, `HabitatID`, `SpeciesID`, `ObserverName`, `ObservationDate`, `Notes`, `Attachments`) VALUES
(1, 'OB00001', 1, 1, 'John Doe', '2024-05-13', 'Spotted a Bengal Tiger near the river', '/D:/Wildlife_HMS/src/main/resources/observationAttachments/welcome.jpeg\n');

-- --------------------------------------------------------

--
-- Table structure for table `population`
--

DROP TABLE IF EXISTS `population`;
CREATE TABLE IF NOT EXISTS `population` (
  `PopulationID` int NOT NULL AUTO_INCREMENT,
  `PO_ID` varchar(8) DEFAULT NULL,
  `HabitatID` int NOT NULL,
  `SpeciesID` int NOT NULL,
  `PopulationSize` int NOT NULL,
  `LastSurveyDate` date NOT NULL,
  PRIMARY KEY (`PopulationID`),
  KEY `HabitatID` (`HabitatID`,`SpeciesID`),
  KEY `SpeciesID` (`SpeciesID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `population`
--

INSERT INTO `population` (`PopulationID`, `PO_ID`, `HabitatID`, `SpeciesID`, `PopulationSize`, `LastSurveyDate`) VALUES
(1, 'PL00001', 1, 1, 37, '2024-05-13');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `species`
--

INSERT INTO `species` (`SpeciesID`, `SP_ID`, `CommonName`, `ScientificName`, `ConservationStatus`, `Description`) VALUES
(1, 'SP00001', 'Bengal Tiger', 'Panthera tigris tigris', 'Endangered (EN)', 'Large carnivorous mammal native to Asia'),
(2, 'SP00002', 'Red Fox', 'Vulpes vulpes', 'Least Concern (LC)', 'Small omnivorous mammal with a distinctive coat'),
(3, 'SP00003', 'Oak Tree', 'Quercus robur', 'Near Threatened (NT)', 'Deciduous tree species common in temperate regions');

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_2` (`username`),
  UNIQUE KEY `username` (`username`,`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `useraccounts`
--

INSERT INTO `useraccounts` (`id`, `username`, `password`, `RegisterDate`, `FirstName`, `LastName`, `Email`, `Roll`, `Gender`, `DP`, `Active`) VALUES
(1, 'admin', '123', '2024-05-13', 'Gimhana', 'Deshan', 'admin@gmail.com', 'Administrator', 'Male', 'file:/D:/Wildlife_HMS/target/classes/img/icons8-user-100.png', 1);

-- --------------------------------------------------------

--
-- Table structure for table `userpermissions`
--

DROP TABLE IF EXISTS `userpermissions`;
CREATE TABLE IF NOT EXISTS `userpermissions` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `UserName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `HabitatManagement` tinyint(1) DEFAULT NULL,
  `UserManagement` tinyint(1) DEFAULT NULL,
  `FieldDataCollection` tinyint(1) DEFAULT NULL,
  `Research` tinyint(1) DEFAULT NULL,
  `Reporting` tinyint(1) DEFAULT NULL,
  `Settings` tinyint(1) DEFAULT NULL,
  `OtherFiles` tinyint(1) DEFAULT NULL,
  `CreateP` tinyint(1) DEFAULT NULL,
  `DeleteP` tinyint(1) DEFAULT NULL,
  `UpdateP` tinyint(1) DEFAULT NULL,
  `View` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `UserName` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `userpermissions`
--

INSERT INTO `userpermissions` (`ID`, `UserName`, `HabitatManagement`, `UserManagement`, `FieldDataCollection`, `Research`, `Reporting`, `Settings`, `OtherFiles`, `CreateP`, `DeleteP`, `UpdateP`, `View`) VALUES
(1, 'admin', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
-- Constraints for table `population`
--
ALTER TABLE `population`
  ADD CONSTRAINT `population_ibfk_1` FOREIGN KEY (`HabitatID`) REFERENCES `habitats` (`HabitatID`),
  ADD CONSTRAINT `population_ibfk_2` FOREIGN KEY (`SpeciesID`) REFERENCES `species` (`SpeciesID`);

--
-- Constraints for table `userpermissions`
--
ALTER TABLE `userpermissions`
  ADD CONSTRAINT `userpermissions_ibfk_1` FOREIGN KEY (`UserName`) REFERENCES `useraccounts` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `vegetationtypes`
--
ALTER TABLE `vegetationtypes`
  ADD CONSTRAINT `vegetationtypes_ibfk_1` FOREIGN KEY (`HabitatID`) REFERENCES `habitats` (`HabitatID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
