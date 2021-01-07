# Host: localhost
# Database: ALTASP
# Table: 'subscriber_organization'
# 

USE ALTASP;

DROP TABLE IF EXISTS subscriber_organization;



CREATE TABLE `subscriber_organization` (
	`subscriber_Organization_Id` varchar(30) NOT NULL default '',
	`name` varchar(38) NOT NULL default '',
	`description` varchar(100) NOT NULL default '',
	`database_Server_URL` varchar(200) NOT NULL default '',
	`database_Name` varchar(100) NOT NULL default '',
	`database_Access_Login_Name` varchar(30) NOT NULL default '',
	`database_Access_Login_Password` varchar(30) NOT NULL default '',
        PRIMARY KEY  (`subscriber_Organization_Id`)
) TYPE=InnoDB;

