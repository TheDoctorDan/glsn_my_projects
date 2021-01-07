USE ALTASP;

DROP TABLE IF EXISTS subsystem;

CREATE TABLE `subsystem` (
	`subsystem_Id` varchar(30) NOT NULL default '',
	`description` varchar(30) NOT NULL default '',
	`package_Name` varchar(60) NOT NULL default '',
	`servlet_Name` varchar(60) NOT NULL default '',
        PRIMARY KEY  (`subsystem_Id`)
) TYPE=InnoDB;

