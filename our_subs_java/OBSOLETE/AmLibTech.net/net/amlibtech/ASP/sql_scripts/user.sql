# Host: localhost
# Database: ALTASP
# Table: 'user'
# 

USE ALTASP;

DROP TABLE IF EXISTS user;

CREATE TABLE `user` (
  `user_Id` varchar(20) NOT NULL default '',
  `first_Name` varchar(20) NOT NULL default '',
  `last_Name` varchar(20) NOT NULL default '',
  `password` varchar(40) NOT NULL default '',
  `description` varchar(100) NOT NULL default '',
  `timeZone_Name` varchar(100) NOT NULL default '',
  `reference_Type` varchar(30) NOT NULL default '',
  `reference_Id` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`user_Id`)
) TYPE=InnoDB; 
