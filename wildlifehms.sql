-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 21, 2024 at 03:14 PM
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
-- Table structure for table `environmentalconditions`
--

DROP TABLE IF EXISTS `environmentalconditions`;
CREATE TABLE IF NOT EXISTS `environmentalconditions` (
  `ConditionID` int NOT NULL AUTO_INCREMENT,
  `EC_ID` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ConditionType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ConditionValue` decimal(9,2) DEFAULT NULL,
  `HabitatID` int NOT NULL,
  PRIMARY KEY (`ConditionID`),
  UNIQUE KEY `EC_ID` (`EC_ID`),
  KEY `HabitatID` (`HabitatID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `environmentalconditions`
--

INSERT INTO `environmentalconditions` (`ConditionID`, `EC_ID`, `ConditionType`, `ConditionValue`, `HabitatID`) VALUES
(2, 'EC00002', 'dkfkllf7', '4736.60', 72),
(4, 'EC00004', 'hjjk', '45.00', 66);

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
) ENGINE=MyISAM AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `habitats`
--

INSERT INTO `habitats` (`HabitatID`, `HA_ID`, `HabitatName`, `Location`, `Size`, `Description`) VALUES
(65, 'HA00000', 'dsdad', NULL, '12.00', 'hhj'),
(66, 'HA00123', 'n bn m', NULL, '90.50', NULL),
(72, 'HA00072', 'hsjs', '', '45.00', ''),
(78, 'HA00078', 'akdkdk', '', '5.00', 'jkjk'),
(74, 'HA00074', 'qqdfg', '', '89.30', ''),
(75, 'HA00075', 'hkk', '', '78.00', ''),
(76, 'HA00076', 'jhkj', '', '456.00', ''),
(77, 'HA00077', 'kl;', '', '65565.00', ''),
(79, 'HA00079', 'hjjk', '', '45.50', '');

-- --------------------------------------------------------

--
-- Table structure for table `habitatspecies`
--

DROP TABLE IF EXISTS `habitatspecies`;
CREATE TABLE IF NOT EXISTS `habitatspecies` (
  `HabitatSpeciesID` int NOT NULL,
  `HabitatID` int DEFAULT NULL,
  `SpeciesID` int DEFAULT NULL,
  PRIMARY KEY (`HabitatSpeciesID`),
  KEY `HabitatID` (`HabitatID`),
  KEY `SpeciesID` (`SpeciesID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  `AlertStatus` tinyint(1) NOT NULL,
  PRIMARY KEY (`AlertID`),
  UNIQUE KEY `AL_ID` (`AL_ID`),
  KEY `HabitatID` (`HabitatID`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `monitoringalerts`
--

INSERT INTO `monitoringalerts` (`AlertID`, `AL_ID`, `AlertType`, `AlertDescription`, `AlertDate`, `HabitatID`, `AlertStatus`) VALUES
(5, 'AL00005', 'XCvgh', 'Xxl2', '2024-02-04', 72, 0),
(6, 'AL00006', 'XCv', 'Xxl', '2024-02-04', 72, 0),
(7, 'AL00007', 'XCv', 'Xxl00000', '2024-02-12', 72, 0),
(9, 'AL00009', 'jm', 'mk', '2024-02-12', 77, 0),
(10, 'AL00010', 'jm', 'mk,', '2024-02-13', 77, 0),
(12, 'AL00012', 'hjjk', 'jklu', '2024-04-04', 66, 0);

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
  KEY `HabitatID` (`HabitatID`),
  KEY `SpeciesID` (`SpeciesID`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `observations`
--

INSERT INTO `observations` (`ObservationID`, `OB_ID`, `HabitatID`, `SpeciesID`, `ObserverName`, `ObservationDate`, `Notes`, `Attachments`) VALUES
(22, 'OB00022', 66, 1, 'hajd', '2024-04-21', 'jddkf', 'D:\\Wildlife_HMS\\src\\main\\resources\\observationAttachments\\welcome.jpeg');

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
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `species`
--

INSERT INTO `species` (`SpeciesID`, `SP_ID`, `CommonName`, `ScientificName`, `ConservationStatus`, `Description`) VALUES
(1, 'SP00001', 'Bengal Tiger', 'Panthera tigris tigris', 'Endangered', 'skk12'),
(7, 'SP00007', 'asks', 'dffsdf', 'ajkskd', 'skk1'),
(10, 'SP00010', 'jjkke', 'rkkjr', 'Endangered', 'rr');

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
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `useraccounts`
--

INSERT INTO `useraccounts` (`id`, `username`, `password`, `RegisterDate`, `FirstName`, `LastName`, `Email`, `Roll`, `Gender`, `DP`, `Active`) VALUES
(10, 'admin', '123', '2024-04-20', 'Gimhana', 'Deshan', '', 'Admin', 'Male', 'file:/D:/Wildlife_HMS/src/main/resources/img/icons8-user-100-1.png', 1),
(9, 'gimhana', '8008', '2024-04-20', 'Gimhana', 'Deshan', '', 'Admin', 'Male', 'file:/D:/Wildlife_HMS/src/main/resources/userProfile/Photograph.jpg', 1);

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
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `vegetationtypes`
--

INSERT INTO `vegetationtypes` (`VegetationID`, `VG_ID`, `VegetationName`, `Description`, `HabitatID`) VALUES
(6, 'VG00006', 'jefj', 'wjkwr5', 66);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
