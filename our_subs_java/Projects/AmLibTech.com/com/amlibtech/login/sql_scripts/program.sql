USE ALTASP;

DROP TABLE IF EXISTS program;



CREATE TABLE `program` (
	`program_Name` varchar(60) NOT NULL default '',
	`program_Title` varchar(80) NOT NULL default '',
	`subsystem_Id` varchar(30) NOT NULL default '',
        PRIMARY KEY  (`program_Name`)
) TYPE=InnoDB;

